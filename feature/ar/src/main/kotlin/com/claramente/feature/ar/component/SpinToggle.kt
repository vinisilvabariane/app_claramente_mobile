package com.claramente.feature.ar.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SpinToggle(active: Boolean, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    Text(
        text = if (active) "Girando" else "Girar sozinho",
        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
        color = ClaramenteColors.White,
        modifier = modifier
            .clip(RoundedCornerShape(ClaramenteRadius.Pill))
            .background(if (active) ClaramenteColors.Primary else ClaramenteColors.White.copy(alpha = 0.16f))
            .toggleable(
                value = active,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Switch,
                onValueChange = { onToggle() },
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}
