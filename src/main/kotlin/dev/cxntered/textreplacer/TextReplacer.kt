package dev.cxntered.textreplacer

import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import cc.polyfrost.oneconfig.utils.commands.annotations.Command
import cc.polyfrost.oneconfig.utils.commands.annotations.Main
import cc.polyfrost.oneconfig.utils.dsl.mc
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

    private var cachedUsername: String? = null
    private var cachedServerIp: String? = null
    private var cachedServerDomain: String? = null

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        TextReplacerConfig.initialize()
        CommandManager.INSTANCE.registerCommand(TextReplacerCommand())
    }

    @JvmStatic
    fun getString(input: String): String {
        var shouldExpand = false

        val currentUsername = mc.session.profile.name
        if (currentUsername != cachedUsername) {
            cachedUsername = currentUsername
            shouldExpand = true
        }

        val currentServerIp = mc.currentServerData?.serverIP
        if (currentServerIp != cachedServerIp) {
            cachedServerIp = currentServerIp
            cachedServerDomain = currentServerIp?.let { ip ->
                val baseAddress = ip.split(":").first()
                baseAddress.takeIf {
                    !InetAddressUtils.isIPv4Address(it) && !InetAddressUtils.isIPv6Address(it)
                }?.split(".")?.dropLast(1)?.last()
            }
            shouldExpand = true
        }

        return ReplacerListOption.wrappedReplacers.fold(input) { string, wrapper ->
            with(wrapper.replacer) {
                if (!enabled || text.isEmpty() || replacementText.isEmpty()) return@with string

                if (shouldExpand || expandedText.isEmpty())
                    expandedText = expandText(text)
                if (shouldExpand || expandedReplacementText.isEmpty())
                    expandedReplacementText = expandText(replacementText)

                string.replace(expandedText, expandedReplacementText)
            }
        }
    }

    fun expandText(input: String): String {
        if (input.isEmpty() || !input.contains('¶')) return input

        val variables = mapOf(
            "¶username" to cachedUsername,
            "¶serverIp" to cachedServerIp?.split(":")?.firstOrNull(),
            "¶serverDomain" to cachedServerDomain,
            // hypixel, for some reason, puts 🎂 in their scoreboard IP
            "¶hypixelScoreboardIp" to "www.hypixel.ne\uD83C\uDF82§et"
        )

        return variables.entries.fold(input) { text, (variable, value) ->
            if (value != null) text.replace(variable, value.toString()) else text
        }
    }

    @Command(value = MODID, description = "Access the $NAME GUI.")
    class TextReplacerCommand {
        @Main
        fun handle() {
            TextReplacerConfig.openGui()
        }
    }
}