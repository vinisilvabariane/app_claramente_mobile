package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.model.classroom.Classroom
import androidx.compose.foundation.layout.Column
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
internal fun TeacherClassroomCard(classroom: Classroom, onOpen: () -> Unit) {
    BrandCard(modifier = Modifier.fillMaxWidth(), onClick = onOpen) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow(text = classroom.subject ?: "Turma")
                Text(
                    classroom.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = ClaramenteColors.Ink,
                    modifier = Modifier.padding(top = 2.dp),
                )
                Text(
                    "${classroom.studentCount} aluno(s) · ${classroom.modelCount} modelo(s)",
                    style = MaterialTheme.typography.bodySmall,
                    color = ClaramenteColors.TextSecondary,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            classroom.joinCode?.let {
                Text(it, style = MaterialTheme.typography.titleMedium, color = ClaramenteColors.Primary)
            }
        }
    }
}
