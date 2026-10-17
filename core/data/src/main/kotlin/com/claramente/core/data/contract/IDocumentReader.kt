package com.claramente.core.data.contract

import com.claramente.core.model.classroom.DocumentInfo

interface IDocumentReader {
    fun inspect(uri: String): DocumentInfo

    fun readHeader(uri: String, byteCount: Int): ByteArray

    fun readAll(uri: String): ByteArray
}
