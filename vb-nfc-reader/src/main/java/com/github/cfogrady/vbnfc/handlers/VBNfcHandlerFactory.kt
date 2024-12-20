package com.github.cfogrady.vbnfc.handlers

import android.nfc.tech.MifareUltralight
import android.nfc.tech.NfcA
import android.util.Log
import com.github.cfogrady.vbnfc.getUInt16
import com.github.cfogrady.vbnfc.getUInt32
import java.nio.ByteOrder

class VBNfcHandlerFactory(private val vbSecrets: VBNfcHandler.Secrets, private val vbbeSecrets: VBNfcHandler.Secrets, private val vbcSecrets: VBNfcHandler.Secrets) {
    companion object {
        const val ITEM_ID_CHARACTER: UShort = 3u
        const val STATUS_READY: Byte = 1
        const val STATUS_DIM_READY: Byte = 3
        const val OPERATION_READY: Byte = 1
        const val OPERATION_CHECK_DIM: Byte = 3
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getHandler(nfcData: NfcA): VBNfcHandler {
        val readData = nfcData.transceive(byteArrayOf(VBNfcHandler.NFC_READ_COMMAND, VBNfcHandler.FIRST_DATA_PAGE))
        Log.i("VBNfcHandlerFactory", "First 8 Pages: ${readData.toHexString()}")
        val vbCompatibleTagIdentifier = readData.getUInt32(0, ByteOrder.BIG_ENDIAN) // this is a magic number used to verify that the tag is a VB.
        val deviceId = readData.getUInt16(4, ByteOrder.BIG_ENDIAN)
        val deviceSubId = readData.getUInt16(6, ByteOrder.BIG_ENDIAN)
        if (deviceId == VBBENfcHandler.DEVICE_ID) {
            return VBBENfcHandler(vbbeSecrets, nfcData, vbCompatibleTagIdentifier, deviceSubId, readData.sliceArray(8..<readData.size))
        } else if (deviceId == OriginalVBNfcHandler.DEVICE_ID) {
            return OriginalVBNfcHandler(vbSecrets, nfcData, vbCompatibleTagIdentifier, deviceSubId, readData.sliceArray(8..<readData.size))
        }
        Log.w("VBNfcHandlerFactory", "No Handler Defined for Device: $deviceId")
        throw InvalidDeviceException(deviceId, deviceSubId)
    }

    class InvalidDeviceException(val deviceId: UShort, val deviceSubId: UShort): Exception("No Handlers Defined For Device")
}