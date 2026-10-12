package com.claramente.navigation

enum class HomeTab(val route: String, val label: String) {
    HUB(Routes.HUB, "Início"),
    AR(Routes.AR, "Laboratório"),
    PROFILE(Routes.PROFILE, "Perfil");

    companion object {
        fun fromRoute(route: String?): HomeTab = entries.firstOrNull { it.route == route } ?: HUB
    }
}
