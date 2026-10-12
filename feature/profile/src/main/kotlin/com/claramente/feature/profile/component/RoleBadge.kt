package com.claramente.feature.profile.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun RoleBadge(label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(ClaramenteColors.SurfaceMuted, RoundedCornerShape(ClaramenteRadius.Sm))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
            color = ClaramenteColors.TextSecondary,
        )
    }
}
