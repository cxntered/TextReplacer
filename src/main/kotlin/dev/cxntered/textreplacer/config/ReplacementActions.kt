@file:OptIn(ExperimentalOneConfigUiApi::class)

package dev.cxntered.textreplacer.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.polyfrost.oneconfig.api.config.v1.Visualizer
import org.polyfrost.oneconfig.api.ui.v1.ExperimentalOneConfigUiApi
import org.polyfrost.oneconfig.api.ui.v1.components.Icon
import org.polyfrost.oneconfig.api.ui.v1.components.IconButton
import org.polyfrost.oneconfig.api.ui.v1.components.Text
import org.polyfrost.oneconfig.api.ui.v1.components.Tooltip
import org.polyfrost.oneconfig.api.ui.v1.theme.OneConfigTheme

private val DeleteColor = Color(0xFFE5484D)

val ReplacementActionsVisualizer = Visualizer { prop ->
    val theme = OneConfigTheme.current
    val moveColor = theme.textColor.copy(alpha = 0.7f)
    Row(
        modifier = Modifier.padding(vertical = 6.dp).padding(end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ActionIcon(
            icon = "up",
            action = prop.getMetadata("moveUp"),
            color = moveColor,
            hoverColor = theme.textColor,
            tooltip = "Move up",
        )
        ActionIcon(
            icon = "up",
            action = prop.getMetadata("moveDown"),
            color = moveColor,
            hoverColor = theme.textColor,
            tooltip = "Move down",
            rotation = 180f,
        )
        ActionIcon(
            icon = "trash",
            action = prop.getMetadata("delete"),
            color = DeleteColor.copy(alpha = 0.8f),
            hoverColor = DeleteColor,
            tooltip = "Delete replacement",
        )
    }
}

@Composable
private fun ActionIcon(
    icon: String,
    action: (() -> Unit)?,
    color: Color,
    hoverColor: Color,
    tooltip: String,
    rotation: Float = 0f,
) {
    val theme = OneConfigTheme.current
    val modifier = Modifier.size(20.dp).rotate(rotation)
    Tooltip(
        text = { Text(tooltip, color = theme.textColor, fontSize = 12.sp, modifier = Modifier) },
        modifier = Modifier,
        anchor = Alignment.TopCenter,
    ) {
        if (action != null) {
            IconButton(
                iconName = icon,
                foreground = color,
                hoveredForeground = hoverColor,
                onClick = action,
                enabled = true,
                hoverBorder = false,
                modifier = modifier.pointerHoverIcon(PointerIcon.Hand)
            )
        } else {
            Icon(icon, color = color.copy(alpha = 0.3f), modifier = modifier)
        }
    }
}
