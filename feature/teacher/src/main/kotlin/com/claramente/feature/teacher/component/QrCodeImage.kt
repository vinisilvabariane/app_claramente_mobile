package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import kotlin.math.floor

@Composable
internal fun QrCodeImage(matrix: List<BooleanArray>, modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .aspectRatio(1f)
            .background(ClaramenteColors.White)
            .padding(12.dp),
    ) {
        val count = matrix.size
        if (count == 0) return@Canvas
        val cell = floor(size.minDimension / count)
        val origin = floor((size.minDimension - cell * count) / 2f)
        matrix.forEachIndexed { y, row ->
            var x = 0
            while (x < row.size) {
                if (!row[x]) {
                    x++
                    continue
                }
                val start = x
                while (x < row.size && row[x]) x++
                drawRect(
                    color = ClaramenteColors.Ink,
                    topLeft = Offset(origin + start * cell, origin + y * cell),
                    size = Size((x - start) * cell, cell),
                )
            }
        }
    }
}
