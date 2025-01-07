package com.github.cfogrady.vbnfc.be

import android.util.Log
import com.github.cfogrady.vbnfc.ChecksumCalculator
import com.github.cfogrady.vbnfc.CryptographicTransformer
import com.github.cfogrady.vbnfc.NfcDataTranslator
import com.github.cfogrady.vbnfc.TagCommunicator
import com.github.cfogrady.vbnfc.data.DeviceSubType
import com.github.cfogrady.vbnfc.data.DeviceType
import com.github.cfogrady.vbnfc.data.NfcHeader
import com.github.cfogrady.vbnfc.data.block.AppBlockTranslator
import com.github.cfogrady.vbnfc.data.block.CharacterTypeBlockTranslator
import com.github.cfogrady.vbnfc.data.block.CharacterStatusBlockTranslator
import com.github.cfogrady.vbnfc.getUInt16
import java.nio.ByteOrder

class BENfcDataTranslator(
    cryptographicTransformer: CryptographicTransformer,
    private val checksumCalculator: ChecksumCalculator = ChecksumCalculator()
): NfcDataTranslator<BENfcCharacter>(
    cryptographicTransformer = cryptographicTransformer,
    arrayOf(
        AppBlockTranslator(), // 0
        CharacterTypeBlockTranslator(), // 4
        BETransformationRequirementsBlockTranslator(), // 6
        CharacterStatusBlockTranslator(), // 8
        BETransformationHistoryBlockTranslator(0, 3), // 13
        BETransformationHistoryBlockTranslator(1, 3), // 14
        BETransformationHistoryBlockTranslator(2, 2), // 15
        StatTrainingBlockTranslator(), // 16
        BEAppBlockTranslator(), // 18

    )
) {

    companion object {

        const val OPERATION_PAGE: Byte = 0x6

        const val DIM_IDX = 74

        const val OTP_START_IDX = 352
        const val OTP_END_IDX = 359
        const val OTP2_START_IDX = 368
        const val OTP2_END_IDX = 375
    }

    override fun getOperationCommandBytes(header: NfcHeader, operation: Byte): ByteArray {
        return byteArrayOf(TagCommunicator.NFC_WRITE_COMMAND, OPERATION_PAGE, header.status, operation, header.dimIdBytes[0], header.dimIdBytes[1])
    }

    override fun createBaseCharacter(dataBytes: ByteArray): BENfcCharacter {
        return BENfcCharacter(
            dimId = dataBytes.getUInt16(DIM_IDX, ByteOrder.BIG_ENDIAN),
            otp0 = dataBytes.sliceArray(OTP_START_IDX..OTP_END_IDX),
            otp1 = dataBytes.sliceArray(OTP2_START_IDX..OTP2_END_IDX),
        )
    }

    override fun nonBlockAlignedWrites(character: BENfcCharacter, bytes: ByteArray) {
        super.nonBlockAlignedWrites(character, bytes)
        character.otp0.copyInto(bytes, OTP_START_IDX, 0, character.otp0.size)
        character.otp1.copyInto(bytes, OTP2_START_IDX, 0, character.otp1.size)
    }

    // finalizeByteArrayFormat finalizes the byte array for BE NFC format by setting all the
    // checksums, and duplicating the duplicate memory pages.
    override fun finalizeByteArrayFormat(bytes: ByteArray) {
        checksumCalculator.recalculateChecksums(bytes)
        performPageBlockDuplications(bytes)
    }

    override fun parseHeader(headerBytes: ByteArray): NfcHeader {
        Log.i(TagCommunicator.TAG, "Bytes in header: ${headerBytes.size}")
        val header = NfcHeader(
            deviceId = DeviceType.VitalBraceletBEDeviceType,
            deviceSubType = DeviceSubType.Original,
            vbCompatibleTagIdentifier = headerBytes.sliceArray(0..3), // this is a magic number used to verify that the tag is a VB.
            status = headerBytes[8],
            operation = headerBytes[9],
            dimIdBytes = headerBytes.sliceArray(10..11),
            appFlag = headerBytes[12],
            nonce = headerBytes.sliceArray(13..15)
        )
        Log.i(TagCommunicator.TAG, "Header: $header")
        return header
    }

    private val BlocksWithCopiesInterweaved = intArrayOf(2, 4, 6, 8, 16, 18, 20, 26)
    private fun performPageBlockDuplications(data: ByteArray, blocksToCopy: IntArray = BlocksWithCopiesInterweaved, copyOffset: Int = 16) {
        for (blockIndex in blocksToCopy) {
            val firstIndex = blockIndex*16
            for (i in firstIndex..firstIndex + 15) {
                data[i+copyOffset] = data[i]
            }
        }
    }
}