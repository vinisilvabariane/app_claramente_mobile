package com.claramente.feature.ar.state

import com.google.ar.core.Anchor

data class PlacedQrModel(val anchor: Anchor, val model: ArModel, val onSurface: Boolean)
