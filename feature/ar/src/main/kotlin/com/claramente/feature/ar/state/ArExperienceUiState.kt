package com.claramente.feature.ar.state

import com.claramente.feature.ar.policy.ArWebUrlPolicy
import com.claramente.feature.ar.policy.QrLayoutPolicy

data class ArExperienceUiState(
    val mode: ArMode = ArMode.MARKER,
    val model: ArModel? = null,
    val qrModels: List<ArModel> = emptyList(),
    val support: ArSupport = ArSupport.CHECKING,
    val cameraGranted: Boolean? = null,
    val cameraRequested: Boolean = false,
    val found: Boolean = false,
    val unknownCode: String? = null,
    val failure: String? = null,
    val trackingHint: String? = null,
    val arWebUrl: String? = null,
    val attempt: Int = 0,
    val available: List<ArModel> = ArModels.all,
) {
    fun resolveQr(raw: String): ArModel? = ArModels.resolveFromQr(raw, available)

    val stage: ArStage
        get() = when {
            !mode.usesCamera -> ArStage.RUNNING
            failure != null -> ArStage.FAILED
            support == ArSupport.CHECKING -> ArStage.CHECKING
            support == ArSupport.UNSUPPORTED -> ArStage.UNSUPPORTED
            support == ArSupport.FAILED -> ArStage.FAILED
            support == ArSupport.NEEDS_INSTALL -> ArStage.NEEDS_INSTALL
            cameraGranted == null -> ArStage.CHECKING
            cameraGranted == false && cameraRequested -> ArStage.PERMISSION_DENIED
            cameraGranted == false -> ArStage.NEEDS_PERMISSION
            else -> ArStage.RUNNING
        }

    val title: String
        get() = when {
            qrModels.size > 1 -> qrModels.joinToString(" + ") { it.title }
            model != null -> model.title
            else -> "Leitor de QR code"
        }

    val hint: String
        get() = when {
            mode == ArMode.QR && qrModels.size >= QrLayoutPolicy.MAX_MODELS ->
                "Limite de ${QrLayoutPolicy.MAX_MODELS} modelos — toque em Limpar para recomeçar"
            mode == ArMode.QR && model != null ->
                "Leia outro QR para adicionar · ${qrModels.size} de ${QrLayoutPolicy.MAX_MODELS} · arraste para girar"
            mode == ArMode.QR && unknownCode != null -> "QR não reconhecido — use os códigos do app"
            else -> mode.hint
        }

    val webUrl: String?
        get() = ArWebUrlPolicy.url(arWebUrl, model ?: ArModels.default)
}
