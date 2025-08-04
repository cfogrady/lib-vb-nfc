package com.github.cfogrady.vbnfc.vb

import com.github.cfogrady.vbnfc.ChecksumCalculator
import com.github.cfogrady.vbnfc.CryptographicTransformer
import com.github.cfogrady.vbnfc.NfcDataTranslator
import com.github.cfogrady.vbnfc.TagCommunicator
import com.github.cfogrady.vbnfc.be.BENfcDataTranslator.Companion.DIM_IDX
import com.github.cfogrady.vbnfc.data.NfcHeader
import com.github.cfogrady.vbnfc.data.block.AppBlockTranslator
import com.github.cfogrady.vbnfc.data.block.CharacterTypeBlockTranslator
import com.github.cfogrady.vbnfc.data.block.CharacterStatusBlockTranslator
import com.github.cfogrady.vbnfc.data.block.VitalsHistoryBlockTranslator
import com.github.cfogrady.vbnfc.getUInt16
import java.nio.ByteOrder

class VBNfcDataTranslator(cryptographicTransformer: CryptographicTransformer, private val checksumCalculator: ChecksumCalculator = ChecksumCalculator()) : NfcDataTranslator<VBNfcCharacter>(
    cryptographicTransformer,
    arrayOf(
        AppBlockTranslator(), // 0
        CharacterTypeBlockTranslator(), // 4
        VBTransformationRequirementsBlockTranslator(), // 6
        CharacterStatusBlockTranslator(), // 8
        VitalsHistoryBlockTranslator(), // 10-12
        VBTransformationHistoryBlockTranslator(), //13-15
        SpecialMissionBlockTranslator() // 16-19
    )
) {

    companion object {
        const val OPERATION_PAGE: Byte = 0x6
    }

    override fun finalizeByteArrayFormat(bytes: ByteArray) {
        checksumCalculator.recalculateChecksums(bytes)
        performPageBlockDuplications(bytes, BlocksWithCopiesInterweaved, 16)
        performPageBlockDuplications(bytes, SequentialBlocksWithCopyFollowing, 64)
    }

    private val BlocksWithCopiesInterweaved = intArrayOf(0, 2, 4, 6, 8, 46, 48, 50, 52)
    private val SequentialBlocksWithCopyFollowing = intArrayOf(16, 17, 18, 19)
    private fun performPageBlockDuplications(data: ByteArray, blocksToCopy: IntArray = BlocksWithCopiesInterweaved, copyOffset: Int = 16) {
        for (blockIndex in blocksToCopy) {
            val firstIndex = blockIndex*16
            for (i in firstIndex..firstIndex + 15) {
                data[i+copyOffset] = data[i]
            }
        }
    }

    override fun getOperationCommandBytes(header: NfcHeader, operation: Byte): ByteArray {
        val vbHeader = header as VBNfcHeader
        return byteArrayOf(TagCommunicator.NFC_WRITE_COMMAND, OPERATION_PAGE, header.status, header.dimIdBytes[1], operation, vbHeader.reserved)
    }

    override fun createBaseCharacter(dataBytes: ByteArray): VBNfcCharacter {
        return VBNfcCharacter(
            dimId = dataBytes.getUInt16(DIM_IDX, ByteOrder.BIG_ENDIAN),
        )
    }

    override fun parseHeader(headerBytes: ByteArray): NfcHeader {
        val header = VBNfcHeader(
            deviceType = headerBytes.getUInt16(4, byteOrder = ByteOrder.BIG_ENDIAN),
            deviceSubType = headerBytes.getUInt16(6, byteOrder = ByteOrder.BIG_ENDIAN),
            vbCompatibleTagIdentifier = headerBytes.sliceArray(0..3), // this is a magic number used to verify that the tag is a VB.
            status = headerBytes[8],
            dimId = headerBytes[9],
            operation = headerBytes[10],
            reserved = headerBytes[11],
            appFlag = headerBytes[12],
            nonce = headerBytes.sliceArray(13..15)
        )
        return header
    }
}