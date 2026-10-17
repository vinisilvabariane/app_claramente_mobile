package com.claramente.feature.teacher.component

import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.BrandTextField
import com.claramente.core.designsystem.component.PrimaryButton
import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
internal fun CreateClassroomCard(
    name: String,
    subject: String,
    canCreate: Boolean,
    creating: Boolean,
    onName: (String) -> Unit,
    onSubject: (String) -> Unit,
    onCreate: () -> Unit,
) {
    BrandCard(modifier = Modifier.fillMaxWidth()) {
        Text("Nova turma", style = MaterialTheme.typography.titleSmall, color = ClaramenteColors.Ink)
        BrandTextField(
            value = name,
            onValueChange = onName,
            placeholder = "Ex.: 1º ano B",
            label = "Nome da turma",
            enabled = !creating,
            modifier = Modifier.padding(top = 12.dp),
        )
        BrandTextField(
            value = subject,
            onValueChange = onSubject,
            placeholder = "Ex.: Biologia",
            label = "Disciplina (opcional)",
            enabled = !creating,
            imeAction = ImeAction.Done,
            onImeAction = { if (canCreate) onCreate() },
            modifier = Modifier.padding(top = 12.dp),
        )
        PrimaryButton(
            text = "Criar turma",
            onClick = onCreate,
            enabled = canCreate,
            loading = creating,
            modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
        )
    }
}
