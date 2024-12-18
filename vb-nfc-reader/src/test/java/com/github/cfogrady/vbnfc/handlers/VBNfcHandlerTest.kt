package com.github.cfogrady.vbnfc.handlers

import android.nfc.tech.NfcA
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test

class VBNfcHandlerTest {

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun createPasswordIsCorrect() {

        val mockNfcA = mockk<NfcA>()

        mockkStatic(android.util.Base64::class)
        every { android.util.Base64.encodeToString(any<ByteArray>(), any<Int>()) } answers {
            val input = it.invocation.args[0] as ByteArray
            val flags = it.invocation.args[1] as Int
            java.util.Base64.getUrlEncoder().encodeToString(input)
        }
        mockkStatic(android.util.Log::class)
        every { android.util.Log.i(any<String>(), any<String>()) } answers {
            val message = it.invocation.args[1] as String
            println(message)
            1
        }

        val secrets = VBNfcHandler.Secrets("password1", "password2", intArrayOf(15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0))

        val vbNfcHandler = object : VBNfcHandler(secrets, mockNfcA) {
            override fun readHeader() {
            }

            override fun sendCharacter() {
            }

            override fun getDeviceId(): Int {
                return -1
            }

            override fun getOperationCommandBytes(operation: Byte): ByteArray {
                return byteArrayOf(0)
            }
        }
        val input = byteArrayOf(0x04, 0x40, 0xaf.toByte(), 0xa2.toByte(), 0xee.toByte(), 0x0f, 0x90.toByte())
        val result = vbNfcHandler.createPassword(input)
        val expected = "00dce58e"

        Assert.assertEquals(expected, result.toHexString())
    }
}