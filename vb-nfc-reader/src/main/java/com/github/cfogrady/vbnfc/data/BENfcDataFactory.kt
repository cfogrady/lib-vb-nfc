package com.github.cfogrady.vbnfc.data

import com.github.cfogrady.vbnfc.getUInt16
import java.nio.ByteOrder

class BENfcDataFactory {

    fun buildBENfcData(bytes: ByteArray): BENfc {

    }

    fun buildBENfcCharacter(bytes: ByteArray): BENfcCharacter {
        return BENfcCharacter(
            injuryStatus = bytes.getUInt16(64, ByteOrder.BIG_ENDIAN),
            charIndex = bytes.getUInt16(72, ByteOrder.BIG_ENDIAN),
            dimId = bytes.getUInt16(74, ByteOrder.BIG_ENDIAN),
            phase = bytes[76],
            attribute = NfcCharacter.Attribute.entries[bytes[77].toInt()],
            pp = bytes.getUInt16(96, ByteOrder.BIG_ENDIAN),
            currentPhaseBattlesWon = bytes.getUInt16(98, ByteOrder.BIG_ENDIAN),
            currentPhaseBattlesLost = bytes.getUInt16(100, ByteOrder.BIG_ENDIAN),
            totalBattlesWon = bytes.getUInt16(102, ByteOrder.BIG_ENDIAN),
            totalBattlesLost = bytes.getUInt16(104, ByteOrder.BIG_ENDIAN),
            mood = bytes[129],
            activityLevel = bytes[130],
            heartRateCurrent = bytes[131],
            vitalPoints = bytes.getUInt16(132, ByteOrder.BIG_ENDIAN),
            transformationHistory = buildTransformationHistory(bytes),
            itemEffectMentalStateValue = bytes[134],
            itemEffectMentalStateMinutesRemaining = bytes[135],
            itemEffectActivityLevelValue = bytes[136],
            itemEffectActivityLevelMinutesRemaining = bytes[137],
            itemEffectVitalPointsChangeValue = bytes[138],
            itemEffectVitalPointsChangeMinutesRemaining = bytes[139],
            transformationCountdown = bytes.getUInt16(141, ByteOrder.BIG_ENDIAN),
            // vitalPointsCurr = readUShort(160),
            trainingHp = bytes.getUInt16(256, ByteOrder.BIG_ENDIAN),
            trainingAp = bytes.getUInt16(258, ByteOrder.BIG_ENDIAN),
            trainingBp = bytes.getUInt16(260, ByteOrder.BIG_ENDIAN),
            remainingTrainingTime = bytes.getUInt16(266, ByteOrder.BIG_ENDIAN),
            abilityRarity = NfcCharacter.AbilityRarity.entries[bytes[290].toInt()],
            abilityType = bytes.getUInt16(292, ByteOrder.BIG_ENDIAN),
            abilityBranch = bytes.getUInt16(294, ByteOrder.BIG_ENDIAN),
            abilityReset = bytes[296],
            rank = bytes[288],
            itemType = bytes[299],
            itemMultiplier = bytes[300],
            itemRemainingTime = bytes[301]
        )
    }

    fun buildTransformationHistory(data: ByteArray): Array<NfcCharacter.Transformation> {
        val transformationHistory = Array<NfcCharacter.Transformation>(8) {phase ->
            var rootIdx = phase*4 + 208
            if (phase > 2) {
                rootIdx += 4 // we skip 220-223 for some reason
            }
            if (phase > 5) {
                rootIdx += 4 // we skip 236-239 for some reason
            }
            NfcCharacter.Transformation(
                toCharIndex = data[rootIdx],
                year = data[rootIdx+1],
                month = data[rootIdx+2],
                day = data[rootIdx+3]
            )
        }
        return transformationHistory
    }

    fun buildBENfcDevice(bytes: ByteArray): BENfcDevice {
        appReserved1 = readByteArray(0, 12)
        gender = readByte(12)
        registeredDims = readByteArray(32, 15)
        appReserved2 = readUShortArray(66, 3)
        unusedCurrDays = bytes[78]


        unusedWinPctg = readByte(106)
        reserved1 = readByte(107)
        saveFirmwareVersion = readUShort(108)
        advMissionStage = readByte(128)




        reserved2 = readByte(140)

        reserved3 = readUShort(162)
        year = readByte(164)
        month = readByte(165)
        day = readByte(166)
        vitalPointsHistory0 = readUShort(176)
        vitalPointsHistory1 = readUShort(178)
        vitalPointsHistory2 = readUShort(180)
        vitalPointsHistory3 = readUShort(182)
        vitalPointsHistory4 = readUShort(184)
        vitalPointsHistory5 = readUShort(186)
        year0PreviousVitalPoints = readByte(188)
        month0PreviousVitalPoints = readByte(189)
        day0PreviousVitalPoints = readByte(190)
        year1PreviousVitalPoints = readByte(192)
        month1PreviousVitalPoints = readByte(193)
        day1PreviousVitalPoints = readByte(194)
        year2PreviousVitalPoints = readByte(195)
        month2PreviousVitalPoints = readByte(196)
        day2PreviousVitalPoints = readByte(197)
        year3PreviousVitalPoints = readByte(198)
        month3PreviousVitalPoints = readByte(199)
        day3PreviousVitalPoints = readByte(200)
        year4PreviousVitalPoints = readByte(201)
        month4PreviousVitalPoints = readByte(202)
        day4PreviousVitalPoints = readByte(203)
        year5PreviousVitalPoints = readByte(204)
        month5PreviousVitalPoints = readByte(205)
        day5PreviousVitalPoints = readByte(206)

        reserved4 = readUShort(262)
        reserved5 = readByte(264)
        questionMark = readByte(265)
        reserved6 = readByte(289)
        reserved7 = readByte(291)

        reserved8 = readUShort(297)
        otp0 = readByteArray(352, 8)
        otp1 = readByteArray(368, 8)
        reserved9 = readUShortArray(376, 2)
        firmwareVersion = readUShort(380)
    }

    init {

    }

    private fun readByte(offset: Int): Byte {
        return nfcData[offset]
    }

    private fun readUShort(offset: Int): UShort {
        return (nfcData[offset].toUByte().toInt() or (nfcData[offset + 1].toUByte().toInt() shl 8)).toUShort()
    }

    private fun readByteArray(offset: Int, length: Int): ByteArray {
        return nfcData.sliceArray(offset..<offset + length)
    }

    private fun readUShortArray(offset: Int, length: Int): Array<UShort> {
        val result = Array<UShort>(length) { 0u }
        for (i in 0..<length) {
            result[i] = readUShort(offset + i * 2)
        }
        return result
    }
}