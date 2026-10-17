package com.claramente.feature.classroom.component

import com.claramente.core.designsystem.component.ArGlyph
import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.component.ProgressTrack
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import com.claramente.core.domain.policy.FileSizePolicy
import com.claramente.core.model.classroom.Model3D
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ClassroomModelCard(model: Model3D, preparing: Boolean, progress: Float, enabled: Boolean, onOpen: () -> Unit) {
    BrandCard(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(ClaramenteColors.PrimaryMist, RoundedCornerShape(ClaramenteRadius.Md)),
                contentAlignment = Alignment.Center,
            ) {
                ArGlyph(color = ClaramenteColors.Primary, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow(text = model.subject ?: "Modelo 3D")
                Text(
                    model.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = ClaramenteColors.Ink,
                    modifier = Modifier.padding(top = 2.dp),
                )
                model.description?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = ClaramenteColors.TextSecondary,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                Text(
                    FileSizePolicy.label(model.sizeBytes),
                    style = MaterialTheme.typography.labelSmall,
                    color = ClaramenteColors.TextTertiary,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
        PrimaryButton(
            text = if (preparing) "Baixando ${(progress * 100).toInt()}%" else "Ver em realidade aumentada",
            onClick = onOpen,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
        )
        if (preparing) {
            ProgressTrack(progress = progress, modifier = Modifier.padding(top = 10.dp))
            Text(
                "${FileSizePolicy.label((model.sizeBytes * progress).toLong())} de ${FileSizePolicy.label(model.sizeBytes)}",
                style = MaterialTheme.typography.labelSmall,
                color = ClaramenteColors.TextTertiary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}
