package dev.cxntered.textreplacer.config

data class Replacer(
    var enabled: Boolean = true,
    var text: String = "",
    var replacementText: String = "",
    @Transient var expandedText: String = "",
    @Transient var expandedReplacementText: String = "",
)
