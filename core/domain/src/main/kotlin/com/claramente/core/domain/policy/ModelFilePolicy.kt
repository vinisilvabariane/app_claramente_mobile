package com.claramente.core.domain.policy

object ModelFilePolicy {
    const val MAX_BYTES = 25L * 1024 * 1024
    const val HEADER_BYTES = 20
    private const val MAGIC = 0x46546C67L
    private const val VERSION = 2L
    private const val JSON_CHUNK = 0x4E4F534AL

    fun isGlb(header: ByteArray, sizeBytes: Long): Boolean =
        header.size >= HEADER_BYTES &&
            uint32(header, 0) == MAGIC &&
            uint32(header, 4) == VERSION &&
            (sizeBytes < 0 || uint32(header, 8) == sizeBytes) &&
            uint32(header, 16) == JSON_CHUNK

    fun fits(sizeBytes: Long): Boolean = sizeBytes in 1..MAX_BYTES

    private fun uint32(bytes: ByteArray, offset: Int): Long =
        (0 until 4).fold(0L) { acc, index -> acc or ((bytes[offset + index].toLong() and 0xFF) shl (8 * index)) }
}
