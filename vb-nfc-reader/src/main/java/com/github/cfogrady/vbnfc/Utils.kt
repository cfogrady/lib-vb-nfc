package com.github.cfogrady.vbnfc

import kotlin.math.min

// ConverToPages converts the byte array into the paged structure used in NFC communication
// If data for the header isn't included, the first 8 pages will be 0 filled.
fun ConvertToPages(data: ByteArray, header: ByteArray? = null) : List<ByteArray> {
    val pages = ArrayList<ByteArray>()
    // setup blank header pages
    for (i in 0..7) {
        if (header != null) {
            val index = i*4
            pages.add(header.sliceArray(index..<index+4))
        } else {
            pages.add(byteArrayOf(0, 0, 0, 0))
        }
    }
    for(i in data.indices step 4) {
        pages.add(data.sliceArray(i..<i+4))
    }
    return pages
}

fun FormatPagedBytes(data: ByteArray): String {
    val builder = StringBuilder()
    for(i in data.indices step 4) {
        if(i % 16 == 0) {
            builder.append("Block ${i/16}:").append(System.lineSeparator())
        }
        for (j in i..<min(i+4, data.size)) {
            builder.append(String.format("%03d", data[j].toUByte().toShort())).append(" ")
        }
        builder.append(System.lineSeparator())
    }
    return builder.toString()
}