package dev.cxntered.textreplacer

import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import dev.cxntered.textreplacer.command.TextReplacerCommand
import dev.cxntered.textreplacer.config.TextReplacerConfig
import dev.cxntered.textreplacer.elements.ReplacerListOption
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(
        modid = TextReplacer.MODID,
        name = TextReplacer.NAME,
        version = TextReplacer.VERSION,
        modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter",
)
object TextReplacer {
    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"

    val PLUS_ICON = SVG("/assets/textreplacer/icons/plus.svg")
    val MINUS_ICON = SVG("/assets/textreplacer/icons/minus.svg")

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent?) {
        TextReplacerConfig.initialize()
        CommandManager.INSTANCE.registerCommand(TextReplacerCommand())
    }

    fun getString(input: String): String? {
        var string: String? = input

        for (wrapper in ReplacerListOption.wrappedReplacers) {
            val (enabled, text, replacementText) = wrapper.replacer

            if (enabled) {
                if (text.isEmpty() || replacementText.isEmpty()) return string

                if (string!!.contains(text)) {
                    string = string.replace(text.toRegex(), replacementText)
                }
            }
        }

        return string
    }
}