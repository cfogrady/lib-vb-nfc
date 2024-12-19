package com.github.cfogrady.vbnfc

import java.nio.ByteOrder

class NfcCharacterFactory {
    fun buildNfcCharacterFromBytes(rawCharacter: ByteArray): NfcCharacter {
        val pages = convertToPages(rawCharacter)
        var page = pages[26]
        val slotId = page.getUInt16(0, ByteOrder.BIG_ENDIAN)
        val dimId = page.getUInt16(2, ByteOrder.BIG_ENDIAN)
        return NfcCharacter(dimId, slotId)
    }

    fun convertToPages(data: ByteArray) : List<ByteArray> {
        val pages = ArrayList<ByteArray>()
        // setup blank header pages
        for (i in 0..7) {
            pages.add(byteArrayOf(0, 0, 0, 0))
        }
        for(i in data.indices step 4) {
            pages.add(data.sliceArray(i..<i+4))
        }
        return pages
    }
}