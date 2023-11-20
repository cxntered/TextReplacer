package dev.cxntered.textreplacer

import cc.polyfrost.oneconfig.utils.commands.CommandManager
import dev.cxntered.textreplacer.command.TextReplacerCommand
import dev.cxntered.textreplacer.config.TextReplacerConfig
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

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent?) {
        TextReplacerConfig
        CommandManager.INSTANCE.registerCommand(TextReplacerCommand())
    }
}