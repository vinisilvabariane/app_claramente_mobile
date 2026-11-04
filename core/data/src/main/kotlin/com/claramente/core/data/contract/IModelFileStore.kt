package com.claramente.core.data.contract

interface IModelFileStore {
    fun cached(modelId: String, sha256: String): String?

    fun save(modelId: String, sha256: String, content: ByteArray): String

    fun evict(modelId: String)
}
