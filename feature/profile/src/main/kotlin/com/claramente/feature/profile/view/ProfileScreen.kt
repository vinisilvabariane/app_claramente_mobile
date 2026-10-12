package com.claramente.feature.profile.view

import com.claramente.core.auth.model.UserSession
import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.theme.ButtonTone
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.profile.component.IdentityCard
import com.claramente.feature.profile.component.ProfileInfoRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(user: UserSession?, loggingOut: Boolean, onLogout: () -> Unit) {
    val details = listOfNotNull(
        user?.registration?.takeIf { it.isNotBlank() }?.let { "Matrícula" to it },
        user?.department?.takeIf { it.isNotBlank() }?.let { "Turma" to it },
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClaramenteColors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column {
                Eyebrow(text = "Conta")
                Text(
                    "Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    color = ClaramenteColors.Ink,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            IdentityCard(user = user)
            if (details.isNotEmpty()) {
                Column {
                    Text(
                        "Dados acadêmicos",
                        style = MaterialTheme.typography.titleMedium,
                        color = ClaramenteColors.Ink,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )
                    BrandCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    ) {
                        details.forEachIndexed { index, (label, value) ->
                            if (index > 0) HorizontalDivider(color = ClaramenteColors.Border)
                            ProfileInfoRow(label = label, value = value)
                        }
                    }
                }
            }
            SecondaryButton(
                text = "Sair da conta",
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                tone = ButtonTone.DANGER,
                enabled = !loggingOut,
            )
        }
    }
}
