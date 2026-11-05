package com.claramente.feature.ar.state

data class ArExperienceActions(
    val onBack: () -> Unit,
    val onSupport: (ArSupport) -> Unit,
    val onCameraState: (Boolean) -> Unit,
    val onCameraRequested: () -> Unit,
    val onFound: (Boolean) -> Unit,
    val onQr: (String) -> Unit,
    val onRescan: () -> Unit,
    val onSessionFailed: (String?) -> Unit,
    val onTrackingHint: (String?) -> Unit,
    val onRetry: () -> Unit,
    val onViewer: () -> Unit,
    val onOpenWeb: () -> Unit,
)
