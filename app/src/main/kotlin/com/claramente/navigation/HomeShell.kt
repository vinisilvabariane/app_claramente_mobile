package com.claramente.navigation

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.ar.state.ArActions
import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ArModel
import com.claramente.feature.ar.view.ArScreen
import com.claramente.feature.ar.viewmodel.ArViewModel
import com.claramente.feature.hub.state.HubFeed
import com.claramente.feature.hub.view.HubScreen
import com.claramente.feature.profile.view.ProfileScreen
import com.claramente.feature.session.state.SessionUiState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeShell(
    session: SessionUiState,
    arViewModel: ArViewModel,
    onLogout: () -> Unit,
    onOpenUrl: (String) -> Unit,
    onStartAr: (ArMode, ArModel) -> Unit,
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentTab = HomeTab.fromRoute(backStackEntry?.destination?.route)

    Scaffold(
        containerColor = ClaramenteColors.Background,
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
        bottomBar = {
            HomeTabBar(
                current = currentTab,
                onSelect = { tab ->
                    navController.navigate(tab.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(Routes.HUB) { saveState = true }
                    }
                },
            )
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HUB,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            composable(Routes.HUB) {
                HubScreen(user = session.user, stats = HubFeed.stats, feed = HubFeed.items, onContinue = {})
            }
            composable(Routes.PROFILE) {
                ProfileScreen(user = session.user, loggingOut = session.loggingOut, onLogout = onLogout)
            }
            composable(Routes.AR) {
                val state by arViewModel.state.collectAsState()
                ArScreen(
                    state = state,
                    actions = ArActions(
                        onSelect = arViewModel::select,
                        onSelectMode = arViewModel::selectMode,
                        onStart = { onStartAr(state.mode, state.selected) },
                        onOpenWeb = { state.experienceUrl?.let(onOpenUrl) },
                    ),
                )
            }
        }
    }
}
