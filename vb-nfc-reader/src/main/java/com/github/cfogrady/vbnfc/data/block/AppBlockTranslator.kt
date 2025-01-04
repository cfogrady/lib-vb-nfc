package com.github.cfogrady.vbnfc.data.block

import com.github.cfogrady.vbnfc.data.NfcCharacter

class AppBlockTranslator<T : NfcCharacter>: BlockTranslator<T> {
    companion object {
        const val APP_RESERVED_START = 0
        const val APP_RESERVED_SIZE = 12
    }

    override val startBlock: Int = 0
    override val endBlock: Int = 0

    override fun parseBlockIntoCharacter(block: ByteArray, character: T) {
        character.appReserved1 = block.sliceArray(APP_RESERVED_START..<(APP_RESERVED_START + APP_RESERVED_SIZE))
    }

    override fun writeCharacterIntoBlocks(character: T, block: ByteArray): ByteArray {
        character.appReserved1.copyInto(block,
            APP_RESERVED_START, 0,
            APP_RESERVED_SIZE
        )
        return block
    }
}