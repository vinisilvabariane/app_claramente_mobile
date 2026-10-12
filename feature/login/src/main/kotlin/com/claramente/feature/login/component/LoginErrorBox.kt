package com.claramente.feature.login.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun LoginErrorBox(message: String, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(ClaramenteRadius.Md)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(ClaramenteColors.ErrorFill, shape)
            .border(1.dp, ClaramenteColors.ErrorBorder, shape)
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = ClaramenteColors.ErrorText,
        )
    }
}
