package com.github.cfogrady.vbnfc.vb

import com.github.cfogrady.vbnfc.CryptographicTransformer
import com.github.cfogrady.vbnfc.data.NfcCharacter
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test

class VBNfcDataTranslatorTest {

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        val BabySlot0_2021_01_06 = "000000000004745700000000240000f3000000000004745700000000240000f3014000700401000000000000000000b6014000700401000000000000000000b600000000000000000000000d0000000d00000000000000000000000d0000000d000100000000000000000000000000010001000000000000000000000000000101320100000000000000000000003b6f01320100000000000000000000003b6f000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ff00ff00ff00ff00ff21010623fffffffffffffffffffffffffffffff100ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ff00ff00ff00ff00ff22121645fffffffffffffffffffffffffffffff100ff00ff00fffffffffffffffffffff40000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000721010600000000042501035c0000000721010600000000042501035c00000300003e00000400000b0000005000000300003e00000400000b0000005000000024122024121124121024120922000000241220241211241210241209222402222402210000000000000000008f2402222402210000000000000000008f".hexToByteArray()
        @OptIn(ExperimentalStdlibApi::class)
        val BabySlot1_2022_02_13 = "000000000004745700000000240000f3000000000004745700000000240000f3014000700401000000000000000000b6014000700401000000000000000000b600000000000000000001000d0100000f00000000000000000001000d0100000f0001000000000000000000000000000100010000000000000000000000000001015201c000c500000000000000003b14015201c000c500000000000000003b140000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000100ff00ff00ff00ff21010625220213ffffffffffffffffffffffff2b00ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ff00ff00ff00ff00ff22121645fffffffffffffffffffffffffffffff100ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000452202130000000004250103a9000000452202130000000004250103a900000300003e00000400000b0000005000000300003e00000400000b0000005000000024122024121124121024120922000000241220241211241210241209222402222402210000000000000000008f2402222402210000000000000000008f".hexToByteArray()
        @OptIn(ExperimentalStdlibApi::class)
        val ChildSlot2_2023_03_25 = "000000000004745700000000240000f3000000000004745700000000240000f3014000700401000000000000000000b6014000700401000000000000000000b600000000000000000002000d0202001300000000000000000002000d02020013000100000000000000000000000000010001000000000000000000000000000101640141017500000000000000003b5801640141017500000000000000003b5800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001000200ff00ff00ff21010628220213230325ffffffffffffffffff7900ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ff00ff00ff00ff00ff22121645fffffffffffffffffffffffffffffff100ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000682303250000000004250103e0000000682303250000000004250103e000000300003e00000400000b0000005000000300003e00000400000b0000005000000024122024121124121024120922000000241220241211241210241209222402222402210000000000000000008f2402222402210000000000000000008f".hexToByteArray()
        @OptIn(ExperimentalStdlibApi::class)
        val AdultSlot3_2023_03_26 = "000000000004745700000000240000f3000000000004745700000000240000f3014000700401000000000000000000b6014000700401000000000000000000b600000000000000000003000d0303001600000000000000000003000d0303001600010000000000000000000000000001000100000000000000000000000000010132013a000000000000000000003ba90132013a000000000000000000003ba9000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010002000300ff00ff2101062c220213230325230326ffffffffffffc800ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ff00ff00ff00ff00ff22121645fffffffffffffffffffffffffffffff100ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000772303260000000004250103f0000000772303260000000004250103f000000300003e00000400000b0000005000000300003e00000400000b0000005000000024122024121124121024120922000000241220241211241210241209222402222402210000000000000000008f2402222402210000000000000000008f".hexToByteArray()
        @OptIn(ExperimentalStdlibApi::class)
        val PerfectSlot7_2023_04_26 = "".hexToByteArray()
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testParsing() {
        mockkStatic(android.util.Log::class)
        every { android.util.Log.i(any<String>(), any<String>()) } answers {
            val message = it.invocation.args[1] as String
            println(message)
            1
        }

        val testRaw = "000000000004745700000000240000f3000000000004745700000000240000f3014000700401000000000000000000b6014000700401000000000000000000b60000000000000000000200040204202c0000000000000000000200040204202c0002000000000000000000000000000200020000000000000000000000000002010000500000000000000000000000510100005000000000000000000000005100000000241220000000000000000056000000000000000000000000241211472412102412092401262401250000001a0000000100ff00ff00ff00ff24012648240127ffffffffffffffffffffffff4000ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ff00ff00ff00ff00ff22121645fffffffffffffffffffffffffffffff100ff00ff00fffffffffffffffffffff4000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002501030000000003241220820000000025010300000000032412208200003e00000400000b0000000000004d00003e00000400000b0000000000004d00000024121124121024120924022214000000241211241210241209240222142402212402200000000000000000008d2402212402200000000000000000008d".hexToByteArray()
        val expectedCharacter = VBNfcCharacter(
            dimId=4u,
            charIndex=2u,
            stage=2,
            attribute=NfcCharacter.Attribute.Free,
            ageInDays=32,
            nextAdventureMissionStage=1,
            mood=0,
            vitalPoints=0u,
            itemEffectMentalStateValue=0,
            itemEffectMentalStateMinutesRemaining=0,
            itemEffectActivityLevelValue=0,
            itemEffectActivityLevelMinutesRemaining=0,
            itemEffectVitalPointsChangeValue=0,
            itemEffectVitalPointsChangeMinutesRemaining=0,
            transformationCountdownInMinutes = 0u,
            injuryStatus=NfcCharacter.InjuryStatus.None,
            trophies=0u,
            currentPhaseBattlesWon=0u,
            currentPhaseBattlesLost=0u,
            totalBattlesWon=0u,
            totalBattlesLost=0u,
            activityLevel=0,
            heartRateCurrent=80u,
            transformationHistory= arrayOf(
                NfcCharacter.Transformation(
                    toCharIndex = 0,
                    yearsSince1988 = 0,
                    month = 0,
                    day = 1),
                NfcCharacter.Transformation(
                    toCharIndex = 0,
                    yearsSince1988 = -1,
                    month = 0,
                    day = -1
                ),
                NfcCharacter.Transformation(
                    toCharIndex = 0,
                    yearsSince1988 = -1,
                    month = 0,
                    day = -1
                ),
                NfcCharacter.Transformation(
                    toCharIndex = 36,
                    yearsSince1988 = 1,
                    month = 39,
                    day = -1
                ),
                NfcCharacter.Transformation(
                    toCharIndex = -1,
                    yearsSince1988 = -1,
                    month = -1,
                    day = -1
                ),
                NfcCharacter.Transformation(
                    toCharIndex = -1,
                    yearsSince1988 = -1,
                    month = -1,
                    day = -1
                ),
                NfcCharacter.Transformation(
                    toCharIndex = 0,
                    yearsSince1988 = -1,
                    month = 0,
                    day = -1
                ),
                NfcCharacter.Transformation(
                    toCharIndex = 0,
                    yearsSince1988 = -1,
                    month = -1,
                    day = -1
                )),
            appReserved1= byteArrayOf(0, 0, 0, 0, 0, 4, 116, 87, 0, 0, 0, 0),
            appReserved2= arrayOf(0u, 0u, 0u),
            generation=2u,
            totalTrophies=0u,
        )

        val mockCryptographicTransformer = mockkClass(CryptographicTransformer::class)
        val translator = VBNfcDataTranslator(mockCryptographicTransformer)
        val character = translator.parseNfcCharacter(testRaw)
        println("Baby 0 Parse")
        translator.parseNfcCharacter(BabySlot0_2021_01_06)
        println("Baby 1 Parse")
        translator.parseNfcCharacter(BabySlot1_2022_02_13)
        println("Child Parse")
        translator.parseNfcCharacter(ChildSlot2_2023_03_25)
        println("Adult Parse")
        translator.parseNfcCharacter(AdultSlot3_2023_03_26)
        Assert.assertEquals(expectedCharacter, character)
    }
}