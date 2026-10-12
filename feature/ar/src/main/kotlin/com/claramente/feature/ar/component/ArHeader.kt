package com.claramente.feature.ar.component

import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ArHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Eyebrow(text = "Laboratório · Realidade aumentada")
        Text(
            text = "Explore em 3D",
            style = MaterialTheme.typography.headlineMedium,
            color = ClaramenteColors.Ink,
            modifier = Modifier.padding(top = 4.dp),
        )
        Text(
            text = "Escolha um modelo, aponte a câmera e observe o conteúdo de todos os ângulos.",
            style = MaterialTheme.typography.bodyMedium,
            color = ClaramenteColors.TextSecondary,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}
