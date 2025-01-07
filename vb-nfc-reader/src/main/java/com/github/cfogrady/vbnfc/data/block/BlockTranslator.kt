package com.github.cfogrady.vbnfc.data.block

import com.github.cfogrady.vbnfc.data.NfcCharacter

// This interface is meant to translate to and from data blocks. In this context a block is defined
// as every 4 pages of the raw nfc data.
interface BlockTranslator<T: NfcCharacter> {
    fun parseBlockIntoCharacter(block: ByteArray, character: T)
    fun writeCharacterIntoBlocks(character: T, block: ByteArray): ByteArray

    val startBlock: Int
    val endBlock: Int
}