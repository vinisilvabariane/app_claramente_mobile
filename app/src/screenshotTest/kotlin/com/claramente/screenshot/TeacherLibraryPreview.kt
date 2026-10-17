package com.claramente.screenshot

import com.android.tools.screenshot.PreviewTest
import com.claramente.core.designsystem.theme.ClaramenteTheme
import com.claramente.core.model.classroom.DocumentInfo
import com.claramente.feature.teacher.state.TeacherLibraryActions
import com.claramente.feature.teacher.state.TeacherLibraryUiState
import com.claramente.feature.teacher.view.TeacherLibraryScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@PreviewTest
@Preview(showBackground = true, device = PreviewDevices.PHONE)
@Composable
fun TeacherLibraryPreview() {
    ClaramenteTheme {
        TeacherLibraryScreen(
            state = TeacherLibraryUiState(
                loading = false,
                models = listOf(PreviewData.cell, PreviewData.dna),
                file = DocumentInfo("content://x", "coracao.glb", 3_100_000),
                title = "Coração humano",
            ),
            actions = TeacherLibraryActions({}, {}, {}, {}, {}, {}, {}, {}),
        )
    }
}
