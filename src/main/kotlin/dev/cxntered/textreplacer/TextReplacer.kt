package dev.cxntered.textreplacer

import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import cc.polyfrost.oneconfig.utils.dsl.mc
import dev.cxntered.textreplacer.command.TextReplacerCommand
import dev.cxntered.textreplacer.config.TextReplacerConfig
import dev.cxntered.textreplacer.elements.ReplacerListOption
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import org.apache.http.conn.util.InetAddressUtils

@Mod(
    modid = TextReplacer.MODID,
    name = TextReplacer.NAME,
    version = TextReplacer.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object TextReplacer {
    const val MODID = "@ID@"
    const val NAME = "@NAME@"
    const val VERSION = "@VER@"

    val PLUS_ICON = SVG("/assets/textreplacer/icons/plus.svg")
    val MINUS_ICON = SVG("/assets/textreplacer/icons/minus.svg")

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        TextReplacerConfig.initialize()
        CommandManager.INSTANCE.registerCommand(TextReplacerCommand())
    }

    @JvmStatic
    fun getString(input: String): String {
        var string = input

        for (wrapper in ReplacerListOption.wrappedReplacers) {
            with(wrapper.replacer) {
                if (!enabled || text.isEmpty() || replacementText.isEmpty()) return@with

                if (expandedText.isEmpty())
                    expandedText = expandText(text)
                if (expandedReplacementText.isEmpty())
                    expandedReplacementText = expandText(replacementText)

                string = string.replace(expandedText, expandedReplacementText)
            }
        }

        return string
    }

    fun expandText(input: String): String {
        if (input.isEmpty() || !input.contains('¶')) return input
        var string = input

        val serverIp = mc.currentServerData?.serverIP?.split(":")?.firstOrNull()

        val variables = mapOf(
            "¶username" to mc.session.profile.name,
            "¶serverIp" to serverIp,
            "¶serverDomain" to run {
                if (serverIp != null &&
                    !InetAddressUtils.isIPv4Address(serverIp) &&
                    !InetAddressUtils.isIPv6Address(serverIp)
                ) {
                    val parts = serverIp.split(".")
                    parts[parts.size - 2]
                } else null
            },
            // hypixel, for some reason, puts 🎂 in their scoreboard IP
            "¶hypixelScoreboardIp" to "www.hypixel.ne\uD83C\uDF82§et"
        )

        for ((variable, value) in variables) {
            if (value != null) string = string.replace(variable, value.toString())
        }

        return string
    }
}