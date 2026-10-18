package com.claramente.feature.ar.view

import com.claramente.core.designsystem.component.ArGlyph
import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArScannerSection(progress: Pair<Int, Int>?, onScan: () -> Unit, modifier: Modifier = Modifier) {
    BrandCard(modifier = modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                ArGlyph(color = ClaramenteColors.Primary, modifier = Modifier.size(24.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Câmera de RA", style = MaterialTheme.typography.titleSmall, color = ClaramenteColors.Ink)
                    Text(
                        "Leia os QR codes do material para colocar um ou mais modelos lado a lado.",
                        style = MaterialTheme.typography.bodySmall,
                        color = ClaramenteColors.TextSecondary,
                    )
                }
            }
            val text = progress?.let { (done, total) -> "Preparando modelos… $done de $total" } ?: "Ler QR codes"
            PrimaryButton(
                text = text,
                onClick = onScan,
                modifier = Modifier.fillMaxWidth(),
                enabled = progress == null,
                loading = progress != null,
            )
        }
    }
}
