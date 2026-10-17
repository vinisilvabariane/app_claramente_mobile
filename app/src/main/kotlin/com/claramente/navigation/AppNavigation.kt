package com.claramente.navigation

import com.claramente.core.designsystem.component.LoadingScreen
import com.claramente.feature.ar.state.ArExperienceActions
import com.claramente.feature.ar.view.ArExperienceScreen
import com.claramente.feature.ar.viewmodel.ArExperienceViewModel
import com.claramente.feature.classroom.viewmodel.StudentClassroomViewModel
import com.claramente.feature.login.state.LoginActions
import com.claramente.feature.login.view.LoginScreen
import com.claramente.feature.login.viewmodel.LoginViewModel
import com.claramente.feature.session.viewmodel.SessionViewModel
import com.claramente.feature.teacher.viewmodel.TeacherClassroomsViewModel
import com.claramente.feature.teacher.viewmodel.TeacherLibraryViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    sessionViewModel: SessionViewModel,
    loginViewModel: LoginViewModel,
    arExperienceViewModel: ArExperienceViewModel,
    studentClassroomViewModel: StudentClassroomViewModel,
    teacherClassroomsViewModel: TeacherClassroomsViewModel,
    teacherLibraryViewModel: TeacherLibraryViewModel,
    arWebUrl: String?,
    onOpenUrl: (String) -> Unit,
) {
    val session by sessionViewModel.state.collectAsState()
    val userId = session.user?.id

    LaunchedEffect(userId) {
        teacherClassroomsViewModel.clear()
        teacherLibraryViewModel.clear()
    }

    if (!session.checked) {
        LoadingScreen()
    } else {
        val navController = rememberNavController()
        val signedIn = session.user != null
        val target = if (signedIn) Routes.HOME else Routes.LOGIN
        val startDestination = remember { target }

        LaunchedEffect(signedIn) {
            if (navController.currentDestination?.route != target) {
                navController.navigate(target) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        NavHost(navController = navController, startDestination = startDestination) {
            composable(Routes.LOGIN) {
                val state by loginViewModel.state.collectAsState()
                LoginScreen(
                    state = state,
                    actions = LoginActions(
                        onEmail = loginViewModel::onEmail,
                        onPassword = loginViewModel::onPassword,
                        onSubmit = loginViewModel::submit,
                        onMockLogin = loginViewModel::mockLogin,
                    ),
                )
            }
            composable(Routes.HOME) {
                HomeShell(
                    session = session,
                    arWebUrl = arWebUrl,
                    studentClassroomViewModel = studentClassroomViewModel,
                    teacherClassroomsViewModel = teacherClassroomsViewModel,
                    teacherLibraryViewModel = teacherLibraryViewModel,
                    onLogout = sessionViewModel::logout,
                    onOpenUrl = onOpenUrl,
                    onStartAr = { mode, model, available ->
                        arExperienceViewModel.start(mode, model, available)
                        navController.navigate(Routes.AR_EXPERIENCE)
                    },
                )
            }
            composable(Routes.AR_EXPERIENCE) {
                val experience by arExperienceViewModel.state.collectAsState()
                ArExperienceScreen(
                    state = experience,
                    actions = ArExperienceActions(
                        onBack = { navController.popBackStack() },
                        onSupport = arExperienceViewModel::onSupport,
                        onCameraState = arExperienceViewModel::onCameraState,
                        onCameraRequested = arExperienceViewModel::onCameraRequested,
                        onFound = arExperienceViewModel::onFound,
                        onQr = arExperienceViewModel::onQr,
                        onRescan = arExperienceViewModel::onRescan,
                        onSessionFailed = arExperienceViewModel::onSessionFailed,
                        onRetry = arExperienceViewModel::onRetry,
                        onOpenWeb = { experience.webUrl?.let(onOpenUrl) },
                    ),
                )
            }
        }
    }
}
