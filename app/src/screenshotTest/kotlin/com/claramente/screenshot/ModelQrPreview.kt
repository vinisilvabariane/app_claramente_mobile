package com.claramente.screenshot

import com.android.tools.screenshot.PreviewTest
import com.claramente.core.designsystem.theme.ClaramenteTheme
import com.claramente.feature.teacher.view.ModelQrDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@PreviewTest
@Preview(showBackground = true, device = PreviewDevices.PHONE)
@Composable
fun ModelQrPreview() {
    ClaramenteTheme {
        ModelQrDialog(model = PreviewData.cell.copy(id = "3f2b8c1e-9a4d-4e7b-8c21-5d6f7a8b9c0d"), onDismiss = {})
    }
}
