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
                Assert.assertEquals("$msg block $blockIdx mismatch", formatBlockInHex(expectedBlock), formatBlockInHex(actualBlock))
            }
        }

        @OptIn(ExperimentalStdlibApi::class)
        fun formatBlockInHex(block: ByteArray): String {
            val builder = StringBuilder()
            for(i in block.indices step PAGE_SIZE) {
                for (j in i..<min(i+PAGE_SIZE, block.size)) {
                    builder.append(block[j].toHexString())
                }
                builder.append(System.lineSeparator())
            }
            return builder.toString()
        }
    }
}