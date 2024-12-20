package com.github.cfogrady.vbnfc.handlers

import android.nfc.tech.NfcA
import com.github.cfogrady.vbnfc.NfcCharacterFactory
import com.github.cfogrady.vbnfc.VBBESecrets
import com.github.cfogrady.vbnfc.getUInt16
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test
import java.nio.ByteOrder

class VBNfcHandlerTest {

    val testTagId = byteArrayOf(0x04, 0x40, 0xaf.toByte(), 0xa2.toByte(), 0xee.toByte(), 0x0f, 0x90.toByte())

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun createPasswordIsCorrect() {

        val mockNfcA = mockk<NfcA>()

        mockkStatic(android.util.Base64::class)
        every { android.util.Base64.encodeToString(any<ByteArray>(), any<Int>()) } answers {
            val input = it.invocation.args[0] as ByteArray
            java.util.Base64.getUrlEncoder().encodeToString(input)
        }
        mockkStatic(android.util.Log::class)
        every { android.util.Log.i(any<String>(), any<String>()) } answers {
            val message = it.invocation.args[1] as String
            println(message)
            1
        }

        val vbNfcHandler = object : VBNfcHandler(VBBESecrets, mockNfcA) {
            override fun readHeader() {
            }

            override fun getDeviceId(): UShort {
                return 0u
            }

            override fun getHeaderDimId(): UShort {
                TODO("Not yet implemented")
            }

            override fun setHeaderDimId(dimId: UShort) {
                TODO("Not yet implemented")
            }

            override fun getOperationCommandBytes(operation: Byte): ByteArray {
                return byteArrayOf(0)
            }
        }
        val result = vbNfcHandler.createPassword(testTagId)
        val expected = "5651b1c8"

        Assert.assertEquals(expected, result.toHexString())
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun validateDataDecryption() {
        val character = "551abd291a96404cb38cd99babe1184b43eb2479a65c59ebd7e0f3246845a54c5f9dc34770bc69e18ba7bc147c18516f3524c78631f16496e4492e97f74a289105d83d6cf1908eb66565fd4018986ef20862ce4a9376efcb001da75135c5c3fd93f4b9dad14d46c92f192c8e39cbd669c72f27d273a6ddcf554203761924dc1697f5e592c01dcdcc335a4f3eb89cd478907449b3401c0ae9cd0c56cc140a35dcc3dc9ad17a475ac884129d662e4525c14349d1ce507d59a34b1dbe60d1f7d8842da3f1f4604710eef661c3de5291cf33e5569e5abacf9b601d45eee68a7b6eaff0f21724e90cad6b721244d57d78da67d735a0e73deb431d6fb1b8fb755787ee51e8a20da957f97d62c408e6572f865be94aca43fa19116ac318fd2700f093011438f47eaac2e85d538042ff93ce6bc333589f6d341555d36fbba74567ef613d2242c9471f2ed7aa3e565cecd46fa2ca8f88a022c714fc04f0f69c43e74af7ad7bdec18794d9bc03e2e06773a54c3ddbfe76ee5f96845293ef8fc68653831c405edbd8594a620502cb506770a89ba999d4df0d23c40d021e7c2e7159d2f380cbeb77c5d97c9b503503e7dbbae37c42a4a2a4985cabbbc979d42aaac4651c95c41dc4b0baec7d7199e9d929836e13d3e3f8c958facb305f339992162ab0258b42dfa4a02ad3ec28d48c60909e008c5fb2b2e976942784e098313d75e8a6eec78d559aa2617c35574c7a0c5f8f7e103c77087b213d6b02c444a1fb4eab23a967ddac02f9b959675c69f3c97894929c4d5a2779444b05246da35744aab4b545eefc9478620d6e1fb584a9f79080236e005cf738228c104570c200e11b6307a2d236c6031b0f89da38ce1b401eed4dda2b78268d316115b2e9c222441d9cd8d4b559adb9f1ce4a8f2a0ed3f55034e47c778d85654ac1e5048326827f0db6103cfcce491d6bf084a9e7ee2a1446c616c87f1246fb2da22a6eaae4167814f24230aed64e342cae18742c21e4b2f2c600ce783f7ec33f6937a2224a366cd1feb53011e766d0d5459d1b83a1a9147be8ddc8fea75bbdeac18077aaa469898354ff400608e2dac32be82ff7528252c0da681d2fe78150f6020c3691b5cc6cb81285f6716acf0040b346d1b76cd61bc08377f67a8adc341ceda34585ddefeee1dfc416d05067015984e3619bcbc188d8d49ab4024c9e1b83a6c0fd91b4e835d59b3f59d97d"
        val characterData = character.hexToByteArray()

        val mockNfcA = mockk<NfcA>()
        mockkStatic(android.util.Log::class)
        every { android.util.Log.i(any<String>(), any<String>()) } answers {
            val message = it.invocation.args[1] as String
            println(message)
            1
        }

        val vbNfcHandler = object : VBNfcHandler(VBBESecrets, mockNfcA) {
            override fun readHeader() {
            }

            override fun getDeviceId(): UShort {
                return 0u
            }

            override fun getHeaderDimId(): UShort {
                TODO("Not yet implemented")
            }

            override fun setHeaderDimId(dimId: UShort) {
                TODO("Not yet implemented")
            }

            override fun getOperationCommandBytes(operation: Byte): ByteArray {
                return byteArrayOf(0)
            }
        }
        val result = vbNfcHandler.decryptData(characterData, testTagId)
        for(i in result.indices step 4) {
            val stringBuilder = StringBuilder("Page ${i/4+8}: ")
            for(j in 0..2 step 2) {
                stringBuilder.append(result.sliceArray(i+j..<i+j+2).getUInt16(byteOrder = ByteOrder.BIG_ENDIAN))
                stringBuilder.append("      ")
            }
            println(stringBuilder.toString())
        }

        for(i in result.indices step 4) {
            val stringBuilder = StringBuilder("Page ${i/4+8}: ")
            for(j in 0..<4) {
                stringBuilder.append(result[i+j].toUByte())
                stringBuilder.append("   ")
            }
            println(stringBuilder.toString())
        }
        vbNfcHandler.validateCharacterData(result)
        val nfcCharacterFactory = NfcCharacterFactory()
        val nfcCharacter = nfcCharacterFactory.buildNfcCharacterFromBytes(result, 4u)
        println(nfcCharacter)
    }
}