package dev.cxntered.textreplacer.config

import dev.cxntered.textreplacer.TextReplacer
import org.polyfrost.oneconfig.api.config.v1.Config
import org.polyfrost.oneconfig.api.config.v1.ConfigManager
import org.polyfrost.oneconfig.api.config.v1.Node
import org.polyfrost.oneconfig.api.config.v1.Properties
import org.polyfrost.oneconfig.api.config.v1.Property
import org.polyfrost.oneconfig.api.config.v1.Tree
import org.polyfrost.oneconfig.api.config.v1.annotations.Button
import org.polyfrost.oneconfig.api.config.v1.annotations.Switch
import org.polyfrost.oneconfig.api.config.v1.collect.impl.OneConfigCollector
import org.polyfrost.oneconfig.api.platform.v1.Platform
import org.polyfrost.oneconfig.internal.ui.api.ConfigRegistry
import org.polyfrost.oneconfig.internal.ui.api.ConfigSource
import org.polyfrost.oneconfig.utils.v1.ClipboardHelper
import java.util.concurrent.CopyOnWriteArrayList

@Suppress("unused")
object ModConfig : Config(
    "textreplacer.json",
    "/assets/textreplacer/textreplacer.svg",
    "TextReplacer",
    Category.UTILITY
) {
    private const val REPLACEMENT_PREFIX = "replacement_"
    private val theMapField by lazy { Tree::class.java.getDeclaredField("theMap").apply { isAccessible = true } }
    val replacements = CopyOnWriteArrayList<ReplacementEntry>()

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

    @Switch(
        title = "Allow Empty Replacements",
        description = "Allow empty replacements to delete targeted text instead of being ignored.",
        category = "General",
        subcategory = "Advanced"
    )
    var allowEmptyReplacements = false

    @Button(
        title = "Add Replacement",
        description = "Add a new replacement.",
        text = "Add",
        category = "Replacements"
    )
    fun addReplacement() {
        replacements.add(ReplacementEntry())
        generateAccordions(expandedIndex = replacements.lastIndex)
        save()
        refreshUi()
    }

    override fun makeTree(): Tree {
        val tree = super.makeTree()

        replacements.clear()
        ConfigManager.active().load(id)?.map?.let { saved ->
            saved.entries
                .filter { it.key.startsWith(REPLACEMENT_PREFIX) }
                .sortedBy { it.key.removePrefix(REPLACEMENT_PREFIX).toIntOrNull() ?: Int.MAX_VALUE }
                .mapNotNullTo(replacements) { (_, value) -> (value as? Tree)?.toReplacementEntry() }
        }

        generateAccordions(tree)
        return tree
    }

    @Suppress("UNCHECKED_CAST")
    private fun generateAccordions(target: Tree? = tree, expandedIndex: Int? = null) {
        val root = target ?: return

        // remove existing replacement accordions
        (theMapField.get(root) as MutableMap<String, Node>).keys.removeIf { it.startsWith(REPLACEMENT_PREFIX) }

        // add new accordion for each replacement
        val collector = OneConfigCollector()
        replacements.forEachIndexed { index, entry ->
            val accordion = Tree.tree("$REPLACEMENT_PREFIX$index")
            accordion.addMetadata(
                mapOf(
                    "title" to "Replacement #${index + 1}",
                    "description" to getReplacementDescription(entry),
                    "category" to "Replacements",
                    "canBeEnabled" to true,
                    "index" to index,
                    "collapsed" to (index != expandedIndex),
                )
            )
            collector.handle(accordion, entry, 0)

            accordion.put(actionsProperty(index, entry))

            listOf("target", "replacement").forEach {
                accordion.getProp(it).addCallback {
                    entry.refreshExpandedText()
                    accordion.description = getReplacementDescription(entry)
                    false
                }
            }

            entry.refreshExpandedText()

            root.put(accordion)
        }
    }

    private fun actionsProperty(index: Int, entry: ReplacementEntry): Property<Void> {
        val actions = Properties.dummy("actions", "\u00A0", "Manage this replacement.")
        actions.addMetadata("visualizer", ReplacementActionsVisualizer)
        if (index > 0) actions.addMetadata("moveUp", { moveReplacement(index, index - 1) })
        if (index < replacements.lastIndex) actions.addMetadata("moveDown", { moveReplacement(index, index + 1) })
        actions.addMetadata("delete", { deleteReplacement(index) })
        actions.addMetadata("isEmpty", { entry.target.isBlank() && entry.replacement.isBlank() })
        return actions
    }

    private fun ReplacementEntry.refreshExpandedText() {
        expandedTarget = TextReplacer.expandText(target)
        expandedReplacement = TextReplacer.expandText(replacement)
    }

    private fun getReplacementDescription(entry: ReplacementEntry): String {
        return if (entry.target.isBlank() && entry.replacement.isBlank()) {
            "Empty replacement (expand to edit)"
        } else {
            val targetDesc = entry.target.ifBlank { "(empty)" }
            val replacementDesc = when {
                entry.replacement.isNotEmpty() -> entry.replacement
                allowEmptyReplacements -> "(removed)"
                else -> "(empty)"
            }
            "$targetDesc §r→ $replacementDesc"
        }
    }

    private fun Tree.toReplacementEntry() = ReplacementEntry().apply {
        (getProp("enabled")?.get() as? Boolean)?.let { enabled = it }
        (getProp("target")?.get() as? String)?.let { target = it }
        (getProp("replacement")?.get() as? String)?.let { replacement = it }
        (getProp("matchFilters")?.get() as? List<*>)?.let { saved ->
            matchFilters = BooleanArray(matchFilters.size) { saved.getOrNull(it) == true }
        }
    }

    private fun moveReplacement(from: Int, to: Int) {
        replacements.add(to, replacements.removeAt(from))
        generateAccordions(expandedIndex = to)
        save()
        refreshUi()
    }

    private fun deleteReplacement(index: Int) {
        replacements.removeAt(index)
        generateAccordions()
        save()
        refreshUi()
    }

    private fun refreshUi() {
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
