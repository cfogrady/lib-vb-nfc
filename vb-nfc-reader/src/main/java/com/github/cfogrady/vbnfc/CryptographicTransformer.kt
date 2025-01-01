package com.github.cfogrady.vbnfc

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

class CryptographicTransformer(private val salt1: String, private val salt2: String, private val decryptionKey: String, private val substitutionCipher: IntArray) {

    // Creates a 4 byte password by hashing the current data using HMAC256 with the first key. Then
    // applying a 4-bit substitution cypher on the result, and hashing again with the second key.
    // The password are the 4 bytes starting at index 28 of that result.
    @OptIn(ExperimentalStdlibApi::class)
    fun createNfcPassword(inputData: ByteArray): ByteArray {
        val salt1 = decryptSalt(salt1)
        val salt2 = decryptSalt(salt2)
        val hashedInput = generateHMacSHA256Hash(salt1, inputData)
        val substitutedBytes = apply4BitSubstitutionCipher(hashedInput)
        val secondHash = generateHMacSHA256Hash(salt2, substitutedBytes)
        return secondHash.sliceArray(28..<32)
    }

    fun decryptData(data: ByteArray, tagId: ByteArray): ByteArray {
        val salt1 = decryptSalt(salt1)
        val salt2 = decryptSalt(salt2)
        return cryptoTransformation(Cipher.DECRYPT_MODE, data, tagId, salt1, salt2)
    }

    fun encryptData(data: ByteArray, tagId: ByteArray): ByteArray {
        val salt1 = decryptSalt(salt1)
        val salt2 = decryptSalt(salt2)
        return cryptoTransformation(Cipher.ENCRYPT_MODE, data, tagId, salt1, salt2)
    }

    private fun decryptSalt(str: String): String {
        val decoded = Base64.getDecoder().decode(str)
        val decrypt = decryptAesCbcPkcs5Padding(decryptionKey, decoded)
        return String(decrypt, StandardCharsets.UTF_8)
    }

    private fun decryptAesCbcPkcs5Padding(key: String, data: ByteArray): ByteArray {
        val keyBytes = key.toByteArray(StandardCharsets.UTF_8)
        val rightSizedKey = keyBytes.copyOf(32)
        val ivBytes = keyBytes.copyOfRange(key.length - 16, key.length)
        val secretKeySpec = SecretKeySpec(rightSizedKey, "AES")
        val ivParameterSpec = IvParameterSpec(ivBytes)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)
        return cipher.doFinal(data)
    }

    private fun generateHMacSHA256Hash(salt: String, data: ByteArray): ByteArray {
        val saltBytes = salt.toByteArray(StandardCharsets.US_ASCII)
        val secretKeySpec = SecretKeySpec(saltBytes, "HMacSHA256")
        val mac = Mac.getInstance("HMacSHA256")
        mac.init(secretKeySpec)
        return mac.doFinal(data)
    }

    // This is a 4 bit substitution cipher, where each 4 bits act as an index to another 4 bits
    private fun apply4BitSubstitutionCipher(data: ByteArray): ByteArray {
        val result = ByteArray(data.size)
        for (idx in 0..<data.size) {
            val byte: Int = data[idx].toInt()
            var newByte = 0
            for (fourBitShifts in 0..<2) { // perform one OR without shift, and one OR shifted 4 bits
                val shift = fourBitShifts * 4
                val permutationIndex = (byte shr shift) and 0xF
                newByte = newByte or (substitutionCipher[permutationIndex] shl shift)
            }
            result[idx] = newByte.toByte()
        }
        return result
    }

    // Hashes the tagId once and applies substitution cipher. Then hashes again.
    // Splits hash and original tagId into key and initialization vector
    private fun cryptoTransformation(cipherMode: Int, data: ByteArray, tagId: ByteArray, salt1: String, salt2: String): ByteArray {
        var hashedTagId = apply4BitSubstitutionCipher(generateHMacSHA256Hash(salt1, tagId))
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



    private fun xorBytes(data1: ByteArray, data2: ByteArray, resultSize: Int): ByteArray {
        val results = ByteArray(resultSize)
        results.fill(0)
        for (i in data1.indices) {
            results[i] = data1[i] xor data2[i]
        }
        return results
    }
}