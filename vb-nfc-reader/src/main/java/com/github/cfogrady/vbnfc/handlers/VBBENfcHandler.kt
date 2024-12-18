package com.github.cfogrady.vbnfc.handlers

import android.nfc.tech.NfcA
import android.util.Log
import com.github.cfogrady.vbnfc.getUInt16
import java.nio.ByteOrder

class VBBENfcHandler(secrets: Secrets, nfcData: NfcA, val vbCompatibleTagIdentifier: UInt, val itemId: UShort, pagesSixAndSeven: ByteArray) : VBNfcHandler(secrets, nfcData) {

    companion object {
        const val DEVICE_ID: UShort = 4u
    }

    var status: Byte = 0
    var operation: Byte = 0
    var dimIdBytes: ByteArray = byteArrayOf(0, 0)
    var appFlag: Byte = 0
    var nonce: ByteArray = byteArrayOf(0, 0, 0)

    init {
        readHeaderFromPageSixAndSeven(pagesSixAndSeven)
    }

    override fun readHeader() {
        TODO("Not yet implemented")
    }

    override fun sendCharacter() {
        TODO("Not yet implemented")
    }

    override fun getDeviceId(): Int {
        return DEVICE_ID.toInt()
    }

    override fun getOperationCommandBytes(operation: Byte): ByteArray {
        return byteArrayOf(NFC_WRITE_COMMAND, 0x06, status, operation, dimIdBytes[0], dimIdBytes[1])
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun readHeaderFromPageSixAndSeven(pageBytes: ByteArray) {
        Log.i(TAG, "Bytes in header: ${pageBytes.size}")
        status = pageBytes[0]
        operation = pageBytes[1]
        dimIdBytes = pageBytes.sliceArray(2..3)
        appFlag = pageBytes[4]
        nonce = pageBytes.sliceArray(5..7)
        Log.i(TAG, "Header: Status $status, Operation $operation, DIM ${getDimId()} AppFlag $appFlag, Nonce ${nonce.toHexString()}")
    }

    fun getDimId(): UShort {
        return dimIdBytes.getUInt16(0, ByteOrder.BIG_ENDIAN)
    }
}