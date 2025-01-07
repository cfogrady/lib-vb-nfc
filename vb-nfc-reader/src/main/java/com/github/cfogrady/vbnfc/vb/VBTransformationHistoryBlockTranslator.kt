package com.github.cfogrady.vbnfc.vb

import android.util.Log
import com.github.cfogrady.vbnfc.FormatPagedBytes
import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.data.block.BlockTranslator
import com.github.cfogrady.vbnfc.data.convertFromDecToHex
import com.github.cfogrady.vbnfc.data.convertFromHexToDec

class VBTransformationHistoryBlockTranslator : BlockTranslator<VBNfcCharacter> {
    override val startBlock: Int = 13
    override val endBlock: Int = 15

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun parseBlockIntoCharacter(block: ByteArray, character: VBNfcCharacter) {
        Log.i("VBTransformationHistoryBlockTranslator", FormatPagedBytes(block))
        val charIndices = UByteArray(9)
        val month = UByteArray(9)
        val year = UShortArray(9)
        val day = UByteArray(9)
        // block 1
        for(i in 0..5) {
            charIndices[i] = block[(i*2)+1].toUByte()
        }
        year[0] = (block[12].convertFromHexToDec()+2000u).toUShort()
        month[0] = block[13].convertFromHexToDec()
        day[0] = block[14].convertFromHexToDec()

        // block 2
        for(i in 1..5) {
            if(charIndices[i] == UByte.MAX_VALUE) {
                year[i] = UShort.MAX_VALUE
                month[i] = UByte.MAX_VALUE
                day[i] = UByte.MAX_VALUE
            } else {
                val blockIdx = (i-1)*3+16
                year[i] = (block[blockIdx].convertFromHexToDec()+2000u).toUShort()
                month[i] = block[blockIdx+1].convertFromHexToDec()
                day[i] = block[blockIdx+2].convertFromHexToDec()
            }
        }

        // block 3
        for(i in 6..8) {
            val blockIdx = ((i-6)*2)+1+32
            charIndices[i] = block[blockIdx].toUByte()
            if(charIndices[i] == UByte.MAX_VALUE) {
                year[i] = UShort.MAX_VALUE
                month[i] = UByte.MAX_VALUE
                day[i] = UByte.MAX_VALUE
            } else {
                val dateBlockIdx = (i-6)*3+38
                year[i] = (block[dateBlockIdx].convertFromHexToDec() + 2000u).toUShort()
                month[i] = block[dateBlockIdx+1].convertFromHexToDec()
                day[i] = block[dateBlockIdx+2].convertFromHexToDec()
            }
        }

        character.transformationHistory = Array(9) {
            NfcCharacter.Transformation(charIndices[it], year[it], month[it], day[it])
        }
    }

    override fun writeCharacterIntoBlocks(character: VBNfcCharacter, block: ByteArray): ByteArray {
        // block 1
        for(i in 0..5) {
            block[i*2+1] = character.transformationHistory[i].toCharIndex.toByte()
        }
        block[12] = (character.transformationHistory[0].year-2000u).toUByte().convertFromDecToHex()
        block[13] = character.transformationHistory[0].month.convertFromDecToHex()
        block[14] = character.transformationHistory[0].day.convertFromDecToHex()

        // block 2
        for(i in 1..5) {
            val blockIdx = (i-1)*3+16
            if(character.transformationHistory[i].toCharIndex == UByte.MAX_VALUE) {
                block[blockIdx] = -1
                block[blockIdx+1] = -1
                block[blockIdx+2] = -1
            } else {
                block[blockIdx] = (character.transformationHistory[i].year-2000u).toUByte().convertFromDecToHex()
                block[blockIdx+1] = character.transformationHistory[i].month.convertFromDecToHex()
                block[blockIdx+2] = character.transformationHistory[i].day.convertFromDecToHex()
            }
        }

        // block 3
        for(i in 6..8) {
            val blockIdx = ((i-6)*2)+1+32
            val dateBlockIdx = (i-6)*3+38
            block[blockIdx] = character.transformationHistory[i].toCharIndex.toByte()
            if(character.transformationHistory[i].toCharIndex == UByte.MAX_VALUE) {
                block[dateBlockIdx] = -1
                block[dateBlockIdx+1] = -1
                block[dateBlockIdx+2] = -1
            } else {
                block[dateBlockIdx] = (character.transformationHistory[i].year - 2000u).toUByte().convertFromDecToHex()
                block[dateBlockIdx+1] = character.transformationHistory[i].month.convertFromDecToHex()
                block[dateBlockIdx+2] = character.transformationHistory[i].day.convertFromDecToHex()
            }
        }

        return block
    }
}