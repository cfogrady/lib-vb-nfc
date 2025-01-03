package com.github.cfogrady.vbnfc.be

import com.github.cfogrady.vbnfc.data.block.Block6Translator
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.toByteArray
import java.nio.ByteOrder

class BEBlock6Translator : Block6Translator<BENfcCharacter>() {
    companion object {
        const val TRAINING_PP_IDX = 0
        const val CHARACTER_CREATION_FIRMWARE_VERSION_IDX = 12
    }

    override fun parseBlockIntoCharacter(block: ByteArray, character: BENfcCharacter) {
        super.parseBlockIntoCharacter(block, character)
        character.setTrainingPp(block.getUInt16(TRAINING_PP_IDX, ByteOrder.BIG_ENDIAN))
        character.characterCreationFirmwareVersion = FirmwareVersion(
            majorVersion = block[CHARACTER_CREATION_FIRMWARE_VERSION_IDX],
            minorVersion = block[CHARACTER_CREATION_FIRMWARE_VERSION_IDX+1])
    }

    override fun writeCharacterIntoBlocks(character: BENfcCharacter, block: ByteArray): ByteArray {
        super.writeCharacterIntoBlocks(character, block)
        character.getTrainingPp().toByteArray(block, TRAINING_PP_IDX, ByteOrder.BIG_ENDIAN)
        block[CHARACTER_CREATION_FIRMWARE_VERSION_IDX] = character.characterCreationFirmwareVersion.majorVersion
        block[CHARACTER_CREATION_FIRMWARE_VERSION_IDX+1] = character.characterCreationFirmwareVersion.minorVersion
        return block
    }
}