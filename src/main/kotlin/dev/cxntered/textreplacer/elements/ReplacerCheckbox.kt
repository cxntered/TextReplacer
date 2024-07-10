package dev.cxntered.textreplacer.elements

import cc.polyfrost.oneconfig.gui.animations.Animation
import cc.polyfrost.oneconfig.gui.animations.ColorAnimation
import cc.polyfrost.oneconfig.gui.animations.DummyAnimation
import cc.polyfrost.oneconfig.gui.animations.EaseInOutQuad
import cc.polyfrost.oneconfig.internal.assets.Colors
import cc.polyfrost.oneconfig.internal.assets.SVGs
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.color.ColorPalette
import cc.polyfrost.oneconfig.utils.dsl.VG
import cc.polyfrost.oneconfig.utils.dsl.drawHollowRoundedRect
import cc.polyfrost.oneconfig.utils.dsl.drawRoundedRect
import cc.polyfrost.oneconfig.utils.dsl.drawSVG
import dev.cxntered.textreplacer.config.Replacer

class ReplacerCheckbox(
    private val text: Replacer
) {
    private val colorAnimation = ColorAnimation(ColorPalette.SECONDARY)
    private var alphaAnimation: Animation = DummyAnimation(if (text.enabled) 1f else 0f)

    fun draw(vgLong: Long, x: Float, y: Float, inputHandler: InputHandler) {
        val hover = inputHandler.isAreaHovered(x, y + 4f, 24f, 24f)
        val clicked = inputHandler.isClicked && hover
        val pressed = inputHandler.isMouseDown && hover

        if (clicked) {
            text.enabled = !text.enabled
            alphaAnimation = EaseInOutQuad(100, 1f, 0f, text.enabled)
        }

        val vg = VG(vgLong)

        vg.drawRoundedRect(
            x, y + 4, 24, 24,
            radius = 6,
            color = colorAnimation.getColor(hover, pressed)
        )
        vg.drawHollowRoundedRect(
            x, y + 4, 23.5f, 23.5f,
            radius = 6,
            color = Colors.GRAY_300,
            thickness = 1
        )
        vg.drawRoundedRect(
            x, y + 4, 24, 24,
            radius = 6,
            color = colorWithAlpha(Colors.PRIMARY_500)
        )
        SVGs.CHECKBOX_TICK?.let {
            vg.drawSVG(
                svg = it,
                x, y + 4, 24, 24,
                color = colorWithAlpha(0xFFFFFF)
            )
        }

        if (text.enabled && hover) vg.drawHollowRoundedRect(
            x - 1, y + 3, 24, 24,
            radius = 6,
            color = Colors.PRIMARY_600,
            thickness = 2
        )
    }

    private fun colorWithAlpha(color: Int): Int {
        val alpha = (alphaAnimation.get() * 255f).toInt()
        val rgb = color and 0xFFFFFF
        val shiftedAlpha = alpha shl 24
        return rgb or shiftedAlpha
    }
}