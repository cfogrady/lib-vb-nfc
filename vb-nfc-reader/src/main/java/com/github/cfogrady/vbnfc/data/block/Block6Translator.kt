package com.github.cfogrady.vbnfc.data.block

import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

open class Block6Translator<T: NfcCharacter> : BlockTranslator<T> {
    companion object {
        const val CURRENT_BATTLES_WON_IDX = 2
        const val CURRENT_BATTLES_LOST_IDX = 4
        const val TOTAL_BATTLES_WON_IDX = 6
        const val TOTAL_BATTLES_LOST_IDX = 8
        const val WIN_PCT_IDX = 10 // unused
    }

    override fun parseBlockIntoCharacter(block: ByteArray, character: T) {
        character.currentPhaseBattlesWon = block.getUInt16(CURRENT_BATTLES_WON_IDX, ByteOrder.BIG_ENDIAN)
        character.currentPhaseBattlesLost = block.getUInt16(CURRENT_BATTLES_LOST_IDX, ByteOrder.BIG_ENDIAN)
        character.totalBattlesWon = block.getUInt16(TOTAL_BATTLES_WON_IDX, ByteOrder.BIG_ENDIAN)
        character.totalBattlesLost = block.getUInt16(TOTAL_BATTLES_LOST_IDX, ByteOrder.BIG_ENDIAN)
    }

    override fun writeCharacterIntoBlocks(character: T, block: ByteArray): ByteArray {
        character.currentPhaseBattlesWon.toByteArray(block,
            CURRENT_BATTLES_WON_IDX, ByteOrder.BIG_ENDIAN)
        character.currentPhaseBattlesLost.toByteArray(block,
            CURRENT_BATTLES_LOST_IDX, ByteOrder.BIG_ENDIAN)
        character.totalBattlesWon.toByteArray(block,
            TOTAL_BATTLES_WON_IDX, ByteOrder.BIG_ENDIAN)
        character.totalBattlesLost.toByteArray(block,
            TOTAL_BATTLES_LOST_IDX, ByteOrder.BIG_ENDIAN)
        block[WIN_PCT_IDX] = character.getWinPercentage()
        return block
    }
}