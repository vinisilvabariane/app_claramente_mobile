package com.claramente.navigation

import com.claramente.core.model.auth.UserRole

enum class HomeTab(val route: String, val label: String) {
    HUB(Routes.HUB, "Início"),
    CLASSROOM(Routes.CLASSROOM, "Turma"),
    TEACHER_CLASSROOMS(Routes.TEACHER_CLASSROOMS, "Turmas"),
    LIBRARY(Routes.LIBRARY, "Biblioteca"),
    PROFILE(Routes.PROFILE, "Perfil");

    companion object {
        val student: List<HomeTab> = listOf(HUB, CLASSROOM, PROFILE)
        val teacher: List<HomeTab> = listOf(TEACHER_CLASSROOMS, LIBRARY, PROFILE)

        fun forRole(role: UserRole): List<HomeTab> = if (role == UserRole.TEACHER) teacher else student

        fun fromRoute(route: String?, tabs: List<HomeTab>): HomeTab = tabs.firstOrNull { it.route == route } ?: tabs.first()
    }
}
