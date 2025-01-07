package com.github.cfogrady.vbnfc.be

import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.data.block.BlockTranslator
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

class BEAppBlockTranslator: BlockTranslator<BENfcCharacter> {
    companion object {
        const val RANK_IDX = 0
        const val ABILITY_RARITY_IDX = 2
        const val ABILITY_TYPE_IDX = 4
        const val ABILITY_BRANCH_IDX = 6
        const val ABILITY_RESET_IDX = 8
        const val ITEM_TYPE_IDX = 11
        const val ITEM_MULTIPLIER_IDX = 12
        const val ITEM_REMAINING_TIME_IDX = 13
    }

    override val startBlock: Int = 18
    override val endBlock: Int = 18

    override fun parseBlockIntoCharacter(block: ByteArray, character: BENfcCharacter) {
        character.abilityRarity = NfcCharacter.AbilityRarity.entries[block[ABILITY_RARITY_IDX].toInt()]
        character.abilityType = block.getUInt16(ABILITY_TYPE_IDX, ByteOrder.BIG_ENDIAN)
        character.abilityBranch = block.getUInt16(ABILITY_BRANCH_IDX, ByteOrder.BIG_ENDIAN)
        character.abilityReset = block[ABILITY_RESET_IDX]
        character.rank = block[RANK_IDX]
        character.itemType = block[ITEM_TYPE_IDX]
        character.itemMultiplier = block[ITEM_MULTIPLIER_IDX]
        character.itemRemainingTime = block[ITEM_REMAINING_TIME_IDX]
    }

    override fun writeCharacterIntoBlocks(character: BENfcCharacter, block: ByteArray): ByteArray {
        block[ABILITY_RARITY_IDX] = character.abilityRarity.ordinal.toByte()
        character.abilityType.toByteArray(block, ABILITY_TYPE_IDX, ByteOrder.BIG_ENDIAN)
        character.abilityBranch.toByteArray(block, ABILITY_BRANCH_IDX, ByteOrder.BIG_ENDIAN)
        block[ABILITY_RESET_IDX] = character.abilityReset
        block[RANK_IDX] = character.rank
        block[ITEM_TYPE_IDX] = character.itemType
        block[ITEM_MULTIPLIER_IDX] = character.itemMultiplier
        block[ITEM_REMAINING_TIME_IDX] = character.itemRemainingTime
        return block
    }
}