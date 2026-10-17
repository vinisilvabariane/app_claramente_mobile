package com.claramente.feature.teacher.view

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.MessageBox
import com.claramente.core.designsystem.component.ScreenTitle
import com.claramente.core.designsystem.component.SectionTitle
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.teacher.component.LibraryModelCard
import com.claramente.feature.teacher.component.UploadModelCard
import com.claramente.feature.teacher.state.TeacherLibraryActions
import com.claramente.feature.teacher.state.TeacherLibraryUiState
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
fun TeacherLibraryScreen(
    state: TeacherLibraryUiState,
    actions: TeacherLibraryActions,
    examples: @Composable () -> Unit = {},
) {
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { actions.onPickFile(it.toString()) }
    }
    var confirmDeleteId by remember { mutableStateOf<String?>(null) }
    var qrModelId by remember { mutableStateOf<String?>(null) }

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
                eyebrow = "Painel do professor",
                title = "Biblioteca 3D",
                subtitle = "Envie modelos e depois libere cada um para as turmas.",
            )
            state.error?.let { MessageBox(message = it) }
            state.notice?.let { MessageBox(message = it, isError = false) }
            UploadModelCard(
                state = state,
                actions = actions,
                onChooseFile = { picker.launch(arrayOf("model/gltf-binary", "application/octet-stream", "*/*")) },
            )
            Column {
                SectionTitle("Seus modelos")
                when {
                    state.loading -> Text("Carregando…", style = MaterialTheme.typography.bodyMedium, color = ClaramenteColors.TextSecondary)
                    state.models.isEmpty() -> BrandCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Nenhum modelo enviado ainda.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ClaramenteColors.TextSecondary,
                        )
                    }
                    else -> Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        state.models.forEach { model ->
                            LibraryModelCard(
                                model = model,
                                preparing = state.preparingId == model.id,
                                progress = state.preparingProgress,
                                deleting = state.deletingId == model.id,
                                busy = state.preparingId != null || state.deletingId != null,
                                onPreview = { actions.onPreview(model) },
                                onDelete = { confirmDeleteId = model.id },
                                onShowQr = { qrModelId = model.id },
                            )
                        }
                    }
                }
            }
            examples()
        }
    }

    state.models.firstOrNull { it.id == qrModelId }?.let { model ->
        ModelQrDialog(model = model, onDismiss = { qrModelId = null })
    }

    val pending = state.models.firstOrNull { it.id == confirmDeleteId }
    if (pending != null) {
        AlertDialog(
            onDismissRequest = { confirmDeleteId = null },
            title = { Text("Excluir modelo?") },
            text = { Text("\"${pending.title}\" sai da biblioteca e de todas as turmas em que estava liberado.") },
            confirmButton = {
                TextButton(onClick = {
                    confirmDeleteId = null
                    actions.onDelete(pending.id)
                }) { Text("Excluir", color = ClaramenteColors.ErrorText) }
            },
            dismissButton = { TextButton(onClick = { confirmDeleteId = null }) { Text("Cancelar") } },
            containerColor = ClaramenteColors.White,
        )
    }
}
