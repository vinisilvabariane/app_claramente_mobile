package com.claramente.screenshot

import com.android.tools.screenshot.PreviewTest
import com.claramente.core.designsystem.theme.ClaramenteTheme
import com.claramente.feature.classroom.state.StudentClassroomActions
import com.claramente.feature.classroom.state.StudentClassroomUiState
import com.claramente.feature.classroom.view.StudentClassroomScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@PreviewTest
@Preview(showBackground = true, device = PreviewDevices.PHONE)
@Composable
fun StudentClassroomPreview() {
    ClaramenteTheme {
        StudentClassroomScreen(
            state = StudentClassroomUiState(
                loading = false,
                classrooms = listOf(PreviewData.classroom.copy(joinCode = null)),
                selectedId = PreviewData.classroom.id,
                models = listOf(PreviewData.cell),
                preparingId = PreviewData.cell.id,
                preparingProgress = 0.4f,
            ),
            actions = StudentClassroomActions({}, {}, {}, {}, {}, {}),
        )
    }
}
