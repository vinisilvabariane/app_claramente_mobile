package com.claramente.feature.ar.component

import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ArModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ArLaunchPanel(model: ArModel, webUrl: String?, onStart: (ArMode) -> Unit, onOpenWeb: () -> Unit) {
    var mode by rememberSaveable(model.id) { mutableStateOf(ArMode.MARKER) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column {
            Eyebrow(text = model.subject)
            Text(
                model.title,
                style = MaterialTheme.typography.headlineSmall,
                color = ClaramenteColors.Ink,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Column {
            Text(
                "Como você quer ver?",
                style = MaterialTheme.typography.titleSmall,
                color = ClaramenteColors.Ink,
                modifier = Modifier.padding(bottom = 10.dp),
            )
            ArModePicker(modes = ArMode.entries, selected = mode, onSelect = { mode = it })
            Text(
                mode.hint,
                style = MaterialTheme.typography.bodySmall,
                color = ClaramenteColors.TextSecondary,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
        PrimaryButton(
            text = if (mode == ArMode.QR) "Ler QR code em AR" else "Abrir câmera",
            onClick = { onStart(mode) },
            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
        )
        if (webUrl != null) {
            SecondaryButton(text = "Abrir versão web", onClick = onOpenWeb, modifier = Modifier.fillMaxWidth())
        }
        ArFootnote()
    }
}
