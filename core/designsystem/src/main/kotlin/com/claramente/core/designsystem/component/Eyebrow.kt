package com.claramente.core.designsystem.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Eyebrow(text: String, modifier: Modifier = Modifier, color: Color = ClaramenteColors.TextTertiary) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = color,
    )
}
