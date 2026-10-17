package com.claramente.feature.ar.view

import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.feature.ar.component.ArLaunchPanel
import com.claramente.feature.ar.state.ArMode
import com.claramente.feature.ar.state.ArModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArLaunchDialog(model: ArModel, webUrl: String?, onStart: (ArMode) -> Unit, onOpenWeb: () -> Unit, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = ClaramenteColors.White,
    ) {
        ArLaunchPanel(model = model, webUrl = webUrl, onStart = onStart, onOpenWeb = onOpenWeb)
    }
}
