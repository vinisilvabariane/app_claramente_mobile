package com.claramente.feature.login.view

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.login.component.LoginCard
import com.claramente.feature.login.component.LoginHero
import com.claramente.feature.login.state.LoginActions
import com.claramente.feature.login.state.LoginUiState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
fun LoginScreen(state: LoginUiState, actions: LoginActions) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ClaramenteColors.Background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 460.dp)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            LoginHero()
            LoginCard(state = state, actions = actions, modifier = Modifier.fillMaxWidth())
        }
    }
}
