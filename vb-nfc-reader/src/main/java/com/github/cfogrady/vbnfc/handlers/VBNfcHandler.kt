package com.github.cfogrady.vbnfc.handlers

import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.util.Log
import com.github.cfogrady.vbnfc.NfcCharacter
import java.lang.IllegalStateException
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

abstract class VBNfcHandler(private val secrets: Secrets, private val nfcData: NfcA) {
    abstract fun readHeader()
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

        passwordAuth()
        Log.i(TAG, "Reading Character")
        val characterData = readCharacter()
        validateCharacterData(characterData)
        Log.i(TAG, "Signaling operation complete")
        nfcData.transceive(getOperationCommandBytes(OPERATION_TRANSFERRED_TO_APP))
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun readCharacter(): ByteArray {
        val result = ByteArray(((LAST_DATA_PAGE+4)- START_DATA_PAGE) * 4)
        for (page in START_DATA_PAGE..LAST_DATA_PAGE step 4) {
            val pages = nfcData.transceive(byteArrayOf(NFC_READ_COMMAND, page.toByte()))
            if (pages.size < 16) {
                throw Exception("Failed to read page: $page")
            }
            System.arraycopy(pages, 0, result, (page - START_DATA_PAGE)*4, pages.size)
        }
        return result
    }

    fun prepareDIMForCharacter(dimId: UShort) {

    }

    fun sendCharacter(character: NfcCharacter) {

    }

    abstract fun getOperationCommandBytes(operation: Byte): ByteArray

    private fun encodeBase64Url(value: String): String {
        // originally: encode to base 64 string and replace newlines with blank, '=' with blank, '+' with minus, and '/' with '_'
        val encoded = Base64.getUrlEncoder().encodeToString(value.toByteArray())
        return encoded
    }

    data class VBNfcHeader(val magic: UInt, val itemId: UShort, val itemNumber: UShort)

    data class Secrets(val passwordKey1: String, val passwordKey2: String, val decryptionKey: String, val substitutionCypher: IntArray)

    @OptIn(ExperimentalStdlibApi::class)
    fun passwordAuth() {
        NfcAdapter.EXTRA_ID
        var tagId = nfcData.tag.id
        Log.i(TAG, "TagId: ${tagId.toHexString()}")
        val password = createPassword(tagId)
        try {
            val result = nfcData.transceive(byteArrayOf(NFC_PASSWORD_COMMAND, password[0], password[1], password[2], password[3]))
            Log.i(TAG, "PasswordAuth Result: ${result.toHexString()}")
            if (result.size == 1) {
                throw AuthenticationException("Authentication failed. Result: ${result.toHexString()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
        }
    }

    // Creates a 4 byte password by hashing the current data using HMAC256 with the first key. Then
    // applying a 4-bit substitution cypher on the result, and hashing again with the second key.
    // The password are the 4 bytes starting at index 28 of that result.
    @OptIn(ExperimentalStdlibApi::class)
    internal fun createPassword(inputData: ByteArray): ByteArray {
        val salt1 = decodeBase64AndDecrypt(secrets.passwordKey1)
        val salt2 = decodeBase64AndDecrypt(secrets.passwordKey2)
        val hashedInput = generateHMacSHA256Hash(salt1, inputData)
        val substitutedBytes = apply4BitSubstitutionCypher(hashedInput)
        val secondHash = generateHMacSHA256Hash(salt2, substitutedBytes)
        return secondHash.sliceArray(28..<32)
    }

    private fun decodeBase64AndDecrypt(str: String): String {
        val decoded = Base64.getDecoder().decode(str)
        val decrypt = decrypt(secrets.decryptionKey, decoded)
        return String(decrypt, StandardCharsets.UTF_8)
    }

    private fun decrypt(key: String, data: ByteArray): ByteArray {
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8)
        val rightSizedKey = keyBytes.copyOf(32)
        val ivBytes = keyBytes.copyOfRange(key.length - 16, key.length)
        val secretKeySpec = SecretKeySpec(rightSizedKey, "AES")
        val ivParameterSpec = IvParameterSpec(ivBytes)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        return cipher.doFinal(data)
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

    internal fun decryptData(data: ByteArray, tagId: ByteArray): ByteArray {
        val salt1 = decodeBase64AndDecrypt(secrets.passwordKey1)
        val salt2 = decodeBase64AndDecrypt(secrets.passwordKey2)
        return cryptoTransformation(Cipher.DECRYPT_MODE, data, tagId, salt1, salt2)
    }

    val pagesWithChecksum = hashSetOf(8, 16, 32, 40, 48, 52, 56, 60, 64, 68, 72, 76, 80, 84, 104, 192, 200, 208, 216)

    @OptIn(ExperimentalStdlibApi::class)
    internal fun validateCharacterData(data: ByteArray) {
        // loop through all data
        for(i in data.indices step 16) {
            val page = i/4 + 8 // first 8 pages are header data and not part of the character data
            if (pagesWithChecksum.contains(page)) {
                var sum = 0
                val checksumIndex = i + 15
                for(j in i..<checksumIndex) {
                    sum += data[j]
                }
                val checksumByte = (sum and 0xff).toByte()
                if (checksumByte != data[checksumIndex]) {
                    throw IllegalStateException("Checksum ${checksumByte.toHexString()} doesn't match expected ${data[checksumIndex].toHexString()}")
                }
            }
        }
    }

    // Hashes the tagId once and applies substitution cipher. Then hashes again.
    // Splits hash and original tagId into key and initialization vector
    private fun cryptoTransformation(cipherMode: Int, data: ByteArray, tagId: ByteArray, salt1: String, salt2: String): ByteArray {
        var hashedTagId = apply4BitSubstitutionCypher(generateHMacSHA256Hash(salt1, tagId))
        hashedTagId = generateHMacSHA256Hash(salt2, hashedTagId) // second hash

        // generate actual key and initializing vector from tagIdGeneratedKey
        val iv1 = ByteArray(15)
        hashedTagId.copyInto(iv1, 0, 24, 32)
        tagId.copyInto(iv1, 8, 0, 7)
        val iv2 = hashedTagId.copyOf(15)
        val ivParameterSpec = IvParameterSpec(xorBytes(iv1, iv2, 16))
        val secretKeySpec = SecretKeySpec(hashedTagId.copyOf(16), "AES")
        val cipher = Cipher.getInstance("AES/CTR/NoPadding")
        cipher.init(cipherMode, secretKeySpec, ivParameterSpec)
        return cipher.doFinal(data)
    }

    private fun generateHMacSHA256Hash(salt: String, data: ByteArray): ByteArray {
        val saltBytes = salt.toByteArray(StandardCharsets.US_ASCII)
        val secretKeySpec = SecretKeySpec(saltBytes, "HMacSHA256")
        val mac = Mac.getInstance("HMacSHA256")
        mac.init(secretKeySpec)
        return mac.doFinal(data)
    }

    private fun xorBytes(data1: ByteArray, data2: ByteArray, resultSize: Int): ByteArray {
        val results = ByteArray(resultSize)
        results.fill(0)
        for (i in data1.indices) {
            results[i] = data1[i] xor data2[i]
        }
        return results
    }

    class AuthenticationException(message: String): Exception(message)
}