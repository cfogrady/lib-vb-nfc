package com.github.cfogrady.vbnfc.be

import android.util.Log
import com.github.cfogrady.vbnfc.CryptographicTransformer
import com.github.cfogrady.vbnfc.TranslatorTestUtils
import com.github.cfogrady.vbnfc.data.NfcCharacter
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test

class BENfcDataTranslatorTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun mockLogging(): Unit {
            mockkStatic(Log::class)
            every { Log.i(any<String>(), any<String>()) } answers {
                val tag = it.invocation.args[0] as String
                val message = it.invocation.args[1] as String
                println("$tag: $message")
                1
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testNfcCharacterParsing() {
        val nfcBytes = "000000000000000000000000000000000000000000000000000000000000000010400010040010001000000000001094104000100400100010000000000010940000000000000000000500820302008c0000000000000000000500820302008c000b00030002000b000b000001010028000b00030002000b000b0000010100280464028b04b4000000000000000308b80464028b04b4000000000000000308b80474000000001500000000000000008d0002000000000000000000002401062d240105240104240103240102240101c80024010601240106042401060000008605240116ffffffffffffffff00000038ffffffffffffffff00000000000000f80005000f000a00000000046b0000008d0005000f000a00000000046b0000008d00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010101010101010100000000000000210202020202020202000000000101003a000000000000000000000000000000000000000000000000000000000000000014c5400000000000000000000000001914c540000000000000000000000000190000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".hexToByteArray()
        // println("BE Data:\n${FormatPagedBytes(nfcBytes)}")
        val mockCryptographicTransformer = mockkClass(CryptographicTransformer::class)
        val beNfcDataTranslator = BENfcDataTranslator(mockCryptographicTransformer)

        val character = beNfcDataTranslator.parseNfcCharacter(nfcBytes)
        val expectedCharacter = BENfcCharacter(
            dimId = 130u,
            charIndex = 5u,
            stage = 3,
            attribute = NfcCharacter.Attribute.Data,
            ageInDays = 0,
            mood = 100,
            characterCreationFirmwareVersion = FirmwareVersion(1, 1),
            nextAdventureMissionStage = 4,
            vitalPoints = 1204u,
            transformationCountdownInMinutes = 776u,
            injuryStatus = NfcCharacter.InjuryStatus.None,
            trainingPp = 11u,
            currentPhaseBattlesWon = 3u,
            currentPhaseBattlesLost = 2u,
            totalBattlesWon = 11u,
            totalBattlesLost = 11u,
            activityLevel = 2,
            heartRateCurrent = 139u,
            transformationHistory = arrayOf(NfcCharacter.Transformation(0u, 2024u, 1u, 6u),
                NfcCharacter.Transformation(1u, 2024u, 1u, 6u),
                NfcCharacter.Transformation(4u, 2024u, 1u, 6u),
                NfcCharacter.Transformation(5u, 2024u, 1u, 16u),
                NfcCharacter.Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE),
                NfcCharacter.Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE),
                NfcCharacter.Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE),
                NfcCharacter.Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE),
                ),
            vitalHistory = arrayOf(NfcCharacter.DailyVitals(1140u, 0u, 0u, 15u),
                NfcCharacter.DailyVitals(2u, 2024u, 1u, 6u),
                NfcCharacter.DailyVitals(0u, 2024u, 1u, 5u),
                NfcCharacter.DailyVitals(0u, 2024u, 1u, 4u),
                NfcCharacter.DailyVitals(0u, 2024u, 1u, 3u),
                NfcCharacter.DailyVitals(0u, 2024u, 1u, 2u),
                NfcCharacter.DailyVitals(0u, 2024u, 1u, 1u)),
            trainingHp = 5u,
            trainingAp = 15u,
            trainingBp = 10u,
            remainingTrainingTimeInMinutes = 1131u,
            itemEffectMentalStateValue = 0,
            itemEffectMentalStateMinutesRemaining = 0,
            itemEffectActivityLevelValue = 0,
            itemEffectActivityLevelMinutesRemaining = 0,
            itemEffectVitalPointsChangeValue = 0,
            itemEffectVitalPointsChangeMinutesRemaining = 0,
            abilityRarity = NfcCharacter.AbilityRarity.None,
            abilityType = 0u,
            abilityBranch = 0u,
            abilityReset = 0,
            rank = 0,
            itemType = 0,
            itemMultiplier = 0,
            itemRemainingTime = 0,
            appReserved1 = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            appReserved2 = arrayOf(0u, 0u, 0u),
            otp0 = "0101010101010101".hexToByteArray(),
            otp1 = "0202020202020202".hexToByteArray()
        )
        Assert.assertEquals(expectedCharacter, character)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testNfcCharacterFormatting() {
        val testCharacter = BENfcCharacter(
            dimId = 130u,
            charIndex = 5u,
            stage = 3,
            attribute = NfcCharacter.Attribute.Data,
            ageInDays = 0,
            mood = 100,
            characterCreationFirmwareVersion = FirmwareVersion(1, 1),
            nextAdventureMissionStage = 4,
            vitalPoints = 1204u,
            transformationCountdownInMinutes = 776u,
            injuryStatus = NfcCharacter.InjuryStatus.None,
            trainingPp = 11u,
            currentPhaseBattlesWon = 3u,
            currentPhaseBattlesLost = 2u,
            totalBattlesWon = 11u,
            totalBattlesLost = 11u,
            activityLevel = 2,
            heartRateCurrent = 139u,
            transformationHistory = arrayOf(NfcCharacter.Transformation(0u, 2024u, 1u, 6u),
                NfcCharacter.Transformation(1u, 2024u, 1u, 6u),
                NfcCharacter.Transformation(4u, 2024u, 1u, 6u),
                NfcCharacter.Transformation(5u, 2024u, 1u, 16u),
                NfcCharacter.Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE),
                NfcCharacter.Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE),
                NfcCharacter.Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE),
                NfcCharacter.Transformation(UByte.MAX_VALUE, UShort.MAX_VALUE, UByte.MAX_VALUE, UByte.MAX_VALUE),
            ),
            vitalHistory = arrayOf(
                NfcCharacter.DailyVitals(0u, 0u, 0u, 0u),
                NfcCharacter.DailyVitals(0u, 0u, 0u, 0u),
                NfcCharacter.DailyVitals(0u, 0u, 0u, 0u),
                NfcCharacter.DailyVitals(0u, 0u, 0u, 0u),
                NfcCharacter.DailyVitals(0u, 0u, 0u, 0u),
                NfcCharacter.DailyVitals(0u, 0u, 0u, 0u),
                NfcCharacter.DailyVitals(0u, 0u, 0u, 0u),),
            trainingHp = 5u,
            trainingAp = 15u,
            trainingBp = 10u,
            remainingTrainingTimeInMinutes = 1131u,
            itemEffectMentalStateValue = 0,
            itemEffectMentalStateMinutesRemaining = 0,
            itemEffectActivityLevelValue = 0,
            itemEffectActivityLevelMinutesRemaining = 0,
            itemEffectVitalPointsChangeValue = 0,
            itemEffectVitalPointsChangeMinutesRemaining = 0,
            abilityRarity = NfcCharacter.AbilityRarity.None,
            abilityType = 0u,
            abilityBranch = 0u,
            abilityReset = 0,
            rank = 0,
            itemType = 0,
            itemMultiplier = 0,
            itemRemainingTime = 0,
            appReserved1 = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            appReserved2 = arrayOf(0u, 0u, 0u),
            otp0 = "0101010101010101".hexToByteArray(),
            otp1 = "0202020202020202".hexToByteArray()
        )
        val mockCryptographicTransformer = mockkClass(CryptographicTransformer::class)
        val beNfcDataTranslator = BENfcDataTranslator(mockCryptographicTransformer)
        val resultArray = ByteArray(864) {
            0
        }
        // check character by itself
        beNfcDataTranslator.setCharacterInByteArray(testCharacter, resultArray)
        val expectedCharacterInByteArray = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000500820302000000000000000000000000000000000000000b00030002000b000b3c0001010000000000000000000000000000000000000464028b04b400000000000000030800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000024010601240106042401060000000005240116ffffffffffffffff00000000ffffffffffffffff00000000000000000005000f000a00000000046b0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101010101010101000000000000000002020202020202020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".hexToByteArray()
        TranslatorTestUtils.assertAllBlocksAreEqual("Data Set", expectedCharacterInByteArray, resultArray)

        // check checksum and duplicated data
        beNfcDataTranslator.finalizeByteArrayFormat(resultArray)
        val expectedFinalizedByteArray = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000500820302008c0000000000000000000500820302008c000b00030002000b000b3c0001010064000b00030002000b000b3c00010100640464028b04b4000000000000000308b80464028b04b4000000000000000308b80000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000024010601240106042401060000008605240116ffffffffffffffff00000038ffffffffffffffff00000000000000f80005000f000a00000000046b0000008d0005000f000a00000000046b0000008d000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000101010101010101000000000000000002020202020202020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".hexToByteArray()
        TranslatorTestUtils.assertAllBlocksAreEqual("Finalized Data", expectedFinalizedByteArray, resultArray)
    }
}