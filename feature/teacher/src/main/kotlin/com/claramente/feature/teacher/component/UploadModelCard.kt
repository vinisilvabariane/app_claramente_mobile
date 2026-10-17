package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.BrandTextField
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.component.SecondaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.domain.policy.FileSizePolicy
import com.claramente.core.domain.policy.ModelFilePolicy
import com.claramente.feature.teacher.state.TeacherLibraryActions
import com.claramente.feature.teacher.state.TeacherLibraryUiState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun UploadModelCard(state: TeacherLibraryUiState, actions: TeacherLibraryActions, onChooseFile: () -> Unit) {
    val editable = !state.uploading
    BrandCard(modifier = Modifier.fillMaxWidth()) {
        Text("Enviar modelo 3D", style = MaterialTheme.typography.titleSmall, color = ClaramenteColors.Ink)
        Text(
            "Formato .glb (glTF 2.0 binário), até ${FileSizePolicy.label(ModelFilePolicy.MAX_BYTES)}. " +
                "Prefira texturas de até 2K para abrir bem em celulares simples.",
            style = MaterialTheme.typography.bodySmall,
            color = ClaramenteColors.TextSecondary,
            modifier = Modifier.padding(top = 2.dp, bottom = 12.dp),
        )
        SecondaryButton(
            text = when {
                state.inspecting -> "Verificando arquivo…"
                state.file != null -> "Trocar arquivo"
                else -> "Escolher arquivo .glb"
            },
            onClick = onChooseFile,
            enabled = editable && !state.inspecting,
            modifier = Modifier.fillMaxWidth(),
        )
        state.file?.let { file ->
            Text(
                "${file.name} · ${FileSizePolicy.label(file.sizeBytes)}",
                style = MaterialTheme.typography.bodySmall,
                color = ClaramenteColors.Success,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
        BrandTextField(
            value = state.title,
            onValueChange = actions.onTitle,
            placeholder = "Ex.: Célula animal",
            label = "Título",
            enabled = editable,
            modifier = Modifier.padding(top = 12.dp),
        )
        BrandTextField(
            value = state.subject,
            onValueChange = actions.onSubject,
            placeholder = "Ex.: Biologia",
            label = "Disciplina (opcional)",
            enabled = editable,
            modifier = Modifier.padding(top = 12.dp),
        )
        BrandTextField(
            value = state.description,
            onValueChange = actions.onDescription,
            placeholder = "O que o aluno deve observar",
            label = "Descrição (opcional)",
            enabled = editable,
            modifier = Modifier.padding(top = 12.dp),
        )
        PrimaryButton(
            text = if (state.uploading) "Enviando…" else "Enviar modelo",
            onClick = actions.onUpload,
            enabled = state.canUpload,
            loading = state.uploading,
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
        )
    }
}
