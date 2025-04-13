package dev.cxntered.textreplacer

import cc.polyfrost.oneconfig.libs.universal.UMinecraft
import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.commands.CommandManager
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

    fun getString(input: String): String {
        var string = input

        for (wrapper in ReplacerListOption.wrappedReplacers) {
            with (wrapper.replacer) {
                if (!enabled) return@with
                if (text.isEmpty() || replacementText.isEmpty()) return@with

                text = replaceVariables(text)
                replacementText = replaceVariables(replacementText)

                if (string.contains(text)) {
                    string = string.replace(text, replacementText)
                }
            }
        }

        return string
    }

    private fun replaceVariables(input: String): String {
        var string = input
        val mc = UMinecraft.getMinecraft()

        string = string.replace("¶username", mc.session.profile.name)

        if (!mc.isSingleplayer && mc.currentServerData != null) {
            var serverIp: String = mc.currentServerData.serverIP
            if (serverIp.contains(":"))
                serverIp = serverIp.split(":")[0]

            string = string.replace("¶serverIp", serverIp)

            if (!InetAddressUtils.isIPv4Address(serverIp) && !InetAddressUtils.isIPv6Address(serverIp)) {
                val parts = serverIp.split(".")
                val serverDomain = parts[parts.size - 2]

                string = string.replace("¶serverDomain", serverDomain)
            }

            // hypixel, for some reason, puts 🎂 in their scoreboard IP
            string = string.replace("¶hypixelScoreboardIp", "www.hypixel.ne\uD83C\uDF82§et")
        }

        return string
    }
}