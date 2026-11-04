package com.claramente.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp

@Composable
fun ClassroomGlyph(color: Color, modifier: Modifier = Modifier.size(24.dp)) {
    Canvas(modifier) {
        scale(size.minDimension / 24f, Offset.Zero) {
            drawRoundRect(
                color = color,
                topLeft = Offset(3f, 4f),
                size = Size(18f, 12f),
                cornerRadius = CornerRadius(2f, 2f),
                style = Stroke(width = 2f),
            )
            drawLine(color, Offset(7f, 8.5f), Offset(13f, 8.5f), strokeWidth = 2f, cap = StrokeCap.Round)
            drawLine(color, Offset(7f, 11.5f), Offset(11f, 11.5f), strokeWidth = 2f, cap = StrokeCap.Round)
            drawLine(color, Offset(9f, 20f), Offset(15f, 20f), strokeWidth = 2f, cap = StrokeCap.Round)
            drawLine(color, Offset(12f, 16f), Offset(12f, 20f), strokeWidth = 2f, cap = StrokeCap.Round)
        }
    }
}
