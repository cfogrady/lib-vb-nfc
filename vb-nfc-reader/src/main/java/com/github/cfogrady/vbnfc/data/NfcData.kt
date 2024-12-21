package com.github.cfogrady.vbnfc.data

open class NfcData<T : NfcCharacter, K : NfcDevice>(val nfcCharacter: T, val nfcDevice: K)