package com.claramente.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp

@Composable
fun ProfileGlyph(color: Color, modifier: Modifier = Modifier.size(24.dp)) {
    Canvas(modifier) {
        scale(size.minDimension / 24f, Offset.Zero) {
            drawCircle(color, radius = 3.4f, center = Offset(12f, 8.2f), style = Stroke(width = 2f))
            val shoulders = Path().apply {
                moveTo(5f, 20f)
                cubicTo(6.1f, 16.7f, 8.9f, 15f, 12f, 15f)
                cubicTo(15.1f, 15f, 17.9f, 16.7f, 19f, 20f)
            }
            drawPath(shoulders, color, style = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round))
        }
    }
}
