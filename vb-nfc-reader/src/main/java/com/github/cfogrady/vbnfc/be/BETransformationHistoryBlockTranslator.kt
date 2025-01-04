package com.github.cfogrady.vbnfc.be

import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.data.block.BlockTranslator

class BETransformationHistoryBlockTranslator(private val transformationBlock: Int, private val blockHistorySize: Int, private val startTransformationIndex: Int = transformationBlock * 3):
    BlockTranslator<BENfcCharacter> {

    override val startBlock: Int = 13+transformationBlock
    override val endBlock: Int = 13+transformationBlock

    override fun parseBlockIntoCharacter(block: ByteArray, character: BENfcCharacter) {
        for(i in 0..<blockHistorySize) {
            character.transformationHistory[startTransformationIndex + i] = NfcCharacter.Transformation(
                toCharIndex = block[i*4],
                yearsSince1988 = block[i*4+1],
                month = block[i*4+2],
                day = block[i*4+3],
            )
        }
    }

    override fun writeCharacterIntoBlocks(character: BENfcCharacter, block: ByteArray): ByteArray {
        for(i in 0..<blockHistorySize) {
            val transformation = character.transformationHistory[startTransformationIndex + i]
            block[i*4] = transformation.toCharIndex
            block[i*4+1] = transformation.yearsSince1988
            block[i*4+2] = transformation.month
            block[i*4+3] = transformation.day
        }
        return block
    }
}