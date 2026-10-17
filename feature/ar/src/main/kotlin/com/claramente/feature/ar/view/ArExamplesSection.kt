package com.claramente.feature.ar.view

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.ar.component.ArExampleCard
import com.claramente.feature.ar.state.ArModel
import com.claramente.feature.ar.state.ArModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArExamplesSection(onOpen: (ArModel) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("Modelos de exemplo", style = MaterialTheme.typography.titleMedium, color = ClaramenteColors.Ink)
        Text(
            "Disponíveis para todos, para conhecer a realidade aumentada.",
            style = MaterialTheme.typography.bodySmall,
            color = ClaramenteColors.TextSecondary,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ArModels.all.forEach { model -> ArExampleCard(model = model, onOpen = { onOpen(model) }) }
        }
    }
}
