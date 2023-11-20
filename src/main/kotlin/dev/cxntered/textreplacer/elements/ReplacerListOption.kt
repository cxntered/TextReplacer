package dev.cxntered.textreplacer.elements

import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.gui.elements.IFocusable
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import dev.cxntered.textreplacer.TextReplacer
import dev.cxntered.textreplacer.config.Replacer

@Suppress("UnstableAPIUsage")
object ReplacerListOption : BasicOption(null, null, "", "", "General", "", 2), IFocusable {
    private val addButton = BasicButton(32, 32, TextReplacer.PLUS_ICON, BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY)
    var wrappedReplacers: MutableList<WrappedReplacer> = ArrayList()
    var willBeRemoved: WrappedReplacer? = null

    init {
        addButton.setClickAction {
            wrappedReplacers.add(WrappedReplacer(Replacer()))
        }
    }

    override fun getHeight() = wrappedReplacers.size * 48 + 32

    override fun draw(vg: Long, x: Int, y: Int, inputHandler: InputHandler) {
        var nextY = y

        for (option in wrappedReplacers) {
            option.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)
            nextY += 48
        }

        addButton.draw(vg, x.toFloat(), nextY.toFloat(), inputHandler)

        checkWillBeRemoved()
    }

    private fun checkWillBeRemoved() {
        val replacer = willBeRemoved ?: return
        wrappedReplacers.remove(replacer)
        willBeRemoved = null
    }

    override fun keyTyped(key: Char, keyCode: Int) {
        wrappedReplacers.any { it.keyTyped(key, keyCode) }
    }

    override fun hasFocus() = wrappedReplacers.any { it.hasFocus() }

}