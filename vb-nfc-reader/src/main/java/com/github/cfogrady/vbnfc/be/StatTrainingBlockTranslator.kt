package com.github.cfogrady.vbnfc.be

import com.github.cfogrady.vbnfc.data.block.BlockTranslator
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

class StatTrainingBlockTranslator: BlockTranslator<BENfcCharacter> {
    companion object {
        const val TRAINING_HP_IDX = 0
        const val TRAINING_AP_IDX = 2
        const val TRAINING_BP_IDX = 4
        const val TRAINING_TIME_IDX = 10
    }

    override fun parseBlockIntoCharacter(block: ByteArray, character: BENfcCharacter) {
        character.trainingHp = block.getUInt16(TRAINING_HP_IDX, ByteOrder.BIG_ENDIAN)
        character.trainingAp = block.getUInt16(TRAINING_AP_IDX, ByteOrder.BIG_ENDIAN)
        character.trainingBp = block.getUInt16(TRAINING_BP_IDX, ByteOrder.BIG_ENDIAN)
        character.remainingTrainingTimeInMinutes = block.getUInt16(TRAINING_TIME_IDX, ByteOrder.BIG_ENDIAN)
    }

    override fun writeCharacterIntoBlocks(character: BENfcCharacter, block: ByteArray): ByteArray {
        character.trainingHp.toByteArray(block, TRAINING_HP_IDX, ByteOrder.BIG_ENDIAN)
        character.trainingAp.toByteArray(block, TRAINING_AP_IDX, ByteOrder.BIG_ENDIAN)
        character.trainingBp.toByteArray(block, TRAINING_BP_IDX, ByteOrder.BIG_ENDIAN)
        character.remainingTrainingTimeInMinutes.toByteArray(block,
            TRAINING_TIME_IDX, ByteOrder.BIG_ENDIAN)
        return block
    }
}