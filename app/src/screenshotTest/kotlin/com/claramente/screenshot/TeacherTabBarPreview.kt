package com.claramente.screenshot

import com.android.tools.screenshot.PreviewTest
import com.claramente.core.designsystem.theme.ClaramenteTheme
import com.claramente.navigation.HomeTab
import com.claramente.navigation.HomeTabBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@PreviewTest
@Preview(showBackground = true, widthDp = 411)
@Composable
fun TeacherTabBarPreview() {
    ClaramenteTheme {
        HomeTabBar(current = HomeTab.TEACHER_CLASSROOMS, onSelect = {}, tabs = HomeTab.teacher)
    }
}
