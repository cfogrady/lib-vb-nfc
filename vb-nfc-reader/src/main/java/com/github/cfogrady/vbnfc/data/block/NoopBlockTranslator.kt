package com.github.cfogrady.vbnfc.data.block

import com.github.cfogrady.vbnfc.data.NfcCharacter

class NoopBlockTranslator<T: NfcCharacter> : BlockTranslator<T> {
    override fun parseBlockIntoCharacter(block: ByteArray, character: T) {
    }

    override fun writeCharacterIntoBlocks(character: T, block: ByteArray): ByteArray {
        return block
    }
}