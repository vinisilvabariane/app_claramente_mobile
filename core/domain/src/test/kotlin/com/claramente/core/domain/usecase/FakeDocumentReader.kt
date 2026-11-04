package com.claramente.core.domain.usecase

import com.claramente.core.data.contract.IDocumentReader
import com.claramente.core.model.classroom.DocumentInfo

internal class FakeDocumentReader(private val name: String, private val content: ByteArray, private val reportedSize: Long = content.size.toLong()) :
    IDocumentReader {
    override fun inspect(uri: String): DocumentInfo = DocumentInfo(uri, name, reportedSize)

    override fun readHeader(uri: String, byteCount: Int): ByteArray = content.copyOf(minOf(byteCount, content.size))

    override fun readAll(uri: String): ByteArray = content
}
