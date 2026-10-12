package com.claramente.core.designsystem.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun BrandTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    password: Boolean = false,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
) {
    val shape = RoundedCornerShape(ClaramenteRadius.Md)
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val textStyle = MaterialTheme.typography.bodyLarge.copy(color = ClaramenteColors.Ink)
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (label != null) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = ClaramenteColors.Ink)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            textStyle = textStyle,
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),
            keyboardActions = KeyboardActions(
                onAny = {
                    onImeAction()
                    defaultKeyboardAction(imeAction)
                },
            ),
            singleLine = true,
            visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
            cursorBrush = SolidColor(ClaramenteColors.Primary),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ClaramenteColors.FieldFill, shape)
                        .border(
                            width = if (focused) 2.dp else 1.dp,
                            color = if (focused) ClaramenteColors.Primary else ClaramenteColors.FieldBorder,
                            shape = shape,
                        )
                        .padding(horizontal = 14.dp, vertical = 13.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge,
                            color = ClaramenteColors.TextTertiary,
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}
