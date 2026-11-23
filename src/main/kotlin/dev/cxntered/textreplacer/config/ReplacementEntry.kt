package dev.cxntered.textreplacer.config

import org.polyfrost.oneconfig.api.config.v1.annotations.Include
import org.polyfrost.oneconfig.api.config.v1.annotations.MultiSelectDropdown
import org.polyfrost.oneconfig.api.config.v1.annotations.Text

class ReplacementEntry {
    @Include
    var enabled = true

    @Text(title = "Target Text", description = "The text to be replaced.")
    var target = ""

    @Text(title = "Replacement Text", description = "The text to replace the target with.")
    var replacement = ""

    @MultiSelectDropdown(
        title = "Match Filters",
        description = "Ignore these characters while matching. Invalid characters are not rendered and are commonly found in scoreboards.",
        options = ["Formatting codes", "Invalid characters"]
    )
    var matchFilters = booleanArrayOf(false, false)

    var expandedTarget = ""
    var expandedReplacement = ""
    var expansionDirty = true

    val ignoreFormattingCodes
        get() = matchFilters.getOrNull(INDEX_FORMATTING_CODES) == true
    val ignoreInvalidCharacters
        get() = matchFilters.getOrNull(INDEX_INVALID_CHARACTERS) == true

    companion object {
        // indices for the matchFilters array
        private const val INDEX_FORMATTING_CODES = 0
        private const val INDEX_INVALID_CHARACTERS = 1
    }
}
