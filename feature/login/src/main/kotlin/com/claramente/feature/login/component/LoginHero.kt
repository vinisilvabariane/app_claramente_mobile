package com.claramente.feature.login.component

import com.claramente.core.designsystem.component.BrandMark
import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun LoginHero(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth().padding(top = 24.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            BrandMark(modifier = Modifier.size(34.dp))
            Text(
                text = "claramente",
                style = MaterialTheme.typography.titleLarge,
                color = ClaramenteColors.Ink,
            )
        }
        Eyebrow(
            text = "Ensino médio",
            color = ClaramenteColors.Primary,
            modifier = Modifier.padding(top = 40.dp, bottom = 8.dp),
        )
        Text(
            text = "Estude com clareza.",
            style = MaterialTheme.typography.displaySmall,
            color = ClaramenteColors.Ink,
        )
        Text(
            text = "Aulas, atividades da turma e um laboratório em realidade aumentada no mesmo lugar.",
            style = MaterialTheme.typography.bodyLarge,
            color = ClaramenteColors.TextSecondary,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}
