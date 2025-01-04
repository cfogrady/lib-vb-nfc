package com.github.cfogrady.vbnfc.vb

import com.github.cfogrady.vbnfc.CryptographicTransformer
import com.github.cfogrady.vbnfc.data.NfcCharacter
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.mockkStatic
import org.junit.Assert
import org.junit.Test

class VBNfcDataTranslatorTest {

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
        Assert.assertEquals(expectedCharacter, character)
    }
}