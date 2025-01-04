package com.github.cfogrady.vbnfc.vb

import com.github.cfogrady.vbnfc.data.block.TransformationRequirementsBlockTranslator
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

class VBTransformationRequirementsBlockTranslator : TransformationRequirementsBlockTranslator<VBNfcCharacter>() {
    companion object {
        const val GENERATIONS_IDX = 0
        const val TROPHIES_IDX = 11
        const val TOTAL_TROPHIES_IDX = 13
    }

    override fun parseBlockIntoCharacter(block: ByteArray, character: VBNfcCharacter) {
        super.parseBlockIntoCharacter(block, character)
        character.generation = block.getUInt16(GENERATIONS_IDX, ByteOrder.BIG_ENDIAN)
        character.trophies = block.getUInt16(TROPHIES_IDX, ByteOrder.BIG_ENDIAN)
        character.totalTrophies = block.getUInt16(TOTAL_TROPHIES_IDX, ByteOrder.BIG_ENDIAN)
    }

    override fun writeCharacterIntoBlocks(character: VBNfcCharacter, block: ByteArray): ByteArray {
        super.writeCharacterIntoBlocks(character, block)
        character.generation.toByteArray(block, GENERATIONS_IDX, ByteOrder.BIG_ENDIAN)
        character.trophies.toByteArray(block, TROPHIES_IDX, ByteOrder.BIG_ENDIAN)
        character.totalTrophies.toByteArray(block, TOTAL_TROPHIES_IDX, ByteOrder.BIG_ENDIAN)
        return block
    }
}