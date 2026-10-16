package dev.cxntered.textreplacer.config

import org.polyfrost.oneconfig.api.config.v1.annotations.Include
import org.polyfrost.oneconfig.api.config.v1.annotations.Text

class ReplacementConfig {
    @Include
    var enabled = true

    @Text(title = "Target Text", description = "The text to be replaced.")
    var target = ""

    @Text(title = "Replacement Text", description = "The text to replace the target with.")
    var replacement = ""

    var expandedTarget = ""
    var expandedReplacement = ""
    var expansionDirty = true
}
