package com.claramente.feature.classroom.view

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.LoadingScreen
import com.claramente.core.designsystem.component.MessageBox
import com.claramente.core.designsystem.component.ScreenTitle
import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.component.SectionTitle
import com.claramente.core.designsystem.theme.ButtonTone
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.classroom.component.ClassroomModelCard
import com.claramente.feature.classroom.component.ClassroomPicker
import com.claramente.feature.classroom.component.ClassroomSummaryCard
import com.claramente.feature.classroom.component.JoinClassroomCard
import com.claramente.feature.classroom.state.StudentClassroomActions
import com.claramente.feature.classroom.state.StudentClassroomUiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StudentClassroomScreen(
    state: StudentClassroomUiState,
    actions: StudentClassroomActions,
    examples: @Composable () -> Unit = {},
) {
    if (state.loading) {
        LoadingScreen()
        return
    }
    var confirmLeave by remember { mutableStateOf(false) }
    val selected = state.selected

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
                .widthIn(max = 760.dp)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ScreenTitle(
                eyebrow = "Sala de aula",
                title = selected?.name ?: "Sua turma",
                subtitle = if (selected == null) "Entre na turma para ver os modelos 3D do seu professor." else null,
            )
            state.error?.let { MessageBox(message = it) }
            if (selected == null) {
                JoinClassroomCard(
                    title = "Entrar em uma turma",
                    code = state.code,
                    canJoin = state.canJoin,
                    joining = state.joining,
                    onCode = actions.onCode,
                    onJoin = actions.onJoin,
                )
                SecondaryButton(text = "Atualizar", onClick = actions.onRefresh, modifier = Modifier.fillMaxWidth())
                examples()
            } else {
                if (state.classrooms.size > 1) {
                    ClassroomPicker(classrooms = state.classrooms, selectedId = state.selectedId, onSelect = actions.onSelect)
                }
                ClassroomSummaryCard(classroom = selected)
                Column {
                    SectionTitle("Modelos da turma")
                    when {
                        state.modelsLoading && state.models.isEmpty() -> Text(
                            "Carregando modelos…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ClaramenteColors.TextSecondary,
                        )
                        state.models.isEmpty() -> BrandCard(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "O professor ainda não liberou modelos para esta turma.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ClaramenteColors.TextSecondary,
                            )
                        }
                        else -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            state.models.forEach { model ->
                                ClassroomModelCard(
                                    model = model,
                                    preparing = state.preparingId == model.id,
                                    progress = state.preparingProgress,
                                    enabled = state.preparingId == null,
                                    onOpen = { actions.onOpenModel(model) },
                                )
                            }
                        }
                    }
                }
                SecondaryButton(text = "Atualizar", onClick = actions.onRefresh, modifier = Modifier.fillMaxWidth())
                JoinClassroomCard(
                    title = "Entrar em outra turma",
                    code = state.code,
                    canJoin = state.canJoin,
                    joining = state.joining,
                    onCode = actions.onCode,
                    onJoin = actions.onJoin,
                )
                SecondaryButton(
                    text = "Sair desta turma",
                    onClick = { confirmLeave = true },
                    modifier = Modifier.fillMaxWidth(),
                    tone = ButtonTone.DANGER,
                    enabled = !state.leaving,
                )
                examples()
            }
        }
    }

    if (confirmLeave && selected != null) {
        AlertDialog(
            onDismissRequest = { confirmLeave = false },
            title = { Text("Sair da turma?") },
            text = { Text("Você deixa de ver os modelos de ${selected.name}. Para voltar, use o código da turma de novo.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmLeave = false
                    actions.onLeave()
                }) { Text("Sair", color = ClaramenteColors.ErrorText) }
            },
            dismissButton = { TextButton(onClick = { confirmLeave = false }) { Text("Cancelar") } },
            containerColor = ClaramenteColors.White,
        )
    }
}
