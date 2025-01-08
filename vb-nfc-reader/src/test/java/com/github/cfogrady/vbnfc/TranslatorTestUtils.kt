package com.github.cfogrady.vbnfc

import org.junit.Assert
import kotlin.math.min

class TranslatorTestUtils {

    companion object {
        const val BLOCK_SIZE = 16
        const val PAGE_SIZE = 4

        fun assertAllBlocksAreEqual(msg: String = "", expected: ByteArray, actual: ByteArray) {
            Assert.assertEquals("$msg size mismatch", expected.size, actual.size)
            for(i in actual.indices step BLOCK_SIZE) {
                val blockIdx = i/ BLOCK_SIZE
                val expectedBlock = expected.sliceArray(i..<i+16)
                val actualBlock = actual.sliceArray(i..<i+16)
                Assert.assertEquals("$msg block $blockIdx mismatch", FormatBlockInHex(expectedBlock), FormatBlockInHex(actualBlock))
            }
        }
    }
}