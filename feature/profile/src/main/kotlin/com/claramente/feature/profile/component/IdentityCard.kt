package com.claramente.feature.profile.component

import com.claramente.core.auth.model.UserSession
import com.claramente.core.designsystem.component.AvatarInitials
import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun IdentityCard(user: UserSession?) {
    BrandCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(18.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AvatarInitials(initials = user?.initials ?: "?", size = 60.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    user?.name?.takeIf { it.isNotBlank() } ?: "Estudante",
                    style = MaterialTheme.typography.titleLarge,
                    color = ClaramenteColors.Ink,
                )
                user?.email?.takeIf { it.isNotBlank() }?.let { email ->
                    Text(
                        email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ClaramenteColors.TextSecondary,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                user?.role?.let { role ->
                    RoleBadge(label = role.label, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}
