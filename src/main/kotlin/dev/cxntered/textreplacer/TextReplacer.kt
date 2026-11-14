package dev.cxntered.textreplacer

import dev.cxntered.textreplacer.config.ModConfig
import dev.cxntered.textreplacer.match.FilteringReplacer
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

        @JvmStatic
        fun getString(input: String): String {
            val mc = MinecraftClient.getInstance()
            val currentUsername = mc.session.profile.name
            val currentServerIp = mc.currentServerEntry?.address

            val usernameChanged = currentUsername != cachedUsername
            val serverIpChanged = currentServerIp != cachedServerIp

            if (serverIpChanged) {
                cachedServerIp = currentServerIp
                cachedServerDomain = currentServerIp
                    ?.takeUnless { InetAddressUtils.isIPv4Address(it) || InetAddressUtils.isIPv6Address(it) }
                    ?.substringBefore(":")
                    ?.substringBeforeLast(".", missingDelimiterValue = "")
                    ?.substringAfterLast(".")
            }

            if (usernameChanged || serverIpChanged) ModConfig.replacements.forEach { it.expansionDirty = true }
            cachedUsername = currentUsername

            return ModConfig.replacements.fold(input) { text, entry ->
                if (!entry.enabled || entry.target.isEmpty()) return@fold text

                if (entry.expansionDirty) {
                    entry.expandedTarget = expandText(entry.target)
                    entry.expandedReplacement = expandText(entry.replacement)
                    entry.expansionDirty = false
                }

                if (entry.expandedTarget.isEmpty()) return@fold text
                if (entry.expandedReplacement.isEmpty() && !ModConfig.allowEmptyReplacements) return@fold text

                FilteringReplacer.replace(
                    text,
                    entry.expandedTarget,
                    entry.expandedReplacement,
                    entry.ignoreFormattingCodes,
                    entry.ignoreInvalidCharacters
                )
            }
        }

        fun expandText(input: String): String {
            if ('¶' !in input) return input

            val variables = mapOf(
                "¶username" to cachedUsername,
                "¶serverIp" to cachedServerIp?.substringBefore(":"),
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
