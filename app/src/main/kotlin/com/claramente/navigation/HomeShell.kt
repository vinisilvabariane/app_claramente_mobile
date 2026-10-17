package com.claramente.navigation

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.model.auth.UserRole
import com.claramente.core.model.classroom.Model3D
import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ArModel
import com.claramente.feature.ar.state.ArModels
import com.claramente.feature.ar.policy.ArWebUrlPolicy
import com.claramente.feature.ar.view.ArExamplesSection
import com.claramente.feature.ar.view.ArLaunchDialog
import com.claramente.feature.classroom.state.StudentClassroomActions
import com.claramente.feature.classroom.view.StudentClassroomScreen
import com.claramente.feature.classroom.viewmodel.StudentClassroomViewModel
import com.claramente.feature.hub.state.HubFeed
import com.claramente.feature.hub.view.HubScreen
import com.claramente.feature.profile.view.ProfileScreen
import com.claramente.feature.session.state.SessionUiState
import com.claramente.feature.teacher.state.TeacherClassroomsActions
import com.claramente.feature.teacher.state.TeacherLibraryActions
import com.claramente.feature.teacher.view.TeacherClassroomsScreen
import com.claramente.feature.teacher.view.TeacherLibraryScreen
import com.claramente.feature.teacher.viewmodel.TeacherClassroomsViewModel
import com.claramente.feature.teacher.viewmodel.TeacherLibraryViewModel
import com.claramente.mapper.ClassroomArMapper
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeShell(
    session: SessionUiState,
    arWebUrl: String?,
    studentClassroomViewModel: StudentClassroomViewModel,
    teacherClassroomsViewModel: TeacherClassroomsViewModel,
    teacherLibraryViewModel: TeacherLibraryViewModel,
    onLogout: () -> Unit,
    onOpenUrl: (String) -> Unit,
    onStartAr: (ArMode, ArModel, List<ArModel>) -> Unit,
) {
    val tabs = HomeTab.forRole(session.user?.role ?: UserRole.STUDENT)
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentTab = HomeTab.fromRoute(backStackEntry?.destination?.route, tabs)
    val selectTab: (HomeTab) -> Unit = { tab ->
        navController.navigate(tab.route) {
            launchSingleTop = true
            restoreState = true
            popUpTo(tabs.first().route) { saveState = true }
        }
    }
    var launching by remember { mutableStateOf<ArModel?>(null) }
    val openInAr: (Model3D, String) -> Unit = { model, uri -> launching = ClassroomArMapper.toArModel(model, uri) }
    val examples: @Composable () -> Unit = { ArExamplesSection(onOpen = { launching = it }) }

    Scaffold(
        containerColor = ClaramenteColors.Background,
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
        bottomBar = { HomeTabBar(current = currentTab, onSelect = selectTab, tabs = tabs) },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = tabs.first().route,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (HomeTab.HUB in tabs) {
                composable(Routes.HUB) {
                    HubScreen(
                        user = session.user,
                        stats = HubFeed.stats,
                        feed = HubFeed.items,
                        onContinue = { selectTab(HomeTab.CLASSROOM) },
                    )
                }
            }
            if (HomeTab.CLASSROOM in tabs) {
                composable(Routes.CLASSROOM) {
                    LaunchedEffect(Unit) { studentClassroomViewModel.load() }
                    val state by studentClassroomViewModel.state.collectAsState()
                    StudentClassroomScreen(
                        examples = examples,
                        state = state,
                        actions = StudentClassroomActions(
                            onRefresh = studentClassroomViewModel::load,
                            onCode = studentClassroomViewModel::onCode,
                            onJoin = studentClassroomViewModel::join,
                            onSelect = studentClassroomViewModel::select,
                            onOpenModel = { model -> studentClassroomViewModel.openModel(model, openInAr) },
                            onLeave = studentClassroomViewModel::leave,
                        ),
                    )
                }
            }
            if (HomeTab.TEACHER_CLASSROOMS in tabs) {
                composable(Routes.TEACHER_CLASSROOMS) {
                    LaunchedEffect(Unit) { teacherClassroomsViewModel.load() }
                    val state by teacherClassroomsViewModel.state.collectAsState()
                    TeacherClassroomsScreen(
                        state = state,
                        actions = TeacherClassroomsActions(
                            onRefresh = teacherClassroomsViewModel::load,
                            onName = teacherClassroomsViewModel::onName,
                            onSubject = teacherClassroomsViewModel::onSubject,
                            onCreate = teacherClassroomsViewModel::create,
                            onOpen = teacherClassroomsViewModel::open,
                            onBack = teacherClassroomsViewModel::back,
                            onRegenerateCode = teacherClassroomsViewModel::regenerateCode,
                            onRemoveStudent = teacherClassroomsViewModel::removeStudent,
                            onTogglePublished = teacherClassroomsViewModel::togglePublished,
                            onDelete = teacherClassroomsViewModel::delete,
                            onGoToLibrary = { selectTab(HomeTab.LIBRARY) },
                        ),
                    )
                }
            }
            if (HomeTab.LIBRARY in tabs) {
                composable(Routes.LIBRARY) {
                    LaunchedEffect(Unit) { teacherLibraryViewModel.load() }
                    val state by teacherLibraryViewModel.state.collectAsState()
                    TeacherLibraryScreen(
                        examples = examples,
                        state = state,
                        actions = TeacherLibraryActions(
                            onRefresh = teacherLibraryViewModel::load,
                            onPickFile = teacherLibraryViewModel::pickFile,
                            onTitle = teacherLibraryViewModel::onTitle,
                            onSubject = teacherLibraryViewModel::onSubject,
                            onDescription = teacherLibraryViewModel::onDescription,
                            onUpload = teacherLibraryViewModel::upload,
                            onDelete = teacherLibraryViewModel::delete,
                            onPreview = { model -> teacherLibraryViewModel.preview(model, openInAr) },
                        ),
                    )
                }
            }
            composable(Routes.PROFILE) {
                ProfileScreen(user = session.user, loggingOut = session.loggingOut, onLogout = onLogout)
            }
        }
    }

    launching?.let { model ->
        val webUrl = ArWebUrlPolicy.url(arWebUrl, model)
        ArLaunchDialog(
            model = model,
            webUrl = webUrl,
            onStart = { mode ->
                launching = null
                onStartAr(mode, model, (listOf(model) + ArModels.all).distinctBy { it.id })
            },
            onOpenWeb = { webUrl?.let(onOpenUrl) },
            onDismiss = { launching = null },
        )
    }
}
