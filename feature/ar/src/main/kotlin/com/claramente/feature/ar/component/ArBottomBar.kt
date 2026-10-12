package com.claramente.feature.ar.component

import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import com.claramente.feature.ar.state.ArExperienceActions
import com.claramente.feature.ar.state.ArExperienceUiState
import com.claramente.feature.ar.state.ArMode
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun ArBottomBar(state: ArExperienceUiState, actions: ArExperienceActions, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (state.mode == ArMode.QR && state.model != null) {
            PrimaryButton(text = "Ler outro QR code", onClick = actions.onRescan)
        } else {
            val text = when {
                state.mode == ArMode.QR -> "Procurando o QR code..."
                state.found -> "${state.title} - alvo encontrado"
                else -> "Procurando o alvo..."
            }
            val background = if (state.found) {
                ClaramenteColors.Success.copy(alpha = 0.92f)
            } else {
                ClaramenteColors.InkDeep.copy(alpha = 0.82f)
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.5.sp),
                color = ClaramenteColors.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(ClaramenteRadius.Pill))
                    .background(background)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
            )
        }
    }
}
