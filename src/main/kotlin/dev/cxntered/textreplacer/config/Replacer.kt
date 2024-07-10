package dev.cxntered.textreplacer.config

data class Replacer(
    var enabled: Boolean = true,
    var text: String = "",
    var replacementText: String = "",
)
