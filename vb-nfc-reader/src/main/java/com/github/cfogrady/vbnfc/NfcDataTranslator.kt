package com.github.cfogrady.vbnfc

import com.github.cfogrady.vbnfc.data.NfcCharacter
import com.github.cfogrady.vbnfc.data.NfcHeader
import com.github.cfogrady.vbnfc.data.block.BlockTranslator
import com.github.cfogrady.vbnfc.data.block.CharacterTypeBlockTranslator

abstract class NfcDataTranslator<OUT_CHARACTER_TYPE : NfcCharacter>(
    val cryptographicTransformer: CryptographicTransformer,
    private val blockTranslators: Array<BlockTranslator<in OUT_CHARACTER_TYPE>>
) {


    // setCharacterInByteArray takes the NfcCharacter and modifies the byte array with character
    // data. At the time of writing this is used to write a parsed character into fresh unparsed
    // device data when sending a character back to the device.
    open fun setCharacterInByteArray(character: NfcCharacter, bytes: ByteArray) {
        // There is a problem with the factory or setup if this cast fails
        val actualCharacter = character as OUT_CHARACTER_TYPE
        for(blockTranslator in blockTranslators) {
            val blockData = getDataBlocks(blockTranslator.startBlock, blockTranslator.endBlock, bytes)
            val newBlocks = blockTranslator.writeCharacterIntoBlocks(actualCharacter, blockData)
            writeBlocks(blockTranslator.startBlock, newBlocks, bytes)
        }
        nonBlockAlignedWrites(character, bytes)
    }

    // parseNfcCharacter parses the nfc data byte array into an instance of a NfcCharacter object
    open fun parseNfcCharacter(bytes: ByteArray): OUT_CHARACTER_TYPE {
        val character = createBaseCharacter(bytes)
        for(blockTranslator in blockTranslators) {
            val blockData = getDataBlocks(blockTranslator.startBlock, blockTranslator.endBlock, bytes)
            blockTranslator.parseBlockIntoCharacter(blockData, character)
        }
        return character
    }

    open fun nonBlockAlignedWrites(character: OUT_CHARACTER_TYPE, bytes: ByteArray) {}

    // finalizeByteArrayFormat finalizes the byte array for NFC format by setting all the
    // checksums, and duplicating the duplicate memory pages.
    abstract fun finalizeByteArrayFormat(bytes: ByteArray)

    // getOperationCommandBytes gets an operation command corresponding to the existing header and
    // the input operation
    abstract fun getOperationCommandBytes(header: NfcHeader, operation: Byte): ByteArray

    abstract fun createBaseCharacter(dataBytes: ByteArray): OUT_CHARACTER_TYPE

    // parseHeader parses the nfc header byte array into an instance of NfcHeader
    abstract fun parseHeader(headerBytes: ByteArray): NfcHeader

    private fun getDataBlock(block: Int, bytes: ByteArray) : ByteArray {
        val blockStartIdx = (block)*16
        val blockEndIdx = (block+1)*16
        return bytes.sliceArray(blockStartIdx..<blockEndIdx)
    }

    private fun getDataBlocks(startBlock: Int, endBlock: Int, bytes: ByteArray) : ByteArray {
        val blockStartIdx = (startBlock)*16
        val blockEndIdx = (endBlock+1)*16
        return bytes.sliceArray(blockStartIdx..<blockEndIdx)
    }

    private fun writeBlocks(blockIdx: Int, blockBytes: ByteArray, rawData: ByteArray) {
        blockBytes.copyInto(rawData, blockIdx*16, 0)
    }

}