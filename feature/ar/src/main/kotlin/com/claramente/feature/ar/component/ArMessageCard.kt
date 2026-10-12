package com.claramente.feature.ar.component

import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun ArMessageCard(
    title: String,
    text: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    primaryLabel: String? = null,
    onPrimary: () -> Unit = {},
    secondaryLabel: String? = null,
    onSecondary: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ClaramenteColors.Background)
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = ClaramenteColors.Ink,
            textAlign = TextAlign.Center,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = ClaramenteColors.TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(max = 340.dp).padding(bottom = 8.dp),
        )
        if (primaryLabel != null) {
            PrimaryButton(text = primaryLabel, onClick = onPrimary)
        }
        if (secondaryLabel != null) {
            TextButton(onClick = onSecondary) {
                Text(secondaryLabel, color = ClaramenteColors.Primary, style = MaterialTheme.typography.labelLarge)
            }
        }
        TextButton(onClick = onBack) {
            Text("Voltar", color = ClaramenteColors.TextSecondary, style = MaterialTheme.typography.labelLarge)
        }
    }
}
