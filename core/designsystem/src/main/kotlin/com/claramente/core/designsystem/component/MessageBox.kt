package com.claramente.core.designsystem.component

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
fun MessageBox(message: String, modifier: Modifier = Modifier, isError: Boolean = true) {
    val shape = RoundedCornerShape(ClaramenteRadius.Md)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(if (isError) ClaramenteColors.ErrorFill else ClaramenteColors.SuccessMist, shape)
            .border(1.dp, if (isError) ClaramenteColors.ErrorBorder else ClaramenteColors.Success, shape)
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = if (isError) ClaramenteColors.ErrorText else ClaramenteColors.Ink,
        )
    }
}
