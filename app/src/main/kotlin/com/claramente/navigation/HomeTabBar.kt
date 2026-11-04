package com.claramente.navigation

import com.claramente.core.designsystem.component.ClassroomGlyph
import com.claramente.core.designsystem.component.HomeGlyph
import com.claramente.core.designsystem.component.LibraryGlyph
import com.claramente.core.designsystem.component.ProfileGlyph
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeTabBar(current: HomeTab, onSelect: (HomeTab) -> Unit, tabs: List<HomeTab> = HomeTab.student) {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ClaramenteColors.White)
            .drawBehind { drawRect(color = ClaramenteColors.Border, size = Size(size.width, 1.dp.toPx())) }
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .padding(bottom = maxOf(bottomInset, 8.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(top = 8.dp),
        ) {
            tabs.forEach { tab ->
                val selected = tab == current
                val color = if (selected) ClaramenteColors.Primary else ClaramenteColors.TabInactive
                val interaction = remember { MutableInteractionSource() }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .selectable(
                            selected = selected,
                            interactionSource = interaction,
                            indication = null,
                            role = Role.Tab,
                            onClick = { onSelect(tab) },
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .width(56.dp)
                            .height(30.dp)
                            .background(
                                if (selected) ClaramenteColors.PrimaryMist else Color.Transparent,
                                RoundedCornerShape(ClaramenteRadius.Pill),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        when (tab) {
                            HomeTab.HUB -> HomeGlyph(color = color, modifier = Modifier.size(22.dp))
                            HomeTab.CLASSROOM, HomeTab.TEACHER_CLASSROOMS ->
                                ClassroomGlyph(color = color, modifier = Modifier.size(22.dp))
                            HomeTab.LIBRARY -> LibraryGlyph(color = color, modifier = Modifier.size(22.dp))
                            HomeTab.PROFILE -> ProfileGlyph(color = color, modifier = Modifier.size(22.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                        color = if (selected) ClaramenteColors.Ink else ClaramenteColors.TabInactive,
                    )
                }
            }
        }
    }
}
