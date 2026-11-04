package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.MessageBox
import com.claramente.core.designsystem.component.ScreenTitle
import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.component.SectionTitle
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.teacher.state.TeacherClassroomsActions
import com.claramente.feature.teacher.state.TeacherClassroomsUiState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TeacherClassroomList(state: TeacherClassroomsUiState, actions: TeacherClassroomsActions) {
    ScreenTitle(eyebrow = "Painel do professor", title = "Suas turmas", subtitle = "Crie turmas e passe o código para os alunos entrarem.")
    state.error?.let { MessageBox(message = it) }
    CreateClassroomCard(
        name = state.name,
        subject = state.subject,
        canCreate = state.canCreate,
        creating = state.creating,
        onName = actions.onName,
        onSubject = actions.onSubject,
        onCreate = actions.onCreate,
    )
    Column {
        SectionTitle("Turmas")
        if (state.classrooms.isEmpty()) {
            BrandCard(modifier = Modifier.fillMaxWidth()) {
                Text("Você ainda não criou turmas.", style = MaterialTheme.typography.bodyMedium, color = ClaramenteColors.TextSecondary)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.classrooms.forEach { classroom ->
                    TeacherClassroomCard(classroom = classroom, onOpen = { actions.onOpen(classroom.id) })
                }
            }
        }
    }
    SecondaryButton(text = "Atualizar", onClick = actions.onRefresh, modifier = Modifier.fillMaxWidth())
}
