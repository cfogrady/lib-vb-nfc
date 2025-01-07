package com.github.cfogrady.vbnfc.be

import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.data.block.BlockTranslator
import com.github.cfogrady.vbnfc.data.convertFromDecToHex
import com.github.cfogrady.vbnfc.data.convertFromHexToDec

class BETransformationHistoryBlockTranslator(private val transformationBlock: Int, private val blockHistorySize: Int, private val startTransformationIndex: Int = transformationBlock * 3):
    BlockTranslator<BENfcCharacter> {

    override val startBlock: Int = 13+transformationBlock
    override val endBlock: Int = 13+transformationBlock

    override fun parseBlockIntoCharacter(block: ByteArray, character: BENfcCharacter) {
        for(i in 0..<blockHistorySize) {
            val toCharIndex = block[i*4].toUByte()
            character.transformationHistory[startTransformationIndex + i] = if(toCharIndex == UByte.MAX_VALUE) NfcCharacter.Transformation(
                toCharIndex = toCharIndex,
                year = UShort.MAX_VALUE,
                month = UByte.MAX_VALUE,
                day = UByte.MAX_VALUE
            ) else NfcCharacter.Transformation(
                toCharIndex = toCharIndex,
                year = (block[i*4+1].convertFromHexToDec()+2000u).toUShort(),
                month = block[i*4+2].convertFromHexToDec(),
                day = block[i*4+3].convertFromHexToDec(),
            )
        }
    }

    override fun writeCharacterIntoBlocks(character: BENfcCharacter, block: ByteArray): ByteArray {
        for(i in 0..<blockHistorySize) {
            val transformation = character.transformationHistory[startTransformationIndex + i]
            block[i*4] = transformation.toCharIndex.toByte()
            if(transformation.toCharIndex == UByte.MAX_VALUE) {
                block[i*4+1] = -1
                block[i*4+2] = -1
                block[i*4+3] = -1
            } else {
                block[i*4+1] = (transformation.year-2000u).toUByte().convertFromDecToHex()
                block[i*4+2] = transformation.month.convertFromDecToHex()
                block[i*4+3] = transformation.day.convertFromDecToHex()
            }
        }
        return block
    }
}