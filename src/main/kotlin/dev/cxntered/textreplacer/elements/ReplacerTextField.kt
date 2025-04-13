package dev.cxntered.textreplacer.elements

import cc.polyfrost.oneconfig.gui.elements.text.TextInputField
import cc.polyfrost.oneconfig.internal.assets.SVGs
import cc.polyfrost.oneconfig.utils.InputHandler
import kotlin.reflect.KMutableProperty0

@Suppress("UnstableAPIUsage")
class ReplacerTextField(
    private val textProperty: KMutableProperty0<String>,
    defaultText: String
) : TextInputField(432, 32, defaultText, false, false, SVGs.TEXT_INPUT, 12F) {

    override fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        input = textProperty.get()
        super.draw(vg, x, y, inputHandler)
    }

    fun isKeyTyped(key: Char, keyCode: Int): Boolean {
        if (!isToggled) return false
        keyTyped(key, keyCode)
        textProperty.set(input)
        return true
    }
}