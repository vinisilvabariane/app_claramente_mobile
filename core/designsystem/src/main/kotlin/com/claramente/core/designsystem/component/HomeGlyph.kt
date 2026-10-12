package com.claramente.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp

@Composable
fun HomeGlyph(color: Color, modifier: Modifier = Modifier.size(24.dp)) {
    Canvas(modifier) {
        scale(size.minDimension / 24f, Offset.Zero) {
            val stroke = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            val roof = Path().apply {
                moveTo(4f, 11.5f)
                lineTo(12f, 4f)
                lineTo(20f, 11.5f)
            }
            val walls = Path().apply {
                moveTo(6f, 10f)
                lineTo(6f, 18f)
                arcTo(Rect(6f, 17f, 8f, 19f), 180f, -90f, false)
                lineTo(10f, 19f)
                lineTo(10f, 14f)
                lineTo(14f, 14f)
                lineTo(14f, 19f)
                lineTo(17f, 19f)
                arcTo(Rect(16f, 17f, 18f, 19f), 90f, -90f, false)
                lineTo(18f, 10f)
            }
            drawPath(roof, color, style = stroke)
            drawPath(walls, color, style = stroke)
        }
    }
}
