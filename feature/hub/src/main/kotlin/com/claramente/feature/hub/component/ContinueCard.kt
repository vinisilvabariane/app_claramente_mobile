package com.claramente.feature.hub.component

import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.component.ProgressTrack
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ContinueCard(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ClaramenteColors.Ink, RoundedCornerShape(ClaramenteRadius.Xl))
            .padding(20.dp),
    ) {
        Eyebrow(text = "Continue estudando", color = ClaramenteColors.White.copy(alpha = 0.6f))
        Text(
            "Biologia · Módulo 3",
            style = MaterialTheme.typography.labelMedium,
            color = ClaramenteColors.PrimaryMist,
            modifier = Modifier.padding(top = 14.dp),
        )
        Text(
            "Genética e hereditariedade",
            style = MaterialTheme.typography.headlineSmall,
            color = ClaramenteColors.White,
            modifier = Modifier.padding(top = 2.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Progresso do módulo",
                style = MaterialTheme.typography.bodySmall,
                color = ClaramenteColors.White.copy(alpha = 0.7f),
                modifier = Modifier.weight(1f),
            )
            Text(
                "68%",
                style = MaterialTheme.typography.labelMedium,
                color = ClaramenteColors.White,
            )
        }
        ProgressTrack(
            progress = 0.68f,
            color = ClaramenteColors.Primary,
            trackColor = ClaramenteColors.White.copy(alpha = 0.14f),
        )
        PrimaryButton(
            text = "Retomar aula",
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            containerColor = ClaramenteColors.White,
            textColor = ClaramenteColors.Ink,
        )
    }
}
