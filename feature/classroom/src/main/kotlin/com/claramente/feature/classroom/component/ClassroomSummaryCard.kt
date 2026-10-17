package com.claramente.feature.classroom.component

import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import com.claramente.core.model.classroom.Classroom
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun ClassroomSummaryCard(classroom: Classroom) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ClaramenteColors.Ink, RoundedCornerShape(ClaramenteRadius.Xl))
            .padding(20.dp),
    ) {
        Eyebrow(text = classroom.subject ?: "Turma", color = ClaramenteColors.White.copy(alpha = 0.6f))
        Text(
            classroom.name,
            style = MaterialTheme.typography.headlineSmall,
            color = ClaramenteColors.White,
            modifier = Modifier.padding(top = 6.dp),
        )
        Text(
            "Prof. ${classroom.teacherName}",
            style = MaterialTheme.typography.bodyMedium,
            color = ClaramenteColors.PrimaryMist,
            modifier = Modifier.padding(top = 2.dp),
        )
        Text(
            when (classroom.modelCount) {
                0 -> "Nenhum modelo liberado ainda"
                1 -> "1 modelo 3D liberado"
                else -> "${classroom.modelCount} modelos 3D liberados"
            },
            style = MaterialTheme.typography.bodySmall,
            color = ClaramenteColors.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 14.dp),
        )
    }
}
