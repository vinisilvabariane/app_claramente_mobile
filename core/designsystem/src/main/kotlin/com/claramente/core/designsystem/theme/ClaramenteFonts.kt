package com.claramente.core.designsystem.theme

import com.claramente.core.designsystem.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

object ClaramenteFonts {
    val sans = FontFamily(
        Font(R.font.nunitosans_400regular, FontWeight.Normal),
        Font(R.font.nunitosans_600semibold, FontWeight.SemiBold),
        Font(R.font.nunitosans_700bold, FontWeight.Bold),
        Font(R.font.nunitosans_800extrabold, FontWeight.ExtraBold),
    )
}
