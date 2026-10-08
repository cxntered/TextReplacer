package dev.cxntered.textreplacer.config

import org.polyfrost.oneconfig.api.config.v1.annotations.Button
import org.polyfrost.oneconfig.api.config.v1.annotations.Include
import org.polyfrost.oneconfig.api.config.v1.annotations.Text

class ReplacementConfig(private val onDelete: (ReplacementConfig) -> Unit) {
    @Include
    var enabled = true

    @Text(title = "Target Text", description = "The text to be replaced.")
    var target = ""

    @Text(title = "Replacement Text", description = "The text to replace the target with.")
    var replacement = ""

    var expandedTarget = ""
    var expandedReplacement = ""

    @Button(title = "Delete Replacement", description = "Delete this replacement.", text = "Delete")
    fun delete() = onDelete(this)
}
