package com.claramente.core.designsystem.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AvatarInitials(initials: String, modifier: Modifier = Modifier, size: Dp = 44.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(ClaramenteColors.PrimaryMist),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = (size.value * 0.36f).sp),
            color = ClaramenteColors.PrimaryDeep,
        )
    }
}
