package com.claramente.feature.ar.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
internal fun ArFootnote(modifier: Modifier = Modifier) {
    Text(
        text = "A realidade aumentada usa a câmera do aparelho e o ARCore do Google. " +
            "Aponte para o alvo impresso, para o cubo ou para um QR code do material " +
            "para o conteúdo aparecer sobre ele.",
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
        color = ClaramenteColors.TextTertiary,
        modifier = modifier,
    )
}
