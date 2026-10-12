package com.claramente.feature.login.component

import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun MockLoginBox(onClick: () -> Unit, enabled: Boolean = true, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = ClaramenteColors.Border)
            Text(text = "ou", style = MaterialTheme.typography.bodySmall, color = ClaramenteColors.TextTertiary)
            HorizontalDivider(modifier = Modifier.weight(1f), color = ClaramenteColors.Border)
        }
        SecondaryButton(
            text = "Entrar em modo teste",
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
        )
        Text(
            text = "Sem backend: gera uma sessão local para testar o app no celular.",
            style = MaterialTheme.typography.bodySmall,
            color = ClaramenteColors.TextTertiary,
            textAlign = TextAlign.Center,
        )
    }
}
