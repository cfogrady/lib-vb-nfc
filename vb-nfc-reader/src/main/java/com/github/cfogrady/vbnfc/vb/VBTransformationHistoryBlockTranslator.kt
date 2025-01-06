package com.github.cfogrady.vbnfc.vb

import android.util.Log
import com.github.cfogrady.vbnfc.FormatPagedBytes
import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.data.block.BlockTranslator

class VBTransformationHistoryBlockTranslator : BlockTranslator<VBNfcCharacter> {
    override val startBlock: Int = 13
    override val endBlock: Int = 15

    override fun parseBlockIntoCharacter(block: ByteArray, character: VBNfcCharacter) {
        Log.i("VBTransformationHistoryBlockTranslator", FormatPagedBytes(block))
        val charIndices = ByteArray(9)
        val month = ByteArray(9)
        val year = ByteArray(9)
        val day = ByteArray(9)
        for(i in 0..5) {
            charIndices[i] = block[(i*2)+1]
        }
        year[0] = block[12]
        month[0] = block[13]
        day[0] = block[14]

        for(i in 6..8) {
            charIndices[i] = block[i+26]
        }

        character.transformationHistory = Array<NfcCharacter.Transformation>(9) {
            NfcCharacter.Transformation(charIndices[it], year[it], month[it], day[it])
        }
    }

    override fun writeCharacterIntoBlocks(character: VBNfcCharacter, block: ByteArray): ByteArray {
        TODO("Not yet implemented")
    }
}