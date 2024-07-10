package dev.cxntered.textreplacer.elements

import cc.polyfrost.oneconfig.gui.elements.text.TextInputField
import cc.polyfrost.oneconfig.internal.assets.SVGs
import cc.polyfrost.oneconfig.utils.InputHandler
import dev.cxntered.textreplacer.config.Replacer

@Suppress("UnstableAPIUsage")
class ReplacerTextField(
    private val replacer: Replacer
) : TextInputField(432, 32, "Text to replace", false, false, SVGs.TEXT_INPUT) {

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        input = replacer.text
        super.draw(vg, x, y, inputHandler)
    }

    fun isKeyTyped(key: Char, keyCode: Int): Boolean {
        if (!isToggled) return false
        keyTyped(key, keyCode)
        replacer.text = input
        return true
    }
}