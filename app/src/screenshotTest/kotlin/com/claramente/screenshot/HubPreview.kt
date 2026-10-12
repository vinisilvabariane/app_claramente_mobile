package com.claramente.screenshot

import com.android.tools.screenshot.PreviewTest
import com.claramente.core.designsystem.theme.ClaramenteTheme
import com.claramente.feature.hub.state.HubFeed
import com.claramente.feature.hub.view.HubScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@PreviewTest
@Preview(showBackground = true, device = PreviewDevices.PHONE)
@Composable
fun HubPreview() {
    ClaramenteTheme {
        HubScreen(user = null, stats = HubFeed.stats, feed = HubFeed.items, onContinue = {})
    }
}
