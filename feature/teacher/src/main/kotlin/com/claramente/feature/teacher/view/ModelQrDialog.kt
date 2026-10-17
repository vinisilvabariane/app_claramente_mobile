package com.claramente.feature.teacher.view

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.domain.policy.ModelQrPolicy
import com.claramente.core.model.classroom.Model3D
import com.claramente.feature.teacher.component.QrCodeImage
import com.claramente.feature.teacher.helper.QrSheetPrinter
import com.claramente.feature.teacher.policy.QrMatrixPolicy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ModelQrDialog(model: Model3D, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val content = remember(model.id) { ModelQrPolicy.content(model.id) }
    val matrix = remember(content) { QrMatrixPolicy.matrix(content) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(model.title) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                QrCodeImage(matrix = matrix, modifier = Modifier.fillMaxWidth())
                Text(
                    "Imprima ou projete este código. No modo \"QR code\", o aluno aponta a câmera para ele e o modelo aparece.",
                    style = MaterialTheme.typography.bodySmall,
                    color = ClaramenteColors.TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { QrSheetPrinter.print(context, model.title, content, matrix) }) { Text("Imprimir") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Fechar") } },
        containerColor = ClaramenteColors.White,
    )
}
