package com.github.cfogrady.vbnfc.vb

import com.github.cfogrady.vbnfc.FormatPagedBytes
import com.github.cfogrady.vbnfc.data.block.BlockTranslator
import com.github.cfogrady.vbnfc.data.convertFromDecToHex
import com.github.cfogrady.vbnfc.data.convertFromHexToDec
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder
import java.time.LocalDate

class VitalsHistoryBlockTranslator : BlockTranslator<VBNfcCharacter> {
    override val startBlock: Int = 10
    override val endBlock: Int = 12

    override fun parseBlockIntoCharacter(block: ByteArray, character: VBNfcCharacter) {
        println(FormatPagedBytes(block))

        // block 10
        val vitals = Array<UShort>(7) {0u}
        val years = ByteArray(7) {0}
        val months = ByteArray(7) {0}
        val days = ByteArray(7) {0}
        vitals[0] = block.getUInt16(0)
        years[0] = block[2]
        months[0] = block[3]
        days[0] = block[4]

        // block 11
        for(i in 1..6) {
            val blockIdx = (i-1)*2 + 16
            vitals[i] = block.getUInt16(blockIdx)
        }
        years[1] = block[12]
        months[1] = block[13]
        days[1] = block[14]

        // block 12
        for( i in 2..6) {
            val blockIdx = (i-2)*3 + 32
            years[i] = block[blockIdx]
            months[i] = block[blockIdx+1]
            days[i] = block[blockIdx+2]
        }

        for(i in 0..6) {
            val date = if(years[i] == 0.toByte()) LocalDate.MIN else LocalDate.of(
                (years[i].convertFromHexToDec()+2000u).toInt(),
                months[i].convertFromHexToDec().toInt(),
                days[i].convertFromHexToDec().toInt()
            )
            character.vitalHistory[i] = VBNfcCharacter.DailyVitals(vitals[i], date)
        }
    }

    override fun writeCharacterIntoBlocks(character: VBNfcCharacter, block: ByteArray): ByteArray {
        // block 10
        character.vitalHistory[0].vitalsGained.toByteArray(block, 0, ByteOrder.BIG_ENDIAN)
        if( character.vitalHistory[0].date == LocalDate.MIN) {
            block[2] = 0
            block[3] = 0
            block[4] = 0
        } else {
            block[2] = (character.vitalHistory[0].date.year-2000).toUByte().convertFromDecToHex()
            block[3] = character.vitalHistory[0].date.month.value.toUByte().convertFromDecToHex()
            block[4] = character.vitalHistory[0].date.dayOfMonth.toUByte().convertFromDecToHex()
        }

        // block 11
        for (i in 1..6) {
            val blockIdx = (i-1)*2+16
            character.vitalHistory[i].vitalsGained.toByteArray(block, blockIdx, ByteOrder.BIG_ENDIAN)
        }
        if( character.vitalHistory[1].date == LocalDate.MIN) {
            block[12] = 0
            block[13] = 0
            block[14] = 0
        } else {
            block[12] = (character.vitalHistory[1].date.year-2000).toUByte().convertFromDecToHex()
            block[13] = character.vitalHistory[1].date.month.value.toUByte().convertFromDecToHex()
            block[14] = character.vitalHistory[1].date.dayOfMonth.toUByte().convertFromDecToHex()
        }

        // block 12
        for (i in 2..6) {
            val blockIdx = (i-2)*3 + 32
            if(character.vitalHistory[i].date == LocalDate.MIN) {
                block[blockIdx] = 0
                block[blockIdx+1] = 0
                block[blockIdx+2] = 0
            } else {
                block[blockIdx] = (character.vitalHistory[i].date.year-2000).toUByte().convertFromDecToHex()
                block[blockIdx+1] = character.vitalHistory[i].date.month.value.toUByte().convertFromDecToHex()
                block[blockIdx+2] = character.vitalHistory[i].date.dayOfMonth.toUByte().convertFromDecToHex()
            }
        }

        return block
    }
}