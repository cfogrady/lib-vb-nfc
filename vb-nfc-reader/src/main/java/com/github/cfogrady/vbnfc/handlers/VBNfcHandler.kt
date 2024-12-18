package com.github.cfogrady.vbnfc.handlers

import android.nfc.tech.NfcA
import android.util.Log
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

abstract class VBNfcHandler(private val secrets: Secrets, private val nfcData: NfcA) {
    abstract fun readHeader()
    abstract fun sendCharacter()
    abstract fun getDeviceId(): Int

    companion object {
        const val TAG = "VBNfcHandler"
        const val FIRST_DATA_PAGE: Byte = 0x04
        const val NFC_PASSWORD_COMMAND: Byte = 0x1b
        const val NFC_READ_COMMAND: Byte = 0x30
        const val NFC_WRITE_COMMAND: Byte = 0xA2.toByte()

        const val STATUS_IDLE: Byte = 0
        const val STATUS_READY: Byte = 1

        const val OPERATION_IDLE: Byte = 0
        const val OPERATION_READY: Byte = 1
        const val OPERATION_TRANSFERRED_TO_APP: Byte = 2
        const val OPERATION_CHECK_DIM: Byte = 3
        const val START_DATA_PAGE = 8
        const val LAST_DATA_PAGE = 220 // technically 223, but we read 4 pages at a time.

        const val HMAC256 = "HmacSHA256"

    }

    fun receiveCharacter() {
        Log.i(TAG, "Writing to make ready for operation")
        nfcData.transceive(getOperationCommandBytes(OPERATION_READY))
        Log.i(TAG, "Authenticating")
        passwordAuth(nfcData.tag.id)
        Log.i(TAG, "Reading Character")
        readCharacter()
        Log.i(TAG, "Signaling operation complete")
        nfcData.transceive(getOperationCommandBytes(OPERATION_TRANSFERRED_TO_APP))
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun readCharacter() {
        val result = ByteArray(((LAST_DATA_PAGE+4)- START_DATA_PAGE) * 4)
        for (page in START_DATA_PAGE..LAST_DATA_PAGE step 4) {
            val pages = nfcData.transceive(byteArrayOf(NFC_READ_COMMAND, page.toByte()))
            if (pages.size < 16) {
                throw Exception("Failed to read page: $page")
            }
            System.arraycopy(pages, 0, result, (page - START_DATA_PAGE)*4, pages.size)
        }
        val resultsAsHex = result.toHexString()
        Log.i(TAG, "Results: $resultsAsHex")

    }

    abstract fun getOperationCommandBytes(operation: Byte): ByteArray

    private fun encodeBase64Url(value: String): String {
        // originally: encode to base 64 string and replace newlines with blank, '=' with blank, '+' with minus, and '/' with '_'
        val encoded = Base64.getUrlEncoder().encodeToString(value.toByteArray())
        return encoded
    }

    data class VBNfcHeader(val magic: UInt, val itemId: UShort, val itemNumber: UShort)

    data class Secrets(val secretKey1: String, val secretKey2: String, val substitutionCypher: IntArray)

    @OptIn(ExperimentalStdlibApi::class)
    fun passwordAuth(inputData: ByteArray) {
        val password = createPassword(inputData)
        try {
            val result = nfcData.transceive(byteArrayOf(NFC_PASSWORD_COMMAND, password[0], password[1], password[2], password[3]))
            Log.i(TAG, "PasswordAuth Result: ${result.toHexString()}")
            if (result.size == 1) {
                throw AuthenticationException("Authentication failed. Result: ${result.toHexString()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
            throw e
        }
    }

    // Creates a 4 byte password by hashing the current data using HMAC256 with the first key. Then
    // applying a 4-bit substitution cypher on the result, and hashing again with the second key.
    // The password are the 4 bytes starting at index 28 of that result.
    @OptIn(ExperimentalStdlibApi::class)
    internal fun createPassword(inputData: ByteArray): ByteArray {
        val key1 = encodeBase64Url(secrets.secretKey1)
        val key2 = encodeBase64Url(secrets.secretKey2)
        val mac = Mac.getInstance(HMAC256)
        mac.init(SecretKeySpec(key1.toByteArray(), HMAC256))
        var macResult = mac.doFinal(inputData)
        var substitutedBytes = apply4BitSubstitutionCypher(macResult)
        mac.init(SecretKeySpec(key2.toByteArray(), HMAC256))
        macResult = mac.doFinal(substitutedBytes)
        return macResult.sliceArray(28..<32)
    }

    // This is a 4 bit substitution cypher, where each 4 bits act as an index to another 4 bits
    private fun apply4BitSubstitutionCypher(data: ByteArray): ByteArray {
        val result = ByteArray(data.size)
        for (idx in 0..<data.size) {
            val byte: Int = data[idx].toInt()
            var newByte = 0
            for (fourBitShifts in 0..<2) { // perform one OR without shift, and one OR shifted 4 bits
                val shift = fourBitShifts * 4
                val permutationIndex = (byte shr shift) and 0xF
                newByte = newByte or (secrets.substitutionCypher[permutationIndex] shl shift)
            }
            result[idx] = newByte.toByte()
        }
        return result
    }

    class AuthenticationException(message: String): Exception(message)
}