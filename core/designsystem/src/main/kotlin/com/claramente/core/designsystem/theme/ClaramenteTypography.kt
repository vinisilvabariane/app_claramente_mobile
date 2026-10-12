package com.claramente.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object ClaramenteTypography {
    private val sans = ClaramenteFonts.sans

    val typography = Typography(
        displaySmall = TextStyle(fontFamily = sans, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 38.sp, letterSpacing = (-0.8).sp),
        headlineMedium = TextStyle(fontFamily = sans, fontWeight = FontWeight.ExtraBold, fontSize = 26.sp, lineHeight = 32.sp, letterSpacing = (-0.5).sp),
        headlineSmall = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp, letterSpacing = (-0.3).sp),
        titleLarge = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 19.sp, lineHeight = 25.sp, letterSpacing = (-0.2).sp),
        titleMedium = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 17.sp, lineHeight = 23.sp),
        titleSmall = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 21.sp),
        bodyLarge = TextStyle(fontFamily = sans, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
        bodyMedium = TextStyle(fontFamily = sans, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 21.sp),
        bodySmall = TextStyle(fontFamily = sans, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 19.sp),
        labelLarge = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 20.sp),
        labelMedium = TextStyle(fontFamily = sans, fontWeight = FontWeight.Bold, fontSize = 13.sp, lineHeight = 18.sp),
        labelSmall = TextStyle(fontFamily = sans, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.8.sp),
    )
}
