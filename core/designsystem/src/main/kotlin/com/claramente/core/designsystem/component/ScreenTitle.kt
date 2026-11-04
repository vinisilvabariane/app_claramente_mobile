package com.claramente.core.designsystem.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenTitle(eyebrow: String, title: String, modifier: Modifier = Modifier, subtitle: String? = null) {
    Column(modifier = modifier) {
        Eyebrow(text = eyebrow)
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            color = ClaramenteColors.Ink,
            modifier = Modifier.padding(top = 4.dp),
        )
        subtitle?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodyMedium,
                color = ClaramenteColors.TextSecondary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
