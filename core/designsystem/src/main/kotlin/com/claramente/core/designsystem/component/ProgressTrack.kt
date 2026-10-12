package com.claramente.core.designsystem.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun ProgressTrack(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = ClaramenteColors.Primary,
    trackColor: Color = ClaramenteColors.SurfaceMuted,
) {
    val value = progress.coerceIn(0f, 1f)
    val shape = RoundedCornerShape(ClaramenteRadius.Pill)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .semantics { progressBarRangeInfo = ProgressBarRangeInfo(value, 0f..1f) }
            .clip(shape)
            .background(trackColor),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(value)
                .fillMaxHeight()
                .clip(shape)
                .background(color),
        )
    }
}
