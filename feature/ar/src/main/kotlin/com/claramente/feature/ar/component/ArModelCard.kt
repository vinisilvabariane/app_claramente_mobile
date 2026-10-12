package com.claramente.feature.ar.component

import com.claramente.core.designsystem.component.ArGlyph
import com.claramente.core.designsystem.component.BrandCard
import com.claramente.core.designsystem.component.Eyebrow
import com.claramente.core.designsystem.theme.ClaramenteColors
import com.claramente.core.designsystem.theme.ClaramenteRadius
import com.claramente.feature.ar.state.ArModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
internal fun ArModelCard(model: ArModel, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    BrandCard(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.RadioButton,
                onClick = onClick,
            ),
        borderColor = if (selected) ClaramenteColors.Primary else ClaramenteColors.Border,
        borderWidth = if (selected) 2.dp else 1.dp,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (selected) ClaramenteColors.PrimaryMist else ClaramenteColors.SurfaceMuted,
                        RoundedCornerShape(ClaramenteRadius.Md),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                ArGlyph(
                    color = if (selected) ClaramenteColors.Primary else ClaramenteColors.TextSecondary,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow(text = model.subject)
                Text(
                    text = model.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = ClaramenteColors.Ink,
                    modifier = Modifier.padding(top = 2.dp),
                )
                Text(
                    text = model.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ClaramenteColors.TextSecondary,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(
                        width = if (selected) 6.dp else 1.5.dp,
                        color = if (selected) ClaramenteColors.Primary else ClaramenteColors.BorderStrong,
                        shape = CircleShape,
                    ),
            )
        }
    }
}
