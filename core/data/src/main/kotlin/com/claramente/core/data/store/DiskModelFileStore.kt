package com.claramente.core.data.store

import android.content.Context
import android.net.Uri
import com.claramente.core.data.contract.IModelFileStore
import java.io.File

class DiskModelFileStore(context: Context) : IModelFileStore {
    private val root = File(context.filesDir, "models").apply { mkdirs() }

    override fun cached(modelId: String, sha256: String): String? =
        fileFor(modelId, sha256).takeIf { it.isFile && it.length() > 0 }?.let { Uri.fromFile(it).toString() }

    override fun save(modelId: String, sha256: String, content: ByteArray): String {
        evict(modelId)
        val target = fileFor(modelId, sha256)
        val temporary = File(root, "${target.name}.tmp")
        temporary.writeBytes(content)
        if (!temporary.renameTo(target)) {
            temporary.delete()
            throw IllegalStateException("Não foi possível salvar o modelo no aparelho.")
        }
        return Uri.fromFile(target).toString()
    }

    override fun evict(modelId: String) {
        root.listFiles { file -> file.name.startsWith("${safe(modelId)}-") }?.forEach { it.delete() }
    }

    private fun fileFor(modelId: String, sha256: String): File = File(root, "${safe(modelId)}-${safe(sha256)}.glb")

    private fun safe(value: String): String = value.filter { it.isLetterOrDigit() || it == '-' }
}
