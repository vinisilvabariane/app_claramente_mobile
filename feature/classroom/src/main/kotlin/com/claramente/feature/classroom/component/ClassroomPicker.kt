package com.claramente.feature.classroom.component

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import com.claramente.core.model.classroom.Classroom
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
internal fun ClassroomPicker(classrooms: List<Classroom>, selectedId: String?, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        classrooms.forEach { classroom ->
            val selected = classroom.id == selectedId
            val shape = RoundedCornerShape(ClaramenteRadius.Pill)
            Text(
                classroom.name,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) ClaramenteColors.White else ClaramenteColors.Ink,
                modifier = Modifier
                    .background(if (selected) ClaramenteColors.Primary else ClaramenteColors.White, shape)
                    .border(1.dp, if (selected) ClaramenteColors.Primary else ClaramenteColors.Border, shape)
                    .selectable(
                        selected = selected,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        role = Role.Tab,
                        onClick = { onSelect(classroom.id) },
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            )
        }
    }
}
