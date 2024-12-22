package com.github.cfogrady.vbnfc.data

import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class BENfcDataFactory {

    companion object {
        const val APP_RESERVED_START = 0
        const val APP_RESERVED_SIZE = 12
        const val INJURY_STATUS_IDX = 64
        const val APP_RESERVED_2_START = 66
        const val APP_RESERVED_2_SIZE = 3 //3 ushorts
        const val CHARACTER_INDEX_IDX = 72
        const val DIM_ID_IDX = 74
        const val PHASE_IDX = 76
        const val ATTRIBUTE_IDX = 77
        const val TRAINING_PP_IDX = 96
        const val CURRENT_BATTLES_WON_IDX = 98
        const val CURRENT_BATTLES_LOST_IDX = 100
        const val TOTAL_BATTLES_WON_IDX = 102
        const val TOTAL_BATTLES_LOST_IDX = 104
        const val WIN_PCT_IDX = 106 // unused
        const val MOOD_IDX = 129
        const val ACTIVITY_LEVEL_IDX = 130
        const val HEART_RATE_CURRENT_IDX = 131
        const val VITAL_POINTS_IDX = 132
        const val ITEM_EFFECT_MENTAL_STATE_VALUE_IDX = 134
        const val ITEM_EFFECT_MENTAL_STATE_MINUTES_REMAINING_IDX = 135
        const val ITEM_EFFECT_ACTIVITY_LEVEL_VALUE_IDX = 136
        const val ITEM_EFFECT_ACTIVITY_LEVEL_MINUTES_REMAINING_IDX = 137
        const val ITEM_EFFECT_VITAL_POINTS_CHANGE_VALUE_IDX = 138
        const val ITEM_EFFECT_VITAL_POINTS_CHANGE_MINUTES_REMAINING_IDX = 139
        // 140 reserved
        const val TRANSFORMATION_COUNT_DOWN_IDX = 141
        const val VITAL_POINTS_CURRENT_IDX = 160
        const val TRANSFORMATION_HISTORY_START = 208
        const val TRAINING_HP_IDX = 256
        const val TRAINING_AP_IDX = 258
        const val TRAINING_BP_IDX = 260
        const val TRAINING_TIME_IDX = 266
        const val RANK_IDX = 288
        const val ABILITY_RARITY_IDX = 290
        const val ABILITY_TYPE_IDX = 292
        const val ABILITY_BRANCH_IDX = 294
        const val ABILITY_RESET_IDX = 296
        const val ITEM_TYPE_IDX = 299
        const val ITEM_MULTIPLIER_IDX = 300
        const val ITEM_REMAINING_TIME_IDX = 301
        const val OTP_START_IDX = 352
        const val OTP_END_IDX = 359
        const val OTP2_START_IDX = 368
        const val OTP2_END_IDX = 375


    }

//    fun buildBENfcData(bytes: ByteArray): BENfc {
//
//    }

    fun buildBENfcCharacter(bytes: ByteArray): BENfcCharacter {
        return BENfcCharacter(
            appReserved1 = bytes.sliceArray(APP_RESERVED_START..<(APP_RESERVED_START + APP_RESERVED_SIZE)),
            injuryStatus = NfcCharacter.InjuryStatus.entries[bytes.getUInt16(INJURY_STATUS_IDX, ByteOrder.BIG_ENDIAN).toInt()],
            appReserved2 = readUShortArray(bytes, APP_RESERVED_2_START, APP_RESERVED_2_SIZE),
            charIndex = bytes.getUInt16(CHARACTER_INDEX_IDX, ByteOrder.BIG_ENDIAN),
            dimId = bytes.getUInt16(DIM_ID_IDX, ByteOrder.BIG_ENDIAN),
            phase = bytes[PHASE_IDX],
            attribute = NfcCharacter.Attribute.entries[bytes[ATTRIBUTE_IDX].toInt()],
            trainingPp = bytes.getUInt16(TRAINING_PP_IDX, ByteOrder.BIG_ENDIAN),
            currentPhaseBattlesWon = bytes.getUInt16(CURRENT_BATTLES_WON_IDX, ByteOrder.BIG_ENDIAN),
            currentPhaseBattlesLost = bytes.getUInt16(CURRENT_BATTLES_LOST_IDX, ByteOrder.BIG_ENDIAN),
            totalBattlesWon = bytes.getUInt16(TOTAL_BATTLES_WON_IDX, ByteOrder.BIG_ENDIAN),
            totalBattlesLost = bytes.getUInt16(TOTAL_BATTLES_LOST_IDX, ByteOrder.BIG_ENDIAN),
            mood = bytes[MOOD_IDX],
            activityLevel = bytes[ACTIVITY_LEVEL_IDX],
            heartRateCurrent = bytes[HEART_RATE_CURRENT_IDX].toUByte(),
            vitalPoints = bytes.getUInt16(VITAL_POINTS_IDX, ByteOrder.BIG_ENDIAN),
            itemEffectMentalStateValue = bytes[ITEM_EFFECT_MENTAL_STATE_VALUE_IDX],
            itemEffectMentalStateMinutesRemaining = bytes[ITEM_EFFECT_MENTAL_STATE_MINUTES_REMAINING_IDX],
            itemEffectActivityLevelValue = bytes[ITEM_EFFECT_ACTIVITY_LEVEL_VALUE_IDX],
            itemEffectActivityLevelMinutesRemaining = bytes[ITEM_EFFECT_ACTIVITY_LEVEL_MINUTES_REMAINING_IDX],
            itemEffectVitalPointsChangeValue = bytes[ITEM_EFFECT_VITAL_POINTS_CHANGE_VALUE_IDX],
            itemEffectVitalPointsChangeMinutesRemaining = bytes[ITEM_EFFECT_VITAL_POINTS_CHANGE_MINUTES_REMAINING_IDX],
            transformationCountdown = bytes.getUInt16(TRANSFORMATION_COUNT_DOWN_IDX, ByteOrder.BIG_ENDIAN),
            transformationHistory = buildTransformationHistory(bytes),
            trainingHp = bytes.getUInt16(TRAINING_HP_IDX, ByteOrder.BIG_ENDIAN),
            trainingAp = bytes.getUInt16(TRAINING_AP_IDX, ByteOrder.BIG_ENDIAN),
            trainingBp = bytes.getUInt16(TRAINING_BP_IDX, ByteOrder.BIG_ENDIAN),
            remainingTrainingTime = bytes.getUInt16(TRAINING_TIME_IDX, ByteOrder.BIG_ENDIAN),
            abilityRarity = NfcCharacter.AbilityRarity.entries[bytes[ABILITY_RARITY_IDX].toInt()],
            abilityType = bytes.getUInt16(ABILITY_TYPE_IDX, ByteOrder.BIG_ENDIAN),
            abilityBranch = bytes.getUInt16(ABILITY_BRANCH_IDX, ByteOrder.BIG_ENDIAN),
            abilityReset = bytes[ABILITY_RESET_IDX],
            rank = bytes[RANK_IDX],
            itemType = bytes[ITEM_TYPE_IDX],
            itemMultiplier = bytes[ITEM_MULTIPLIER_IDX],
            itemRemainingTime = bytes[ITEM_REMAINING_TIME_IDX],
            otp0 = bytes.sliceArray(OTP_START_IDX..OTP_END_IDX),
            otp1 = bytes.sliceArray(OTP2_START_IDX..OTP2_END_IDX),
        )
    }

    fun buildTransformationHistory(data: ByteArray): Array<NfcCharacter.Transformation> {
        val transformationHistory = Array<NfcCharacter.Transformation>(8) {phase ->
            var rootIdx = phase*4 + TRANSFORMATION_HISTORY_START
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

    private fun transformationHistoryToByteArray(transformationHistory: Array<NfcCharacter.Transformation>, bytes: ByteArray) {
        if (transformationHistory.size != 8) {
            throw IllegalArgumentException("Transformation History must be exactly size 8")
        }
        for (phase in 0..<transformationHistory.size) {
            var rootIdx = phase*4 + TRANSFORMATION_HISTORY_START
            if (phase > 2) {
                rootIdx += 4 // we skip 220-223 for some reason
            }
            if (phase > 5) {
                rootIdx += 4 // we skip 236-239 for some reason
            }
            bytes[rootIdx] = transformationHistory[phase].toCharIndex
            bytes[rootIdx+1] = transformationHistory[phase].year
            bytes[rootIdx+2] = transformationHistory[phase].month
            bytes[rootIdx+3] = transformationHistory[phase].day
        }
    }

    fun convertNfcDataToBytes(beNfc: BENfc): ByteArray {
        val characterNfc = beNfc.nfcCharacter
        val deviceNfc = beNfc.nfcDevice
        return convertCharacterAndDeviceToBytes(characterNfc, deviceNfc)
    }

    fun convertCharacterAndDeviceToBytes(character: BENfcCharacter, device: BENfcDevice): ByteArray {
        val writeBytes = ByteArray(4*(224-8))
        writeDeviceToByteArray(device, writeBytes)
        writeCharacterToByteArray(character, writeBytes)
        return writeBytes
    }

    fun writeDeviceToByteArray(device: BENfcDevice, bytes: ByteArray) {
        bytes[12] = device.gender.ordinal.toByte()

    }

    fun writeCharacterToByteArray(character: BENfcCharacter, bytes: ByteArray) {
        character.appReserved1.copyInto(bytes, APP_RESERVED_START, 0, APP_RESERVED_SIZE)
        character.injuryStatus.ordinal.toUShort().toByteArray(bytes, INJURY_STATUS_IDX, ByteOrder.BIG_ENDIAN)
        for(i in 0..<APP_RESERVED_2_SIZE) {
            val index = APP_RESERVED_2_START + 2*i
            character.appReserved2[i].toByteArray(bytes, index, ByteOrder.BIG_ENDIAN)
        }
        character.charIndex.toByteArray(bytes, CHARACTER_INDEX_IDX, ByteOrder.BIG_ENDIAN)
        character.dimId.toByteArray(bytes, DIM_ID_IDX, ByteOrder.BIG_ENDIAN)
        bytes[PHASE_IDX] = character.phase
        bytes[ATTRIBUTE_IDX] = character.attribute.ordinal.toByte()
        character.getTrainingPp().toByteArray(bytes, TRAINING_PP_IDX, ByteOrder.BIG_ENDIAN)
        character.currentPhaseBattlesWon.toByteArray(bytes, CURRENT_BATTLES_WON_IDX, ByteOrder.BIG_ENDIAN)
        character.currentPhaseBattlesLost.toByteArray(bytes, CURRENT_BATTLES_LOST_IDX, ByteOrder.BIG_ENDIAN)
        character.totalBattlesWon.toByteArray(bytes, TOTAL_BATTLES_WON_IDX, ByteOrder.BIG_ENDIAN)
        character.totalBattlesLost.toByteArray(bytes, TOTAL_BATTLES_LOST_IDX, ByteOrder.BIG_ENDIAN)
        bytes[WIN_PCT_IDX] = character.getWinPercentage()
        bytes[MOOD_IDX] = character.mood
        bytes[ACTIVITY_LEVEL_IDX] = character.activityLevel
        bytes[HEART_RATE_CURRENT_IDX] = character.heartRateCurrent.toByte()
        character.vitalPoints.toByteArray(bytes, VITAL_POINTS_IDX, ByteOrder.BIG_ENDIAN)
        bytes[ITEM_EFFECT_MENTAL_STATE_VALUE_IDX] = character.itemEffectMentalStateValue
        bytes[ITEM_EFFECT_MENTAL_STATE_MINUTES_REMAINING_IDX] = character.itemEffectMentalStateMinutesRemaining
        bytes[ITEM_EFFECT_ACTIVITY_LEVEL_VALUE_IDX] = character.itemEffectActivityLevelValue
        bytes[ITEM_EFFECT_ACTIVITY_LEVEL_MINUTES_REMAINING_IDX] = character.itemEffectActivityLevelMinutesRemaining
        bytes[ITEM_EFFECT_VITAL_POINTS_CHANGE_VALUE_IDX] = character.itemEffectVitalPointsChangeValue
        bytes[ITEM_EFFECT_VITAL_POINTS_CHANGE_MINUTES_REMAINING_IDX] = character.itemEffectVitalPointsChangeMinutesRemaining
        character.transformationCountdown.toByteArray(bytes, TRANSFORMATION_COUNT_DOWN_IDX, ByteOrder.BIG_ENDIAN)
        character.vitalPoints.toByteArray(bytes, VITAL_POINTS_CURRENT_IDX, ByteOrder.BIG_ENDIAN)
        transformationHistoryToByteArray(character.transformationHistory, bytes)
        character.trainingHp.toByteArray(bytes, TRAINING_HP_IDX, ByteOrder.BIG_ENDIAN)
        character.trainingAp.toByteArray(bytes, TRAINING_AP_IDX, ByteOrder.BIG_ENDIAN)
        character.trainingBp.toByteArray(bytes, TRAINING_BP_IDX, ByteOrder.BIG_ENDIAN)
        character.remainingTrainingTime.toByteArray(bytes, TRAINING_TIME_IDX, ByteOrder.BIG_ENDIAN)
        bytes[ABILITY_RARITY_IDX] = character.abilityRarity.ordinal.toByte()
        character.abilityType.toByteArray(bytes, ABILITY_TYPE_IDX, ByteOrder.BIG_ENDIAN)
        character.abilityBranch.toByteArray(bytes, ABILITY_BRANCH_IDX, ByteOrder.BIG_ENDIAN)
        bytes[ABILITY_RESET_IDX] = character.abilityReset
        bytes[RANK_IDX] = character.rank
        bytes[ITEM_TYPE_IDX] = character.itemType
        bytes[ITEM_MULTIPLIER_IDX] = character.itemMultiplier
        bytes[ITEM_REMAINING_TIME_IDX] = character.itemRemainingTime
        character.otp0.copyInto(bytes, OTP_START_IDX, 0, OTP_END_IDX+1)
        character.otp1.copyInto(bytes, OTP2_START_IDX, 0, OTP2_END_IDX + 1)
    }

    fun buildBENfcDevice(bytes: ByteArray): BENfcDevice {
        return BENfcDevice(
            gender = BENfcDevice.Gender.entries[bytes[12].toInt()],
            registedDims = bytes.sliceArray(32..<32+15),
            currDays = bytes[78]
        )



//        reserved1 = readByte(107)
//        saveFirmwareVersion = readUShort(108)
//        advMissionStage = readByte(128)
//
//
//
//
//        reserved2 = readByte(140)
//
//        reserved3 = readUShort(162)
//        year = readByte(164)
//        month = readByte(165)
//        day = readByte(166)
//        vitalPointsHistory0 = readUShort(176)
//        vitalPointsHistory1 = readUShort(178)
//        vitalPointsHistory2 = readUShort(180)
//        vitalPointsHistory3 = readUShort(182)
//        vitalPointsHistory4 = readUShort(184)
//        vitalPointsHistory5 = readUShort(186)
//        year0PreviousVitalPoints = readByte(188)
//        month0PreviousVitalPoints = readByte(189)
//        day0PreviousVitalPoints = readByte(190)
//        year1PreviousVitalPoints = readByte(192)
//        month1PreviousVitalPoints = readByte(193)
//        day1PreviousVitalPoints = readByte(194)
//        year2PreviousVitalPoints = readByte(195)
//        month2PreviousVitalPoints = readByte(196)
//        day2PreviousVitalPoints = readByte(197)
//        year3PreviousVitalPoints = readByte(198)
//        month3PreviousVitalPoints = readByte(199)
//        day3PreviousVitalPoints = readByte(200)
//        year4PreviousVitalPoints = readByte(201)
//        month4PreviousVitalPoints = readByte(202)
//        day4PreviousVitalPoints = readByte(203)
//        year5PreviousVitalPoints = readByte(204)
//        month5PreviousVitalPoints = readByte(205)
//        day5PreviousVitalPoints = readByte(206)
//
//        reserved4 = readUShort(262)
//        reserved5 = readByte(264)
//        questionMark = readByte(265)
//        reserved6 = readByte(289)
//        reserved7 = readByte(291)
//
//        reserved8 = readUShort(297)
//        reserved9 = readUShortArray(376, 2)
//        firmwareVersion = readUShort(380)
    }

    private fun readUShortArray(bytes: ByteArray, offset: Int, length: Int): Array<UShort> {
        val result = Array<UShort>(length) { 0u }
        for (i in 0..<length) {
            result[i] = bytes.getUInt16(offset + i * 2, ByteOrder.BIG_ENDIAN)
        }
        return result
    }
}