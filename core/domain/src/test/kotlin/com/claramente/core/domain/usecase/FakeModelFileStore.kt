package com.claramente.core.domain.usecase

import com.claramente.core.data.contract.IModelFileStore

internal class FakeModelFileStore : IModelFileStore {
    val files = mutableMapOf<String, ByteArray>()

    override fun cached(modelId: String, sha256: String): String? = "file:///$modelId-$sha256.glb".takeIf { "$modelId-$sha256" in files }

    override fun save(modelId: String, sha256: String, content: ByteArray): String {
        evict(modelId)
        files["$modelId-$sha256"] = content
        return "file:///$modelId-$sha256.glb"
    }

    override fun evict(modelId: String) {
        files.keys.removeAll { it.startsWith("$modelId-") }
    }
}
