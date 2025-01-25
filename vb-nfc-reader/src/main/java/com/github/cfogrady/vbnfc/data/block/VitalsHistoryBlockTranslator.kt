package com.github.cfogrady.vbnfc.data.block

import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.data.convertFromDecToHex
import com.github.cfogrady.vbnfc.data.convertFromHexToDec
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

class VitalsHistoryBlockTranslator<T : NfcCharacter> : BlockTranslator<T> {
    override val startBlock: Int = 10
    override val endBlock: Int = 12

    override fun parseBlockIntoCharacter(block: ByteArray, character: T) {
        // block 10
        // Log.i("VitalHistoryBlockTranslator", FormatPagedBytes(block))
        val vitals = Array<UShort>(7) {0u}
        val years = ByteArray(7) {0}
        val months = ByteArray(7) {0}
        val days = ByteArray(7) {0}
        vitals[0] = block.getUInt16(0, ByteOrder.BIG_ENDIAN)
        years[0] = block[4]
        months[0] = block[5]
        days[0] = block[6]

        // block 11
        for(i in 1..6) {
            val blockIdx = (i-1)*2 + 16
            vitals[i] = block.getUInt16(blockIdx, ByteOrder.BIG_ENDIAN)
        }
        years[1] = block[12+16]
        months[1] = block[13+16]
        days[1] = block[14+16]

        // block 12
        for( i in 2..6) {
            val blockIdx = (i-2)*3 + 32
            years[i] = block[blockIdx]
            months[i] = block[blockIdx+1]
            days[i] = block[blockIdx+2]
        }

        for(i in 0..6) {
            var year = if(years[i] > 0) years[i].convertFromHexToDec().toUInt() else 0u
            // there are cases where the value could be greater than 0, but it creates an invalid year value, so convertFromHexToDex yields 0 still.
            if(year > 0u) {
                year += 2000u
            }
            val dailyVital = NfcCharacter.DailyVitals(
                vitals[i],
                year.toUShort(),
                months[i].convertFromHexToDec(),
                days[i].convertFromHexToDec(),
            )
            character.vitalHistory[i] = dailyVital
        }
    }

    override fun writeCharacterIntoBlocks(character: T, block: ByteArray): ByteArray {
        character.validateVitalHistory()
        // block 10
        character.vitalHistory[0].vitalsGained.toByteArray(block, 0, ByteOrder.BIG_ENDIAN)
        block[4] = if (character.vitalHistory[0].year > 2000u) (character.vitalHistory[0].year-2000u).toUByte().convertFromDecToHex() else 0
        block[5] = character.vitalHistory[0].month.convertFromDecToHex()
        block[6] = character.vitalHistory[0].day.convertFromDecToHex()

        // block 11
        for (i in 1..6) {
            val blockIdx = (i-1)*2+16
            character.vitalHistory[i].vitalsGained.toByteArray(block, blockIdx, ByteOrder.BIG_ENDIAN)
        }
        block[12+16] = if (character.vitalHistory[1].year > 2000u) (character.vitalHistory[1].year-2000u).toUByte().convertFromDecToHex() else 0
        block[13+16] = character.vitalHistory[1].month.convertFromDecToHex()
        block[14+16] = character.vitalHistory[1].day.convertFromDecToHex()

        // block 12
        for (i in 2..6) {
            val blockIdx = (i-2)*3 + 32
            block[blockIdx] = if (character.vitalHistory[i].year > 2000u) (character.vitalHistory[i].year-2000u).toUByte().convertFromDecToHex() else 0
            block[blockIdx+1] = character.vitalHistory[i].month.convertFromDecToHex()
            block[blockIdx+2] = character.vitalHistory[i].day.convertFromDecToHex()
        }
        return block
    }
}