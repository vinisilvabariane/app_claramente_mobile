package com.claramente.feature.profile.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun ProfileInfoRow(label: String, value: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = ClaramenteColors.TextSecondary,
            modifier = Modifier.padding(end = 16.dp),
        )
        Text(
            value,
            style = MaterialTheme.typography.labelLarge,
            color = ClaramenteColors.Ink,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f),
        )
    }
}
