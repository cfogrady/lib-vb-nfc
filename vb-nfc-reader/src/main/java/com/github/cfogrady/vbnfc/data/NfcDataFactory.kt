package com.github.cfogrady.vbnfc.data

import android.util.Log
import com.github.cfogrady.vbnfc.ConvertToPages
import com.github.cfogrady.vbnfc.getUInt16
import java.nio.ByteOrder

class NfcDataFactory(private val beNfcDataFactory: BENfcDataFactory) {
    fun buildNfcDataFromBytes(rawCharacter: ByteArray, productId: UShort): NfcData<NfcCharacter, NfcDevice> {
        if (productId.toInt() == 4) {

        }
    }
}