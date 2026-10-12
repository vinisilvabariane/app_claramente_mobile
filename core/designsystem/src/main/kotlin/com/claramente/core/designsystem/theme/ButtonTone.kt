package com.claramente.core.designsystem.theme

import androidx.compose.ui.graphics.Color

enum class ButtonTone(val main: Color, val deep: Color) {
    PRIMARY(main = ClaramenteColors.Primary, deep = ClaramenteColors.PrimaryDeep),
    DANGER(main = ClaramenteColors.ErrorText, deep = ClaramenteColors.ErrorDeep),
}
