package com.claramente.feature.ar.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun ArOverlay(title: String, hint: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .statusBarsPadding()
            .padding(12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(ClaramenteRadius.Lg))
            .background(ClaramenteColors.Overlay)
            .padding(12.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(ClaramenteRadius.Pill))
                .background(ClaramenteColors.White.copy(alpha = 0.16f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    role = Role.Button,
                    onClick = onBack,
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Text(
                text = "Voltar",
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                color = ClaramenteColors.White,
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                color = ClaramenteColors.White,
            )
            Text(
                text = hint,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 16.sp),
                color = ClaramenteColors.White.copy(alpha = 0.75f),
                modifier = Modifier.padding(top = 1.dp),
            )
        }
    }
}
