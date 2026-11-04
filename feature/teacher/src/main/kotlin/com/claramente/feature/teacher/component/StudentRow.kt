package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.component.AvatarInitials
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.model.classroom.ClassroomStudent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun StudentRow(student: ClassroomStudent, removing: Boolean, onRemove: () -> Unit) {
    val parts = student.name.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    val initials = (parts.firstOrNull()?.take(1).orEmpty() + if (parts.size > 1) parts.last().take(1) else "").uppercase()
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AvatarInitials(initials = initials.ifEmpty { "?" }, size = 36.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(student.name, style = MaterialTheme.typography.bodyMedium, color = ClaramenteColors.Ink)
            Text(student.email, style = MaterialTheme.typography.bodySmall, color = ClaramenteColors.TextSecondary)
        }
        TextButton(onClick = onRemove, enabled = !removing) {
            Text(if (removing) "Removendo…" else "Remover", color = ClaramenteColors.ErrorText)
        }
    }
}
