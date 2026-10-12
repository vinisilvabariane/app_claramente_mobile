package com.claramente.core.designsystem.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

object ClaramenteColors {
    val Ink = Color(0xFF0F1B2D)
    val InkDeep = Color(0xFF0A1322)
    val TextSecondary = Color(0xFF566173)
    val TextTertiary = Color(0xFF8A94A3)
    val Background = Color(0xFFF5F7FA)
    val White = Color(0xFFFFFFFF)
    val SurfaceMuted = Color(0xFFEEF1F5)
    val Border = Color(0xFFE3E7ED)
    val BorderStrong = Color(0xFFCDD3DC)
    val Primary = Color(0xFF1F6FE5)
    val PrimaryDeep = Color(0xFF1655BD)
    val PrimaryMist = Color(0xFFEAF1FD)
    val Success = Color(0xFF1F8A57)
    val SuccessMist = Color(0xFFE6F4EC)
    val Warning = Color(0xFFA86400)
    val WarningMist = Color(0xFFFCF1DE)
    val Violet = Color(0xFF6447D1)
    val VioletMist = Color(0xFFEFEBFB)
    val TabInactive = Color(0xFF8A94A3)
    val FieldFill = Color(0xFFFFFFFF)
    val FieldBorder = Color(0xFFCDD3DC)
    val ErrorFill = Color(0xFFFDECEC)
    val ErrorBorder = Color(0xFFF5C2C0)
    val ErrorText = Color(0xFFB42318)
    val ErrorDeep = Color(0xFF8F1C13)
    val Overlay = Color(0x0A, 0x13, 0x22, 0xC7)

    internal val LightScheme = lightColorScheme(
        primary = Primary,
        onPrimary = White,
        secondary = Ink,
        onSecondary = White,
        tertiary = Violet,
        background = Background,
        onBackground = Ink,
        surface = White,
        onSurface = Ink,
        onSurfaceVariant = TextSecondary,
        outline = Border,
        error = ErrorText,
    )
}
