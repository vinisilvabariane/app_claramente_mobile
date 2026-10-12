package com.claramente.feature.hub.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import com.claramente.feature.hub.state.FeedItem
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.sp

@Composable
internal fun FeedCard(item: FeedItem) {
    BrandCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                item.tag,
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                color = item.tone.color,
                modifier = Modifier
                    .background(item.tone.mist, RoundedCornerShape(ClaramenteRadius.Sm))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            )
            Text(
                item.time,
                style = MaterialTheme.typography.bodySmall,
                color = ClaramenteColors.TextTertiary,
                modifier = Modifier.weight(1f).padding(start = 10.dp),
            )
        }
        Text(
            item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = ClaramenteColors.Ink,
            modifier = Modifier.padding(top = 10.dp),
        )
    }
}
