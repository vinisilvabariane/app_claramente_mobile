package com.claramente.feature.ar.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import com.claramente.feature.ar.state.ArMode
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ArModePicker(modes: List<ArMode>, selected: ArMode, onSelect: (ArMode) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(ClaramenteColors.SurfaceMuted, RoundedCornerShape(ClaramenteRadius.Md))
            .padding(4.dp)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        modes.forEach { mode ->
            ArModeChip(
                mode = mode,
                selected = mode == selected,
                onClick = { onSelect(mode) },
                modifier = Modifier.weight(1f).fillMaxHeight(),
            )
        }
    }
}
