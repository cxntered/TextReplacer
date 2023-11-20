package dev.cxntered.textreplacer.command

import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import dev.cxntered.textreplacer.TextReplacer
import dev.cxntered.textreplacer.config.TextReplacerConfig

@Command(value = TextReplacer.MODID, aliases = ["tr"], description = "Access the ${TextReplacer.NAME} GUI.")
class TextReplacerCommand {
    @Main
    fun main() {
        TextReplacerConfig.openGui()
    }
}