package dev.cxntered.textreplacer

import dev.cxntered.textreplacer.config.ModConfig
import dev.cxntered.textreplacer.text.replaceString
import net.fabricmc.api.ModInitializer
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import org.apache.http.conn.util.InetAddressUtils

object TextReplacer : ModInitializer {
    private var expandedReplacements = mutableMapOf<String, String>()
    private var cachedUsername: String? = null
    private var cachedServerIp: String? = null
    private var cachedServerDomain: String? = null

    override fun onInitialize() {
        ModConfig.CONFIG.load()
    }

    @JvmStatic
    fun getReplacedText(input: Text): Text {
        var text = input
        processReplacements { target, replacement ->
            text = text.replaceString(target, replacement)
        }
        return text
    }

    @JvmStatic
    fun getReplacedString(input: String): String {
        var text = input
        processReplacements { target, replacement ->
            text = text.replace(target, replacement)
        }
        return text
    }

    private fun processReplacements(replaceText: (String, String) -> Unit) {
        var shouldExpand = false

        val currentUsername = MinecraftClient.getInstance().session.username
        if (currentUsername != cachedUsername) {
            cachedUsername = currentUsername
            shouldExpand = true
        }

        val currentServerIp = MinecraftClient.getInstance().currentServerEntry?.address
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

        ModConfig.CONFIG.instance().replacements.entries.forEach { replacement ->
            if (replacement.key.isEmpty() || replacement.value.isEmpty()) return@forEach

            if (shouldExpand || !expandedReplacements.containsKey(replacement.key))
                expandedReplacements[replacement.key] = expandText(replacement.key)
            if (shouldExpand || !expandedReplacements.containsKey(replacement.value))
                expandedReplacements[replacement.value] = expandText(replacement.value)

            val expandedKey = expandedReplacements[replacement.key] ?: replacement.key
            val expandedValue = expandedReplacements[replacement.value] ?: replacement.value

            replaceText(expandedKey, expandedValue)
        }
    }

    private fun expandText(input: String): String {
        if (input.isEmpty() || !input.contains('¶')) return input

        val variables = mapOf(
            "¶username" to cachedUsername,
            "¶serverIp" to cachedServerIp?.split(":")?.firstOrNull(),
            "¶serverDomain" to cachedServerDomain,
        )

        return variables.entries.fold(input) { text, (variable, value) ->
            if (value != null) text.replace(variable, value) else text
        }
    }
}