package com.github.cfogrady.vbnfc.handlers

import android.nfc.tech.NfcA

// This serves as the NFC Handler for Original VB, Digivice-V, and Vital Hero
class OriginalVBNfcHandler(secrets: Secrets, nfcData: NfcA, val vbCompatibleTagIdentifier: UInt, val itemId: UShort, pagesSixAndSeven: ByteArray) : VBNfcHandler(secrets, nfcData) {

    companion object {
        const val DEVICE_ID: UShort = 2u
    }

    var status: Byte = 0
    var operation: Byte = 0
    var dimId: Byte = 0
    var reserved: Byte = 0
    var appFlag: Byte = 0
    var nonce: ByteArray = byteArrayOf(0, 0, 0)

    init {
        readHeaderFromPageSixAndSeven(pagesSixAndSeven)
    }

    override fun readHeader() {
        TODO("Not yet implemented")
    }

    override fun getDeviceId(): Int {
        return DEVICE_ID.toInt()
    }

    override fun getHeaderDimId(): UShort {
        return dimId.toUShort()
    }

    override fun getOperationCommandBytes(operation: Byte): ByteArray {
        return byteArrayOf(NFC_WRITE_COMMAND, 0x06, status, dimId, operation, reserved)
    }

    fun readHeaderFromPageSixAndSeven(pages: ByteArray) {
        status = pages[0]
        dimId = pages[1]
        operation = pages[2]
        reserved = pages[3]
    }
}