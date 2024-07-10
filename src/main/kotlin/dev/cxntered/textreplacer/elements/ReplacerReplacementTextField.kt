package dev.cxntered.textreplacer.elements

import cc.polyfrost.oneconfig.gui.elements.text.TextInputField
import cc.polyfrost.oneconfig.internal.assets.SVGs
import cc.polyfrost.oneconfig.utils.InputHandler
import dev.cxntered.textreplacer.config.Replacer

@Suppress("UnstableAPIUsage")
class ReplacerReplacementTextField(
        private val replacer: Replacer
) : TextInputField(432, 32, "Replacement text", false, false, SVGs.TEXT_INPUT) {

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        input = replacer.replacementText
        super.draw(vg, x, y, inputHandler)
    }

    fun isKeyTyped(key: Char, keyCode: Int): Boolean {
        if (!isToggled) return false
        keyTyped(key, keyCode)
        replacer.replacementText = input
        return true
    }
}