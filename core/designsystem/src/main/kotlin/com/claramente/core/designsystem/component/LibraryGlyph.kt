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
fun LibraryGlyph(color: Color, modifier: Modifier = Modifier.size(24.dp)) {
    Canvas(modifier) {
        scale(size.minDimension / 24f, Offset.Zero) {
            val stroke = Stroke(width = 2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            val top = Path().apply {
                moveTo(12f, 3.5f)
                lineTo(21f, 8f)
                lineTo(12f, 12.5f)
                lineTo(3f, 8f)
                close()
            }
            drawPath(top, color, style = stroke)
            val middle = Path().apply {
                moveTo(3f, 12f)
                lineTo(12f, 16.5f)
                lineTo(21f, 12f)
            }
            drawPath(middle, color, style = stroke)
            val bottom = Path().apply {
                moveTo(3f, 16f)
                lineTo(12f, 20.5f)
                lineTo(21f, 16f)
            }
            drawPath(bottom, color, style = stroke)
        }
    }
}
