package com.claramente.core.domain.policy

object ModelQrPolicy {
    const val PREFIX = "claramente:"

    fun content(modelId: String): String = PREFIX + modelId.lowercase()

    fun idFrom(raw: String): String {
        val text = raw.trim().lowercase()
        return if (text.startsWith(PREFIX)) text.removePrefix(PREFIX) else text
    }
}
