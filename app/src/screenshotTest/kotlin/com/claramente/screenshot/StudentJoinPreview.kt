package com.claramente.screenshot

import com.android.tools.screenshot.PreviewTest
import com.claramente.core.designsystem.theme.ClaramenteTheme
import com.claramente.feature.ar.view.ArExamplesSection
import com.claramente.feature.classroom.state.StudentClassroomActions
import com.claramente.feature.classroom.state.StudentClassroomUiState
import com.claramente.feature.classroom.view.StudentClassroomScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@PreviewTest
@Preview(showBackground = true, device = PreviewDevices.PHONE)
@Composable
fun StudentJoinPreview() {
    ClaramenteTheme {
        StudentClassroomScreen(
            state = StudentClassroomUiState(loading = false, code = "K7M2"),
            actions = StudentClassroomActions({}, {}, {}, {}, {}, {}),
            examples = { ArExamplesSection(onOpen = {}) },
        )
    }
}
