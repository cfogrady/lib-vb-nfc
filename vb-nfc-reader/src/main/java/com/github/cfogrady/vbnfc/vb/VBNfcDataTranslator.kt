package com.github.cfogrady.vbnfc.vb

import com.github.cfogrady.vbnfc.CryptographicTransformer
import com.github.cfogrady.vbnfc.NfcDataTranslator
import com.github.cfogrady.vbnfc.TagCommunicator
import com.github.cfogrady.vbnfc.be.BENfcDataTranslator.Companion.DIM_IDX
import com.github.cfogrady.vbnfc.data.DeviceType
import com.github.cfogrady.vbnfc.data.NfcHeader
import com.github.cfogrady.vbnfc.data.block.Block0Translator
import com.github.cfogrady.vbnfc.data.block.Block4Translator
import com.github.cfogrady.vbnfc.data.block.TransformationRequirementsBlockTranslator
import com.github.cfogrady.vbnfc.data.block.Block8Translator
import com.github.cfogrady.vbnfc.data.block.NoopBlockTranslator
import com.github.cfogrady.vbnfc.data.block.TransformationBlockTranslator
import com.github.cfogrady.vbnfc.getUInt16
import java.nio.ByteOrder

class VBNfcDataTranslator(cryptographicTransformer: CryptographicTransformer) : NfcDataTranslator<VBNfcCharacter>(
    cryptographicTransformer,
    arrayOf(
        Block0Translator(), // 0
        NoopBlockTranslator(), // 1
        NoopBlockTranslator(), // 2
        NoopBlockTranslator(), // 3
        Block4Translator(), // 4
        NoopBlockTranslator(), // 5
        VBTransformationRequirementsBlockTranslator(), // 6
        NoopBlockTranslator(), // 7
        Block8Translator(), // 8
        NoopBlockTranslator(), // 9
        NoopBlockTranslator(), // 10
        NoopBlockTranslator(), // 11
        NoopBlockTranslator(), // 12
        TransformationBlockTranslator(0, 3), // 13
        TransformationBlockTranslator(1, 3), // 14
        TransformationBlockTranslator(2, 2), // 15
        // StatTrainingBlockTranslator(), // 16
        NoopBlockTranslator(), // 17
        // BEAppBlockTranslator(), // 18
    )
) {

    companion object {
        const val OPERATION_PAGE: Byte = 0x6

        const val OTP_START_IDX = 352
    }

    override fun finalizeByteArrayFormat(bytes: ByteArray) {
        TODO("Not yet implemented")
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
            deviceType = DeviceType.VitalSeriesDeviceType,
            deviceSubType = headerBytes.getUInt16(6),
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