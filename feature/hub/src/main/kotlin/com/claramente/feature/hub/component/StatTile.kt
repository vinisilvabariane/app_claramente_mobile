package com.claramente.feature.hub.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.hub.state.HubStat
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun StatTile(stat: HubStat, modifier: Modifier = Modifier) {
    BrandCard(modifier = modifier, contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)) {
        Text(
            text = stat.value,
            style = MaterialTheme.typography.titleLarge,
            color = ClaramenteColors.Ink,
        )
        Text(
            text = stat.label,
            style = MaterialTheme.typography.bodySmall,
            color = ClaramenteColors.TextSecondary,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}
