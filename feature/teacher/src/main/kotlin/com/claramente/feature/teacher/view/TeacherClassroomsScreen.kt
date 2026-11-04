package com.claramente.feature.teacher.view

import com.claramente.core.designsystem.component.LoadingScreen
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.teacher.component.TeacherClassroomDetail
import com.claramente.feature.teacher.component.TeacherClassroomList
import com.claramente.feature.teacher.state.TeacherClassroomsActions
import com.claramente.feature.teacher.state.TeacherClassroomsUiState
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TeacherClassroomsScreen(state: TeacherClassroomsUiState, actions: TeacherClassroomsActions) {
    if (state.loading && state.classrooms.isEmpty()) {
        LoadingScreen()
        return
    }
    val selected = state.selected
    BackHandler(enabled = selected != null, onBack = actions.onBack)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClaramenteColors.Background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            if (selected == null) {
                TeacherClassroomList(state, actions)
            } else {
                TeacherClassroomDetail(selected, state, actions)
            }
        }
    }
}
