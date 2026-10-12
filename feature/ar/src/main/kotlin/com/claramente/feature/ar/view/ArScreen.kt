package com.claramente.feature.ar.view

import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.ar.component.ArFootnote
import com.claramente.feature.ar.component.ArHeader
import com.claramente.feature.ar.component.ArModePicker
import com.claramente.feature.ar.component.ArModelCard
import com.claramente.feature.ar.state.ArActions
import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ArUiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArScreen(state: ArUiState, actions: ArActions) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ClaramenteColors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            ArHeader()
            Column {
                Text(
                    text = "Modelos",
                    style = MaterialTheme.typography.titleMedium,
                    color = ClaramenteColors.Ink,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    state.models.forEach { model ->
                        ArModelCard(
                            model = model,
                            selected = model.id == state.selected.id,
                            onClick = { actions.onSelect(model) },
                        )
                    }
                }
            }
            Column {
                Text(
                    text = "Modo de visualização",
                    style = MaterialTheme.typography.titleMedium,
                    color = ClaramenteColors.Ink,
                    modifier = Modifier.padding(bottom = 12.dp),
                )
                ArModePicker(modes = state.modes, selected = state.mode, onSelect = actions.onSelectMode)
                Text(
                    text = state.mode.hint,
                    style = MaterialTheme.typography.bodySmall,
                    color = ClaramenteColors.TextSecondary,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                PrimaryButton(
                    text = if (state.mode == ArMode.QR) "Ler QR code em AR" else "Abrir ${state.selected.title}",
                    onClick = actions.onStart,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                )
                if (state.arWebUrl != null) {
                    SecondaryButton(text = "Abrir versão web", onClick = actions.onOpenWeb, modifier = Modifier.fillMaxWidth())
                }
            }
            ArFootnote()
        }
    }
}
