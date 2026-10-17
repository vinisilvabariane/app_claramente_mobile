package com.claramente.feature.ar.policy

import com.claramente.feature.ar.state.ArModel

object ArWebUrlPolicy {
    fun url(baseUrl: String?, model: ArModel): String? =
        baseUrl?.takeIf { it.isNotBlank() && !model.isCustom }?.trimEnd('/')?.let { "$it/ar/index.html?modelo=${model.id}" }
}
