package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.model.classroom.Model3D
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun PublishModelRow(model: Model3D, published: Boolean, busy: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(model.title, style = MaterialTheme.typography.bodyMedium, color = ClaramenteColors.Ink)
            Text(
                if (published) "Liberado para a turma" else "Não liberado",
                style = MaterialTheme.typography.bodySmall,
                color = if (published) ClaramenteColors.Success else ClaramenteColors.TextSecondary,
            )
        }
        Switch(
            checked = published,
            onCheckedChange = onToggle,
            enabled = !busy,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ClaramenteColors.White,
                checkedTrackColor = ClaramenteColors.Primary,
                uncheckedThumbColor = ClaramenteColors.White,
                uncheckedTrackColor = ClaramenteColors.BorderStrong,
                uncheckedBorderColor = ClaramenteColors.BorderStrong,
            ),
        )
    }
}
