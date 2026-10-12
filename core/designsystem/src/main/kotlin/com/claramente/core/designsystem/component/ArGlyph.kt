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
fun ArGlyph(color: Color, modifier: Modifier = Modifier.size(24.dp)) {
    Canvas(modifier) {
        scale(size.minDimension / 24f, Offset.Zero) {
            val stroke = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            val top = Path().apply {
                moveTo(4.5f, 7.5f)
                lineTo(12f, 3.2f)
                lineTo(19.5f, 7.5f)
                lineTo(12f, 11.8f)
                close()
            }
            val sides = Path().apply {
                moveTo(4.5f, 7.5f)
                lineTo(4.5f, 16.5f)
                lineTo(12f, 20.8f)
                lineTo(19.5f, 16.5f)
                lineTo(19.5f, 7.5f)
            }
            val edge = Path().apply {
                moveTo(12f, 11.8f)
                lineTo(12f, 20.8f)
            }
            drawPath(top, color, style = stroke)
            drawPath(sides, color, style = stroke)
            drawPath(edge, color, style = stroke)
        }
    }
}
