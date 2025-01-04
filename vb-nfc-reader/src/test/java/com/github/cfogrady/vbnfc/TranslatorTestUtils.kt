package com.github.cfogrady.vbnfc

import org.junit.Assert
import kotlin.math.min

class TranslatorTestUtils {

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun assertAllBlocksAreEqual(msg: String = "", expected: ByteArray, actual: ByteArray) {
            val blockSize = 16
            Assert.assertEquals("$msg size mismatch", expected.size, actual.size)
            for(i in actual.indices step blockSize) {
                val block = i/16
                val endIdx = min(i+blockSize, actual.size)
                Assert.assertEquals("$msg block $block mismatch", expected.sliceArray(i..<endIdx).toHexString(), actual.sliceArray(i..<endIdx).toHexString())
            }
        }
    }
}