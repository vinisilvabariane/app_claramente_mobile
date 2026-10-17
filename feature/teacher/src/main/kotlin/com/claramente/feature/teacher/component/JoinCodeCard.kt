package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun JoinCodeCard(code: String, regenerating: Boolean, onShare: () -> Unit, onRegenerate: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ClaramenteColors.Ink, RoundedCornerShape(ClaramenteRadius.Xl))
            .padding(20.dp),
    ) {
        Eyebrow(text = "Código da turma", color = ClaramenteColors.White.copy(alpha = 0.6f))
        Text(
            code,
            style = MaterialTheme.typography.headlineLarge.merge(TextStyle(letterSpacing = 6.sp)),
            color = ClaramenteColors.White,
            modifier = Modifier.padding(top = 6.dp),
        )
        Text(
            "Os alunos digitam este código na aba Turma do app.",
            style = MaterialTheme.typography.bodySmall,
            color = ClaramenteColors.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            PrimaryButton(
                text = "Compartilhar",
                onClick = onShare,
                modifier = Modifier.weight(1f),
                containerColor = ClaramenteColors.White,
                textColor = ClaramenteColors.Ink,
            )
            PrimaryButton(
                text = "Gerar novo",
                onClick = onRegenerate,
                loading = regenerating,
                enabled = !regenerating,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
