package dev.cxntered.textreplacer.elements

import cc.polyfrost.oneconfig.gui.elements.BasicButton
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import dev.cxntered.textreplacer.TextReplacer
import dev.cxntered.textreplacer.config.Replacer

@Suppress("UnstableAPIUsage")
class WrappedReplacer(
        val replacer: Replacer
) {
    private val removeButton = BasicButton(32, 32, TextReplacer.MINUS_ICON, BasicButton.ALIGNMENT_CENTER, ColorPalette.PRIMARY_DESTRUCTIVE)
    private val checkbox = ReplacerCheckbox(replacer)
    private val textField = ReplacerTextField(replacer.text, "Text to replace")
    private val replacementTextField = ReplacerTextField(replacer.replacementText, "Replacement text")

    init {
        removeButton.setClickAction {
            ReplacerListOption.willBeRemoved = this
        }
    }

    fun draw(vg: Long, x: Float, y: Float, inputHandler: InputHandler) {
        removeButton.draw(vg, x, y, inputHandler)
        checkbox.draw(vg, x + 58, y, inputHandler)
        textField.draw(vg, x + 96, y, inputHandler)
        replacementTextField.draw(vg, x + 560, y, inputHandler)
    }

    fun keyTyped(key: Char, keyCode: Int) = textField.isKeyTyped(key, keyCode) || replacementTextField.isKeyTyped(key, keyCode)

    fun hasFocus() = textField.isToggled || replacementTextField.isToggled
}