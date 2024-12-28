package com.github.cfogrady.vbnfc

import com.github.cfogrady.vbnfc.be.BENfcDataTranslator
import com.github.cfogrady.vbnfc.data.DeviceType

class NfcDataTranslatorFactory(
    private val beNfcDataTranslator: BENfcDataTranslator,
) {
    fun getNfcDataTranslator(deviceType: DeviceType): NfcDataTranslator {
        when(deviceType) {
            DeviceType.VitalBraceletBE ->
                return beNfcDataTranslator

            else -> {throw UnsupportedOperationException("Device type ${deviceType} is not yet supported")}
        }
    }
}