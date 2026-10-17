package com.claramente.feature.ar.component

import com.claramente.core.designsystem.component.ArGlyph
import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import com.claramente.feature.ar.state.ArModel
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
internal fun ArExampleCard(model: ArModel, onOpen: () -> Unit) {
    BrandCard(modifier = Modifier.fillMaxWidth(), onClick = onOpen) {
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(ClaramenteColors.SurfaceMuted, RoundedCornerShape(ClaramenteRadius.Md)),
                contentAlignment = Alignment.Center,
            ) {
                ArGlyph(color = ClaramenteColors.TextSecondary, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow(text = model.subject)
                Text(
                    model.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = ClaramenteColors.Ink,
                    modifier = Modifier.padding(top = 2.dp),
                )
                Text(
                    model.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ClaramenteColors.TextSecondary,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Text("Abrir", style = MaterialTheme.typography.labelLarge, color = ClaramenteColors.Primary)
        }
    }
}
