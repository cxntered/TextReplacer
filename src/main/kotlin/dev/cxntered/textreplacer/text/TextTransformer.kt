package dev.cxntered.textreplacer.text

import net.minecraft.text.OrderedText
import net.minecraft.text.PlainTextContent
import net.minecraft.text.StringVisitable
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.util.Optional

fun Text.replaceString(target: String, replacement: String): Text {
    // early exit if target/replacement is empty or if the flattened text does not contain the target
    if (target.isEmpty() || replacement.isEmpty() || !string.contains(target)) {
        return this
    }

    val content = (content as? PlainTextContent)?.string() ?: return this
    val text = Text.literal(content.replace(target, replacement)).setStyle(style)
    siblings.forEach { text.append(it.replaceString(target, replacement)) }
    return text
}

fun OrderedText.toText(): Text {
    return buildText { section, updateStyle ->
        accept { _, style: Style?, codePoint: Int ->
            updateStyle(style)
            section.appendCodePoint(codePoint)
            true
        }
    }
}

fun StringVisitable.toText(): Text {
    return when (this) {
        is Text -> this
        else -> buildText { section, updateStyle ->
            visit({ style: Style, string: String ->
                updateStyle(style)
                section.append(string)
                Optional.empty<Boolean>()
            }, Style.EMPTY)
        }
    }
}

private fun buildText(builder: (StringBuilder, (Style?) -> Unit) -> Unit): Text {
    val text = Text.empty()
    val currentSection = StringBuilder()
    var currentStyle: Style? = null

    fun flushSection() {
        if (currentSection.isNotEmpty()) {
            text.append(Text.literal(currentSection.toString()).setStyle(currentStyle))
            currentSection.setLength(0)
        }
    }

    fun updateStyle(newStyle: Style?) {
        if (currentStyle == null) {
            currentStyle = newStyle
        } else if (currentStyle !== newStyle) {
            flushSection()
            currentStyle = newStyle
        }
    }

    builder(currentSection) { newStyle -> updateStyle(newStyle) }
    flushSection()

    return text
}