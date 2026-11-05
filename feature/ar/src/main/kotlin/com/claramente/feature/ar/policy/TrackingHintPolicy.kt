package com.claramente.feature.ar.policy

import com.google.ar.core.TrackingFailureReason

internal object TrackingHintPolicy {
    fun hint(reason: TrackingFailureReason?): String? = when (reason) {
        null, TrackingFailureReason.NONE -> null
        TrackingFailureReason.EXCESSIVE_MOTION -> "Mova o celular mais devagar"
        TrackingFailureReason.INSUFFICIENT_LIGHT -> "Pouca luz: vá para um lugar mais claro"
        TrackingFailureReason.INSUFFICIENT_FEATURES -> "Aponte para uma superfície com mais detalhes"
        TrackingFailureReason.CAMERA_UNAVAILABLE -> "A câmera está sendo usada por outro app"
        TrackingFailureReason.BAD_STATE -> "Reiniciando o rastreamento..."
    }
}
