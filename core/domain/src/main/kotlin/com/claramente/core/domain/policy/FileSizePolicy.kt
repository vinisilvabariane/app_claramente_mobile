package com.claramente.core.domain.policy

import java.util.Locale

object FileSizePolicy {
    fun label(bytes: Long): String = when {
        bytes < 0 -> "tamanho desconhecido"
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> String.format(Locale.forLanguageTag("pt-BR"), "%.0f KB", bytes / 1024.0)
        else -> String.format(Locale.forLanguageTag("pt-BR"), "%.1f MB", bytes / (1024.0 * 1024.0))
    }
}
