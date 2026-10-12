package com.claramente.core.designsystem.component

import com.claramente.core.designsystem.theme.ButtonTone
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: ButtonTone = ButtonTone.PRIMARY,
    enabled: Boolean = true,
    loading: Boolean = false,
    containerColor: Color? = null,
    textColor: Color? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val active = enabled && !loading
    val shape = RoundedCornerShape(ClaramenteRadius.Md)
    val base = containerColor ?: tone.main
    val background = when {
        !pressed -> base
        containerColor != null -> base.copy(alpha = 0.88f)
        else -> tone.deep
    }
    Box(
        modifier = modifier
            .graphicsLayer { alpha = if (active) 1f else 0.5f }
            .clip(shape)
            .background(background)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = active,
                role = Role.Button,
                onClick = onClick,
            )
            .defaultMinSize(minHeight = 52.dp)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = textColor ?: ClaramenteColors.White,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = textColor ?: ClaramenteColors.White,
            )
        }
    }
}
