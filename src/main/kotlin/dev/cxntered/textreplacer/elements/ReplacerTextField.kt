package dev.cxntered.textreplacer.elements

import cc.polyfrost.oneconfig.gui.elements.text.TextInputField
import cc.polyfrost.oneconfig.internal.assets.SVGs
import cc.polyfrost.oneconfig.utils.InputHandler

@Suppress("UnstableAPIUsage")
class ReplacerTextField(
        private var text: String, placeholder: String
) : TextInputField(432, 32, placeholder, false, false, SVGs.TEXT_INPUT) {

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        input = text
        super.draw(vg, x, y, inputHandler)
    }

    fun isKeyTyped(key: Char, keyCode: Int): Boolean {
        if (!isToggled) return false
        keyTyped(key, keyCode)
        text = input
        return true
    }
}