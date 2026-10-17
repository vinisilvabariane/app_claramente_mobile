package com.claramente.screenshot

import com.android.tools.screenshot.PreviewTest
import com.claramente.core.designsystem.theme.ClaramenteTheme
import com.claramente.feature.teacher.state.TeacherClassroomsActions
import com.claramente.feature.teacher.state.TeacherClassroomsUiState
import com.claramente.feature.teacher.view.TeacherClassroomsScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@PreviewTest
@Preview(showBackground = true, device = PreviewDevices.PHONE)
@Composable
fun TeacherClassroomPreview() {
    ClaramenteTheme {
        TeacherClassroomsScreen(
            state = TeacherClassroomsUiState(
                loading = false,
                classrooms = listOf(PreviewData.classroom),
                selectedId = PreviewData.classroom.id,
                students = PreviewData.students,
                library = listOf(PreviewData.cell, PreviewData.dna),
            ),
            actions = TeacherClassroomsActions({}, {}, {}, {}, {}, {}, {}, {}, { _, _ -> }, {}, {}),
        )
    }
}
