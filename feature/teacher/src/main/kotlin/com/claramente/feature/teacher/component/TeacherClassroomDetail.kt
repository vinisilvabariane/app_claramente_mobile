package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.MessageBox
import com.claramente.core.designsystem.component.ScreenTitle
import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.component.SectionTitle
import com.claramente.core.designsystem.theme.ButtonTone
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.model.classroom.Classroom
import com.claramente.feature.teacher.state.TeacherClassroomsActions
import com.claramente.feature.teacher.state.TeacherClassroomsUiState
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
internal fun TeacherClassroomDetail(classroom: Classroom, state: TeacherClassroomsUiState, actions: TeacherClassroomsActions) {
    val context = LocalContext.current
    var confirmDelete by remember { mutableStateOf(false) }

    TextButton(onClick = actions.onBack, contentPadding = PaddingValues(0.dp)) {
        Text("‹ Turmas", color = ClaramenteColors.Primary)
    }
    ScreenTitle(eyebrow = classroom.subject ?: "Turma", title = classroom.name)
    state.error?.let { MessageBox(message = it) }
    classroom.joinCode?.let { code ->
        JoinCodeCard(
            code = code,
            regenerating = state.regenerating,
            onShare = {
                val message = "Entre na turma ${classroom.name} no app Claramente com o código $code"
                val intent = Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, message)
                context.startActivity(Intent.createChooser(intent, "Compartilhar código"))
            },
            onRegenerate = actions.onRegenerateCode,
        )
    }
    Column {
        SectionTitle("Modelos liberados")
        when {
            state.detailLoading && state.library.isEmpty() ->
                Text("Carregando…", style = MaterialTheme.typography.bodyMedium, color = ClaramenteColors.TextSecondary)
            state.library.isEmpty() -> BrandCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Sua biblioteca está vazia. Envie um modelo .glb para liberar nesta turma.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ClaramenteColors.TextSecondary,
                )
                SecondaryButton(
                    text = "Ir para a biblioteca",
                    onClick = actions.onGoToLibrary,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                )
            }
            else -> BrandCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)) {
                state.library.forEachIndexed { index, model ->
                    if (index > 0) HorizontalDivider(color = ClaramenteColors.Border)
                    PublishModelRow(
                        model = model,
                        published = state.isPublished(model),
                        busy = model.id in state.busyModelIds,
                        onToggle = { published -> actions.onTogglePublished(model.id, published) },
                    )
                }
            }
        }
    }
    Column {
        SectionTitle("Alunos (${state.students.size})")
        if (state.students.isEmpty()) {
            BrandCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    if (state.detailLoading) "Carregando…" else "Nenhum aluno entrou ainda. Compartilhe o código da turma.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ClaramenteColors.TextSecondary,
                )
            }
        } else {
            BrandCard(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)) {
                state.students.forEachIndexed { index, student ->
                    if (index > 0) HorizontalDivider(color = ClaramenteColors.Border)
                    StudentRow(
                        student = student,
                        removing = state.removingStudentId == student.id,
                        onRemove = { actions.onRemoveStudent(student.id) },
                    )
                }
            }
        }
    }
    SecondaryButton(
        text = if (state.deleting) "Excluindo…" else "Excluir turma",
        onClick = { confirmDelete = true },
        modifier = Modifier.fillMaxWidth(),
        tone = ButtonTone.DANGER,
        enabled = !state.deleting,
    )

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Excluir ${classroom.name}?") },
            text = { Text("Os alunos saem da turma e perdem acesso aos modelos liberados nela. Os modelos continuam na sua biblioteca.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmDelete = false
                    actions.onDelete()
                }) { Text("Excluir", color = ClaramenteColors.ErrorText) }
            },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancelar") } },
            containerColor = ClaramenteColors.White,
        )
    }
}
