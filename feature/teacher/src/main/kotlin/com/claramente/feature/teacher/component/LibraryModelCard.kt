package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.component.ProgressTrack
import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.theme.ButtonTone
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.domain.policy.FileSizePolicy
import com.claramente.core.model.classroom.Model3D
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun LibraryModelCard(
    model: Model3D,
    preparing: Boolean,
    progress: Float,
    deleting: Boolean,
    busy: Boolean,
    onPreview: () -> Unit,
    onDelete: () -> Unit,
    onShowQr: () -> Unit,
) {
    BrandCard(modifier = Modifier.fillMaxWidth()) {
        Eyebrow(text = model.subject ?: "Modelo 3D")
        Text(
            model.title,
            style = MaterialTheme.typography.titleSmall,
            color = ClaramenteColors.Ink,
            modifier = Modifier.padding(top = 2.dp),
        )
        model.description?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = ClaramenteColors.TextSecondary, modifier = Modifier.padding(top = 2.dp))
        }
        Text(
            "${FileSizePolicy.label(model.sizeBytes)} · " + when (model.classroomIds.size) {
                0 -> "não liberado em turmas"
                1 -> "liberado em 1 turma"
                else -> "liberado em ${model.classroomIds.size} turmas"
            },
            style = MaterialTheme.typography.labelSmall,
            color = ClaramenteColors.TextTertiary,
            modifier = Modifier.padding(top = 4.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            PrimaryButton(
                text = if (preparing) "${(progress * 100).toInt()}%" else "Testar em AR",
                onClick = onPreview,
                enabled = !busy,
                modifier = Modifier.weight(1f),
            )
            SecondaryButton(
                text = "QR code",
                onClick = onShowQr,
                modifier = Modifier.weight(1f),
            )
        }
        if (preparing) {
            ProgressTrack(progress = progress, modifier = Modifier.padding(top = 10.dp))
        }
        SecondaryButton(
            text = if (deleting) "Excluindo…" else "Excluir",
            onClick = onDelete,
            enabled = !busy,
            tone = ButtonTone.DANGER,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        )
    }
}
