package com.github.cfogrady.vbnfc.handlers

import android.nfc.tech.NfcA
import android.util.Log
import com.github.cfogrady.vbnfc.ConvertToPages
import com.github.cfogrady.vbnfc.NfcCharacter
import com.github.cfogrady.vbnfc.NfcCharacterFactory
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

abstract class VBNfcHandler(private val secrets: Secrets, private val nfcData: NfcA, private val nfcCharacterFactory: NfcCharacterFactory = NfcCharacterFactory()) {
    abstract fun readHeader()
    abstract fun getDeviceId(): UShort

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
        const val OPERATION_TRANSFERED_TO_DEVICE: Byte = 4
        const val START_DATA_PAGE = 8
        const val LAST_DATA_PAGE = 220 // technically 223, but we read 4 pages at a time.

        const val HMAC256 = "HmacSHA256"

    }

    @OptIn(ExperimentalStdlibApi::class)
    fun receiveCharacter() {
        Log.i(TAG, "Writing to make ready for operation")
        nfcData.transceive(getOperationCommandBytes(OPERATION_READY))
        Log.i(TAG, "Authenticating")

        passwordAuth()
        Log.i(TAG, "Reading Character")
        val encryptedCharacterData = readNfcData()
        Log.i(TAG, "Raw NFC Data Received: ${encryptedCharacterData.toHexString()}")
        val decryptedCharacterData = decryptData(encryptedCharacterData, nfcData.tag.id)
        validateCharacterData(decryptedCharacterData)
        val nfcCharacter = nfcCharacterFactory.buildNfcCharacterFromBytes(decryptedCharacterData, getDeviceId())
        Log.i(TAG, "Known Character Stats: $nfcCharacter")
        Log.i(TAG, "Signaling operation complete")
        nfcData.transceive(getOperationCommandBytes(OPERATION_TRANSFERRED_TO_APP))
    }

    private fun readNfcData(): ByteArray {
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
        // set app nonce to device ensure when we send back the character that we are preparing
        // the same device we send to
        nfcData.transceive(getOperationCommandBytes(OPERATION_READY))
        // app authenticates, and reads everything, and checks the version
        // The version check is only for the BE when transfering from DIM=0 (pulsemon).
        // This was from the bug when the BE first came out.
        // Check (page 103 [0:1] != 1, 0)
        setHeaderDimId(dimId)
        nfcData.transceive(getOperationCommandBytes(OPERATION_CHECK_DIM))
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun sendCharacter(character: NfcCharacter) {
        // check the nonce
        // if it's not expected, then the bracelet isn't ready

        // Check the product and device ids that they match the target.
        // This check relies on the app categories of BE, Vital Hero, and Vital Series.

        // ensure the dim id matches the expected
        if (character.dimId != getHeaderDimId()) {
            throw IllegalArgumentException("Device is ready for DIM ${getHeaderDimId()}, but attempted to send ${character.dimId}")
        }

        // update the memory data
        nfcData.transceive(getOperationCommandBytes(OPERATION_READY))
        passwordAuth()

        // write nfc data
        val myCharacter = "551abd291a96404cb38cd99babe1184b43eb2479a65c59ebd7e0f3246845a54c5f9dc34770bc69e18ba7bc147c18516f3524c78631f16496e4492e97f74a289105d83d6cf1908eb66565fd4018986ef20862ce4a9376efcb001da75135c5c3fd93f4b9dad14d46c92f192c8e39cbd669c72f27d273a6ddcf554203761924dc1697f5e592c01dcdcc335a4f3eb89cd478907449b3401c0ae9cd0c56cc140a35dcc3dc9ad17a475ac884129d662e4525c14349d1ce507d59a34b1dbe60d1f7d8842da3f1f4604710eef661c3de5291cf33e5569e5abacf9b601d45eee68a7b6eaff0f21724e90cad6b721244d57d78da67d735a0e73deb431d6fb1b8fb755787ee51e8a20da957f97d62c408e6572f865be94aca43fa19116ac318fd2700f093011438f47eaac2e85d538042ff93ce6bc333589f6d341555d36fbba74567ef613d2242c9471f2ed7aa3e565cecd46fa2ca8f88a022c714fc04f0f69c43e74af7ad7bdec18794d9bc03e2e06773a54c3ddbfe76ee5f96845293ef8fc68653831c405edbd8594a620502cb506770a89ba999d4df0d23c40d021e7c2e7159d2f380cbeb77c5d97c9b503503e7dbbae37c42a4a2a4985cabbbc979d42aaac4651c95c41dc4b0baec7d7199e9d929836e13d3e3f8c958facb305f339992162ab0258b42dfa4a02ad3ec28d48c60909e008c5fb2b2e976942784e098313d75e8a6eec78d559aa2617c35574c7a0c5f8f7e103c77087b213d6b02c444a1fb4eab23a967ddac02f9b959675c69f3c97894929c4d5a2779444b05246da35744aab4b545eefc9478620d6e1fb584a9f79080236e005cf738228c104570c200e11b6307a2d236c6031b0f89da38ce1b401eed4dda2b78268d316115b2e9c222441d9cd8d4b559adb9f1ce4a8f2a0ed3f55034e47c778d85654ac1e5048326827f0db6103cfcce491d6bf084a9e7ee2a1446c616c87f1246fb2da22a6eaae4167814f24230aed64e342cae18742c21e4b2f2c600ce783f7ec33f6937a2224a366cd1feb53011e766d0d5459d1b83a1a9147be8ddc8fea75bbdeac18077aaa469898354ff400608e2dac32be82ff7528252c0da681d2fe78150f6020c3691b5cc6cb81285f6716acf0040b346d1b76cd61bc08377f67a8adc341ceda34585ddefeee1dfc416d05067015984e3619bcbc188d8d49ab4024c9e1b83a6c0fd91b4e835d59b3f59d97d".hexToByteArray()
        val pagedData = ConvertToPages(myCharacter)
        for(pageToWriteIdx in 8..<pagedData.size) {
            val pageToWrite = pagedData[pageToWriteIdx]
            nfcData.transceive(byteArrayOf(NFC_WRITE_COMMAND, pageToWriteIdx.toByte(), pageToWrite[0], pageToWrite[1], pageToWrite[2], pageToWrite[3]))
        }


        nfcData.transceive(getOperationCommandBytes(OPERATION_TRANSFERED_TO_DEVICE))

    }

    abstract fun getHeaderDimId(): UShort
    abstract fun setHeaderDimId(dimId: UShort)

    abstract fun getOperationCommandBytes(operation: Byte): ByteArray

    private fun encodeBase64Url(value: String): String {
        // originally: encode to base 64 string and replace newlines with blank, '=' with blank, '+' with minus, and '/' with '_'
        val encoded = Base64.getUrlEncoder().encodeToString(value.toByteArray())
        return encoded
    }

    data class Secrets(val passwordKey1: String, val passwordKey2: String, val decryptionKey: String, val substitutionCypher: IntArray)

    @OptIn(ExperimentalStdlibApi::class)
    fun passwordAuth() {
        val tagId = nfcData.tag.id
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

    val pagesWithChecksum = hashSetOf(8, 16, 24, 32, 40, 48, 52, 56, 60, 64, 68, 72, 76, 80, 84, 104, 192, 200, 208, 216)

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