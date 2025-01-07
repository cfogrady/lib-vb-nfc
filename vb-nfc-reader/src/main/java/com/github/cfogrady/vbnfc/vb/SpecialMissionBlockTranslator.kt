package com.github.cfogrady.vbnfc.vb

import com.github.cfogrady.vbnfc.data.block.BlockTranslator
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

class SpecialMissionBlockTranslator: BlockTranslator<VBNfcCharacter> {
    override val startBlock = 16
    override val endBlock = 19

    override fun parseBlockIntoCharacter(block: ByteArray, character: VBNfcCharacter) {
        for (i in 0..(endBlock-startBlock)) {
            character.specialMissions[i] = specialMission(block, i*16)
        }
    }

    private fun specialMission(blocks: ByteArray, startIdx: Int): SpecialMission {
        return SpecialMission(
            status = SpecialMission.Status.entries[blocks[0+startIdx].toInt()],
            type = SpecialMission.Type.entries[blocks[4+startIdx].toInt()],
            id = blocks.getUInt16(5+startIdx, ByteOrder.BIG_ENDIAN),
            goal = blocks.getUInt16(7+startIdx, ByteOrder.BIG_ENDIAN),
            progress = blocks.getUInt16(9+startIdx, ByteOrder.BIG_ENDIAN),
            timeLimitInMinutes = blocks.getUInt16(11+startIdx, ByteOrder.BIG_ENDIAN),
            timeElapsedInMinutes = blocks.getUInt16(13+startIdx, ByteOrder.BIG_ENDIAN),
        )
    }

    override fun writeCharacterIntoBlocks(character: VBNfcCharacter, block: ByteArray): ByteArray {
        for(i in character.specialMissions.indices) {
            val blockIdx = i*16
            val specialMission = character.specialMissions[i]
            block[blockIdx+0] = specialMission.status.ordinal.toByte()
            block[blockIdx+4] = specialMission.type.ordinal.toByte()
            specialMission.id.toByteArray(block, blockIdx+5, ByteOrder.BIG_ENDIAN)
            specialMission.goal.toByteArray(block, blockIdx+7, ByteOrder.BIG_ENDIAN)
            specialMission.progress.toByteArray(block, blockIdx+9, ByteOrder.BIG_ENDIAN)
            specialMission.timeLimitInMinutes.toByteArray(block, blockIdx+11, ByteOrder.BIG_ENDIAN)
            specialMission.timeElapsedInMinutes.toByteArray(block, blockIdx+13, ByteOrder.BIG_ENDIAN)
        }
        return block
    }
}