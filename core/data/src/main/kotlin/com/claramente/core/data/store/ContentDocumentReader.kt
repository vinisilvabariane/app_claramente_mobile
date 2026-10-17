package com.claramente.core.data.store

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.claramente.core.data.contract.IDocumentReader
import com.claramente.core.model.classroom.DocumentInfo
import java.io.FileNotFoundException

class ContentDocumentReader(context: Context) : IDocumentReader {
    private val resolver = context.contentResolver

    override fun inspect(uri: String): DocumentInfo {
        val parsed = Uri.parse(uri)
        var name: String? = null
        var size = -1L
        resolver.query(parsed, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (nameIndex >= 0 && !cursor.isNull(nameIndex)) name = cursor.getString(nameIndex)
                if (sizeIndex >= 0 && !cursor.isNull(sizeIndex)) size = cursor.getLong(sizeIndex)
            }
        }
        if (size < 0) size = resolver.openAssetFileDescriptor(parsed, "r")?.use { it.length } ?: -1L
        return DocumentInfo(uri = uri, name = name ?: parsed.lastPathSegment ?: "modelo.glb", sizeBytes = size)
    }

    override fun readHeader(uri: String, byteCount: Int): ByteArray = open(uri).use { input ->
        val buffer = ByteArray(byteCount)
        var read = 0
        while (read < byteCount) {
            val count = input.read(buffer, read, byteCount - read)
            if (count < 0) break
            read += count
        }
        buffer.copyOf(read)
    }

    override fun readAll(uri: String): ByteArray = open(uri).use { it.readBytes() }

    private fun open(uri: String) =
        resolver.openInputStream(Uri.parse(uri)) ?: throw FileNotFoundException("Arquivo indisponível.")
}
