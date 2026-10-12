package com.claramente.feature.hub.state

import com.claramente.core.designsystem.theme.ClaramenteColors
import androidx.compose.ui.graphics.Color

enum class FeedTone(val color: Color, val mist: Color) {
    NOTICE(ClaramenteColors.Violet, ClaramenteColors.VioletMist),
    ACTIVITY(ClaramenteColors.Warning, ClaramenteColors.WarningMist),
    ACHIEVEMENT(ClaramenteColors.Success, ClaramenteColors.SuccessMist),
    REMINDER(ClaramenteColors.Primary, ClaramenteColors.PrimaryMist),
}
