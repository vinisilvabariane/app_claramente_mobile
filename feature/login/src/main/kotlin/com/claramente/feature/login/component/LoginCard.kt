package com.claramente.feature.login.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.BrandTextField
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.login.state.LoginActions
import com.claramente.feature.login.state.LoginUiState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
internal fun LoginCard(state: LoginUiState, actions: LoginActions, modifier: Modifier = Modifier) {
    BrandCard(modifier = modifier, contentPadding = PaddingValues(20.dp)) {
        Text(
            text = "Entrar",
            style = MaterialTheme.typography.headlineSmall,
            color = ClaramenteColors.Ink,
        )
        Text(
            text = "Use o e-mail e a senha cadastrados pela sua escola.",
            style = MaterialTheme.typography.bodyMedium,
            color = ClaramenteColors.TextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp),
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            state.error?.let { LoginErrorBox(message = it) }
            BrandTextField(
                value = state.email,
                onValueChange = actions.onEmail,
                label = "E-mail",
                placeholder = "nome@escola.com.br",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email,
            )
            BrandTextField(
                value = state.password,
                onValueChange = actions.onPassword,
                label = "Senha",
                placeholder = "Sua senha",
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password,
                password = true,
                imeAction = ImeAction.Done,
                onImeAction = { if (state.canSubmit) actions.onSubmit() },
            )
            PrimaryButton(
                text = if (state.loading) "Entrando..." else "Entrar",
                onClick = actions.onSubmit,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                enabled = state.canSubmit,
                loading = state.loading,
            )
            if (state.mockEnabled) {
                MockLoginBox(onClick = actions.onMockLogin)
            }
        }
    }
}
