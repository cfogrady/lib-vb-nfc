package com.github.cfogrady.vbnfc.data.block

import com.github.cfogrady.vbnfc.copyIntoUShortArray
import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

class Block4Translator<T : NfcCharacter> : BlockTranslator<T> {
    companion object {
        const val INJURY_STATUS_IDX = 0
        const val APP_RESERVED_2_START = 2
        const val APP_RESERVED_2_SIZE = 3 //3 ushorts
        const val CHARACTER_INDEX_IDX = 8
        const val DIM_ID_IDX = 10
        const val STAGE_IDX = 12
        const val ATTRIBUTE_IDX = 13
        const val AGE_IN_DAYS_IDX = 14 // always 0 on BE :(
    }

    override fun parseBlockIntoCharacter(block: ByteArray, character: T) {
        character.injuryStatus = NfcCharacter.InjuryStatus.entries[block.getUInt16(INJURY_STATUS_IDX, ByteOrder.BIG_ENDIAN).toInt()]
        character.appReserved2 = block.copyIntoUShortArray(APP_RESERVED_2_START, APP_RESERVED_2_SIZE)
        character.charIndex = block.getUInt16(CHARACTER_INDEX_IDX, ByteOrder.BIG_ENDIAN)
        // DIM is required during construction and read only because it matches otp on BE characters.
        character.stage = block[STAGE_IDX]
        character.attribute = NfcCharacter.Attribute.entries[block[ATTRIBUTE_IDX].toInt()]
        character.ageInDays = block[AGE_IN_DAYS_IDX]
    }

    override fun writeCharacterIntoBlocks(character: T, block: ByteArray): ByteArray {
        character.injuryStatus.ordinal.toUShort().toByteArray(block,
            INJURY_STATUS_IDX, ByteOrder.BIG_ENDIAN)
        for(i in 0..<APP_RESERVED_2_SIZE) {
            val index = APP_RESERVED_2_START + 2*i
            character.appReserved2[i].toByteArray(block, index, ByteOrder.BIG_ENDIAN)
        }
        character.charIndex.toByteArray(block, CHARACTER_INDEX_IDX, ByteOrder.BIG_ENDIAN)
        character.dimId.toByteArray(block, DIM_ID_IDX, ByteOrder.BIG_ENDIAN)
        block[STAGE_IDX] = character.stage
        block[ATTRIBUTE_IDX] = character.attribute.ordinal.toByte()
        block[AGE_IN_DAYS_IDX] = character.ageInDays
        return block
    }
}