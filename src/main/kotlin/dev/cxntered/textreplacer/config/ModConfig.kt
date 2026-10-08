package dev.cxntered.textreplacer.config

import dev.cxntered.textreplacer.TextReplacer
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.ConfigManager
import org.polyfrost.oneconfig.api.config.v1.Node
import org.polyfrost.oneconfig.api.config.v1.Tree
import org.polyfrost.oneconfig.api.config.v1.annotations.Button
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.oneconfig.api.config.v1.collect.impl.OneConfigCollector
import org.polyfrost.oneconfig.api.platform.v1.Platform
import org.polyfrost.oneconfig.internal.ui.api.ConfigRegistry
import org.polyfrost.oneconfig.internal.ui.api.ConfigSource
import org.polyfrost.oneconfig.utils.v1.ClipboardHelper

@Suppress("unused")
object ModConfig : Config(
    "textreplacer.json",
    "/assets/textreplacer/textreplacer.svg",
    "TextReplacer",
    Category.UTILITY
) {
    private const val REPLACEMENT_PREFIX = "replacement_"
    val replacements = mutableListOf<ReplacementConfig>()

    @JvmStatic
    @Switch(title = "Enabled", description = "Enable or disable the mod.")
    var isEnabled = true

    @Button(
        title = "Copy Section Sign",
        description = "Use the Section Sign (§) to use formatting codes.",
        text = "Copy"
    )
    fun copyFormattingCode() {
        ClipboardHelper.setString("§")
    }

    @Button(
        title = "Copy Pilcrow Sign",
        description = "Use the Pilcrow Sign (¶) to use variables. \nAvailable variables: username, serverIp, serverDomain, hypixelScoreboardIp",
        text = "Copy"
    )
    fun copyVariableCode() {
        ClipboardHelper.setString("¶")
    }

    @Button(
        title = "Add Replacement",
        description = "Add a new replacement.",
        text = "Add",
        category = "Replacements"
    )
    fun addReplacement() {
        replacements.add(ReplacementConfig(::deleteReplacement))
        generateAccordions(expandedIndex = replacements.lastIndex)
        save()
        refreshUI()
    }

    @Suppress("UnstableApiUsage")
    override fun makeTree(): Tree {
        val tree = super.makeTree()

        replacements.clear()
        ConfigManager.active().load(id)?.map?.let { saved ->
            saved.entries
                .filter { it.key.startsWith(REPLACEMENT_PREFIX) }
                .sortedBy { it.key.removePrefix(REPLACEMENT_PREFIX).toIntOrNull() ?: Int.MAX_VALUE }
                .mapNotNullTo(replacements) { (_, value) -> (value as? Tree)?.toReplacementConfig() }
        }

        generateAccordions(tree)
        return tree
    }

    @Suppress("UNCHECKED_CAST")
    private fun generateAccordions(target: Tree? = tree, expandedIndex: Int? = null) {
        val root = target ?: return

        // remove existing replacement accordions
        val field = Tree::class.java.getDeclaredField("theMap").apply { isAccessible = true }
        (field.get(root) as MutableMap<String, Node>).keys.removeIf { it.startsWith(REPLACEMENT_PREFIX) }

        // add new accordion for each replacement
        val collector = OneConfigCollector()
        replacements.forEachIndexed { index, replacement ->
            val accordion = Tree.tree("$REPLACEMENT_PREFIX$index")
            accordion.addMetadata(
                mapOf(
                    "title" to "Replacement #${index + 1}",
                    "description" to getReplacementDescription(replacement),
                    "category" to "Replacements",
                    "canBeEnabled" to true,
                    "index" to index,
                    "collapsed" to (index != expandedIndex),
                )
            )
            collector.handle(accordion, replacement, 0)

            listOf("target", "replacement").forEach {
                accordion.getProp(it).addCallback {
                    replacement.expandedTarget = TextReplacer.expandText(replacement.target)
                    replacement.expandedReplacement = TextReplacer.expandText(replacement.replacement)
                    accordion.description = getReplacementDescription(replacement)
                    false
                }
            }

            replacement.expandedTarget = TextReplacer.expandText(replacement.target)
            replacement.expandedReplacement = TextReplacer.expandText(replacement.replacement)

            root.put(accordion)
        }
    }

    private fun getReplacementDescription(replacement: ReplacementConfig): String {
        return if (replacement.target.isBlank() && replacement.replacement.isBlank()) {
            "Empty replacement (expand to edit)"
        } else {
            val targetDesc = replacement.target.ifBlank { "(empty)" }
            val replacementDesc = replacement.replacement.ifBlank { "(empty)" }
            "$targetDesc → $replacementDesc"
        }
    }

    private fun Tree.toReplacementConfig() = ReplacementConfig(::deleteReplacement).apply {
        (getProp("enabled")?.get() as? Boolean)?.let { enabled = it }
        (getProp("target")?.get() as? String)?.let { target = it }
        (getProp("replacement")?.get() as? String)?.let { replacement = it }
    }

    private fun deleteReplacement(replacement: ReplacementConfig) {
        replacements.remove(replacement)
        generateAccordions()
        save()
        refreshUI()
    }

    private fun refreshUI() {
        Platform.screen().runOnUiThread {
            try {
                tree ?: return@runOnUiThread
                ConfigRegistry.unregister(id)
                ConfigRegistry.registerTree(tree, ConfigSource.OC)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
