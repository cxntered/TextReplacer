package dev.cxntered.textreplacer

import dev.cxntered.textreplacer.config.ModConfig
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient
import org.apache.http.conn.util.InetAddressUtils

class TextReplacer : ModInitializer {
    override fun onInitialize() {
        ModConfig.preload()
    }

    companion object {
        private var cachedUsername: String? = null
        private var cachedServerIp: String? = null
        private var cachedServerDomain: String? = null

        private val mc = MinecraftClient.getInstance()

        @JvmStatic
        fun getString(input: String): String {
            val currentUsername = mc.session.profile.name
            val currentServerIp = mc.currentServerEntry?.address

            val shouldExpand = currentUsername != cachedUsername || currentServerIp != cachedServerIp
            cachedUsername = currentUsername

            if (currentServerIp != cachedServerIp) {
                cachedServerIp = currentServerIp
                cachedServerDomain = currentServerIp
                    ?.substringBefore(":")
                    ?.takeUnless { InetAddressUtils.isIPv4Address(it) || InetAddressUtils.isIPv6Address(it) }
                    ?.substringBeforeLast(".", missingDelimiterValue = "")
                    ?.substringAfterLast(".")
            }

            return ModConfig.replacements.fold(input) { string, wrapper ->
                if (!wrapper.enabled || wrapper.target.isEmpty() || wrapper.replacement.isEmpty()) {
                    return@fold string
                }

                if (shouldExpand || wrapper.expandedTarget.isEmpty()) {
                    wrapper.expandedTarget = expandText(wrapper.target)
                }
                if (shouldExpand || wrapper.expandedReplacement.isEmpty()) {
                    wrapper.expandedReplacement = expandText(wrapper.replacement)
                }

                string.replace(wrapper.expandedTarget, wrapper.expandedReplacement)
            }
        }

        fun expandText(input: String): String {
            if ('¶' !in input) return input

            val variables = mapOf(
                "¶username" to cachedUsername,
                "¶serverIp" to cachedServerIp?.split(":")?.firstOrNull(),
                "¶serverDomain" to cachedServerDomain,
                // hypixel adds 'invisible' emojis in the scoreboard, so we have to hardcode the scoreboard ip
                "¶hypixelScoreboardIp" to "www.hypixel.ne\uD83C\uDF82§et"
            )

            return variables.entries.fold(input) { text, (variable, value) ->
                if (value != null) text.replace(variable, value) else text
            }
        }
    }
}
