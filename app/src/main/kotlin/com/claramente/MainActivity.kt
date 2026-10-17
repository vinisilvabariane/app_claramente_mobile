package com.claramente

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteTheme
import com.claramente.feature.ar.viewmodel.ArExperienceViewModel
import com.claramente.feature.classroom.viewmodel.StudentClassroomViewModel
import com.claramente.feature.login.viewmodel.LoginViewModel
import com.claramente.feature.session.viewmodel.SessionViewModel
import com.claramente.feature.teacher.viewmodel.TeacherClassroomsViewModel
import com.claramente.feature.teacher.viewmodel.TeacherLibraryViewModel
import com.claramente.navigation.AppNavigation
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : ComponentActivity() {
    private val container get() = (application as ClaramenteApplication).container

    private val sessionViewModel: SessionViewModel by viewModels {
        viewModelFactory { initializer { with(container) { SessionViewModel(sessions, restoreSession, logout) } } }
    }

    private val loginViewModel: LoginViewModel by viewModels {
        viewModelFactory { initializer { with(container) { LoginViewModel(login, mockLogin, mockAuthEnabled) } } }
    }

    private val arExperienceViewModel: ArExperienceViewModel by viewModels {
        viewModelFactory { initializer { with(container) { ArExperienceViewModel(arWebUrl) } } }
    }

    private val studentClassroomViewModel: StudentClassroomViewModel by viewModels {
        viewModelFactory {
            initializer {
                with(container) {
                    StudentClassroomViewModel(listClassrooms, joinClassroom, leaveClassroom, listClassroomModels, prepareModel)
                }
            }
        }
    }

    private val teacherClassroomsViewModel: TeacherClassroomsViewModel by viewModels {
        viewModelFactory {
            initializer {
                with(container) {
                    TeacherClassroomsViewModel(
                        listClassrooms,
                        createClassroom,
                        deleteClassroom,
                        regenerateJoinCode,
                        listStudents,
                        removeStudent,
                        listLibrary,
                        setModelPublished,
                    )
                }
            }
        }
    }

    private val teacherLibraryViewModel: TeacherLibraryViewModel by viewModels {
        viewModelFactory {
            initializer {
                with(container) { TeacherLibraryViewModel(listLibrary, inspectModelFile, uploadModel, deleteModel, prepareModel) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val transparent = Color.Transparent.toArgb()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(transparent, transparent),
            navigationBarStyle = SystemBarStyle.light(transparent, ClaramenteColors.Overlay.toArgb()),
        )
        super.onCreate(savedInstanceState)
        setContent {
            ClaramenteTheme {
                AppNavigation(
                    sessionViewModel = sessionViewModel,
                    loginViewModel = loginViewModel,
                    arExperienceViewModel = arExperienceViewModel,
                    studentClassroomViewModel = studentClassroomViewModel,
                    teacherClassroomsViewModel = teacherClassroomsViewModel,
                    teacherLibraryViewModel = teacherLibraryViewModel,
                    arWebUrl = container.arWebUrl,
                    onOpenUrl = ::openUrl,
                )
            }
        }
    }

    private fun openUrl(url: String) {
        val uri = url.toUri()
        runCatching {
            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(ClaramenteColors.Ink.toArgb())
                        .build(),
                )
                .setShowTitle(true)
                .build()
                .launchUrl(this, uri)
        }.onFailure {
            runCatching { startActivity(Intent(Intent.ACTION_VIEW, uri)) }
        }
    }
}
