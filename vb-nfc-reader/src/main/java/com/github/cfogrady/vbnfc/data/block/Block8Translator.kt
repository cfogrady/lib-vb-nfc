package com.github.cfogrady.vbnfc.data.block

import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

class Block8Translator<T : NfcCharacter> : BlockTranslator<T> {
    companion object {
        const val NEXT_ADVENTURE_MISSION_STAGE_IDX = 0
        const val MOOD_IDX = 1
        const val ACTIVITY_LEVEL_IDX = 2
        const val HEART_RATE_CURRENT_IDX = 3
        const val VITAL_POINTS_IDX = 4
        const val ITEM_EFFECT_MENTAL_STATE_VALUE_IDX = 6
        const val ITEM_EFFECT_MENTAL_STATE_MINUTES_REMAINING_IDX = 7
        const val ITEM_EFFECT_ACTIVITY_LEVEL_VALUE_IDX = 8
        const val ITEM_EFFECT_ACTIVITY_LEVEL_MINUTES_REMAINING_IDX = 9
        const val ITEM_EFFECT_VITAL_POINTS_CHANGE_VALUE_IDX = 10
        const val ITEM_EFFECT_VITAL_POINTS_CHANGE_MINUTES_REMAINING_IDX = 11
        // 12 reserved
        const val TRANSFORMATION_COUNT_DOWN_IDX = 13
    }

    override fun parseBlockIntoCharacter(block: ByteArray, character: T) {
        character.nextAdventureMissionStage = block[NEXT_ADVENTURE_MISSION_STAGE_IDX]
        character.mood = block[MOOD_IDX]
        character.activityLevel = block[ACTIVITY_LEVEL_IDX]
        character.heartRateCurrent = block[HEART_RATE_CURRENT_IDX].toUByte()
        character.vitalPoints = block.getUInt16(VITAL_POINTS_IDX, ByteOrder.BIG_ENDIAN)
        character.itemEffectMentalStateValue = block[ITEM_EFFECT_MENTAL_STATE_VALUE_IDX]
        character.itemEffectMentalStateMinutesRemaining = block[ITEM_EFFECT_MENTAL_STATE_MINUTES_REMAINING_IDX]
        character.itemEffectActivityLevelValue = block[ITEM_EFFECT_ACTIVITY_LEVEL_VALUE_IDX]
        character.itemEffectActivityLevelMinutesRemaining = block[ITEM_EFFECT_ACTIVITY_LEVEL_MINUTES_REMAINING_IDX]
        character.itemEffectVitalPointsChangeValue = block[ITEM_EFFECT_VITAL_POINTS_CHANGE_VALUE_IDX]
        character.itemEffectVitalPointsChangeMinutesRemaining = block[ITEM_EFFECT_VITAL_POINTS_CHANGE_MINUTES_REMAINING_IDX]
        character.transformationCountdownInMinutes = block.getUInt16(TRANSFORMATION_COUNT_DOWN_IDX, ByteOrder.BIG_ENDIAN)
    }

    override fun writeCharacterIntoBlocks(character: T, block: ByteArray): ByteArray {
        block[NEXT_ADVENTURE_MISSION_STAGE_IDX] = character.nextAdventureMissionStage
        block[MOOD_IDX] = character.mood
        block[ACTIVITY_LEVEL_IDX] = character.activityLevel
        block[HEART_RATE_CURRENT_IDX] = character.heartRateCurrent.toByte()
        character.vitalPoints.toByteArray(block, VITAL_POINTS_IDX, ByteOrder.BIG_ENDIAN)
        block[ITEM_EFFECT_MENTAL_STATE_VALUE_IDX] = character.itemEffectMentalStateValue
        block[ITEM_EFFECT_MENTAL_STATE_MINUTES_REMAINING_IDX] = character.itemEffectMentalStateMinutesRemaining
        block[ITEM_EFFECT_ACTIVITY_LEVEL_VALUE_IDX] = character.itemEffectActivityLevelValue
        block[ITEM_EFFECT_ACTIVITY_LEVEL_MINUTES_REMAINING_IDX] = character.itemEffectActivityLevelMinutesRemaining
        block[ITEM_EFFECT_VITAL_POINTS_CHANGE_VALUE_IDX] = character.itemEffectVitalPointsChangeValue
        block[ITEM_EFFECT_VITAL_POINTS_CHANGE_MINUTES_REMAINING_IDX] = character.itemEffectVitalPointsChangeMinutesRemaining
        character.transformationCountdownInMinutes.toByteArray(block,
            TRANSFORMATION_COUNT_DOWN_IDX, ByteOrder.BIG_ENDIAN)
        return block
    }
}