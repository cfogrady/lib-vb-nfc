package com.github.cfogrady.vbnfc.data.block

import com.github.cfogrady.vbnfc.data.NfcCharacter

class TransformationBlockTranslator<T : NfcCharacter>(private val transformationBlock: Int, private val blockHistorySize: Int, private val startTransformationIndex: Int = transformationBlock * 3): BlockTranslator<T> {

    override fun parseBlockIntoCharacter(block: ByteArray, character: T) {
        for(i in 0..<blockHistorySize) {
            character.transformationHistory[startTransformationIndex + i] = NfcCharacter.Transformation(
                toCharIndex = block[i*4],
                yearsSince1988 = block[i*4+1],
                month = block[i*4+2],
                day = block[i*4+3],
            )
        }
    }

    override fun writeCharacterIntoBlocks(character: T, block: ByteArray): ByteArray {
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