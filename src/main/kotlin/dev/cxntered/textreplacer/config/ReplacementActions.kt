@file:OptIn(ExperimentalOneConfigUiApi::class)

package dev.cxntered.textreplacer.config

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.polyfrost.oneconfig.api.config.v1.Visualizer
import org.polyfrost.oneconfig.api.ui.v1.ExperimentalOneConfigUiApi
import org.polyfrost.oneconfig.api.ui.v1.components.ControlSize
import org.polyfrost.oneconfig.api.ui.v1.components.IconButton
import org.polyfrost.oneconfig.api.ui.v1.theme.OneConfigTheme

val ReplacementActionsVisualizer = Visualizer { prop ->
    val moveUp = prop.getMetadata<() -> Unit>("moveUp")
    val moveDown = prop.getMetadata<() -> Unit>("moveDown")
    val delete = prop.getMetadata<() -> Unit>("delete")

    Row(
        modifier = Modifier.padding(end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        ActionIcon("up", "Move up", enabled = moveUp != null) { moveUp?.invoke() }
        ActionIcon("down", "Move down", enabled = moveDown != null) { moveDown?.invoke() }
        ActionIcon(
            "trash",
            "Delete replacement",
            enabled = delete != null,
            danger = true,
            tint = OneConfigTheme.current.error,
        ) {
            delete?.invoke()
        }
    }
}

@Composable
private fun ActionIcon(
    icon: String,
    contentDescription: String,
    enabled: Boolean,
    danger: Boolean = false,
    tint: Color = Color.Unspecified,
    onClick: () -> Unit,
) {
    IconButton(
        icon = icon,
        contentDescription = contentDescription,
        onClick = onClick,
        enabled = enabled,
        size = ControlSize.Compact,
        iconSize = 18.dp,
        danger = danger,
        restTint = tint,
    )
}
