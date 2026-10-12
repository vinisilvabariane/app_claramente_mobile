package com.claramente.core.designsystem.component

import com.claramente.core.designsystem.theme.ClaramenteColors
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
fun BrandMark(
    modifier: Modifier = Modifier.size(36.dp),
    background: Color = ClaramenteColors.Primary,
    foreground: Color = ClaramenteColors.White,
) {
    Canvas(modifier) {
        scale(size.minDimension / 40f, Offset.Zero) {
            drawRoundRect(background, Offset.Zero, Size(40f, 40f), CornerRadius(11f))
            drawArc(
                color = foreground,
                startAngle = 40f,
                sweepAngle = 280f,
                useCenter = false,
                topLeft = Offset(10f, 10f),
                size = Size(20f, 20f),
                style = Stroke(width = 4f, cap = StrokeCap.Round),
            )
            drawCircle(foreground, radius = 2.6f, center = Offset(20f, 20f))
        }
    }
}
