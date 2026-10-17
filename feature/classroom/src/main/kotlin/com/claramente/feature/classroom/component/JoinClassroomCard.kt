package com.claramente.feature.classroom.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.BrandTextField
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
internal fun JoinClassroomCard(
    title: String,
    code: String,
    canJoin: Boolean,
    joining: Boolean,
    onCode: (String) -> Unit,
    onJoin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BrandCard(modifier = modifier.fillMaxWidth()) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = ClaramenteColors.Ink)
        Text(
            "Digite o código de 6 caracteres que o professor passou.",
            style = MaterialTheme.typography.bodySmall,
            color = ClaramenteColors.TextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp),
        )
        BrandTextField(
            value = code,
            onValueChange = onCode,
            placeholder = "Ex.: K7M2QX",
            label = "Código da turma",
            keyboardType = KeyboardType.Ascii,
            imeAction = ImeAction.Done,
            onImeAction = { if (canJoin) onJoin() },
            enabled = !joining,
        )
        PrimaryButton(
            text = "Entrar na turma",
            onClick = onJoin,
            enabled = canJoin,
            loading = joining,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        )
    }
}
