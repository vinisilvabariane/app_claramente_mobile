package com.claramente.feature.ar.state

data class ArModel(
    val id: String,
    val title: String,
    val subject: String,
    val description: String,
    val fileUri: String? = null,
) {
    val isCustom: Boolean
        get() = fileUri != null
}
