package dev.cxntered.textreplacer.match

internal object FilteringReplacer {
    private const val FORMATTING_CODE = '§'

    // ignored chars inside a match are skipped
    fun replace(
        text: String,
        target: String,
        replacement: String,
        ignoreFormattingCodes: Boolean,
        ignoreInvalidCharacters: Boolean
    ): String {
        if (!ignoreFormattingCodes && !ignoreInvalidCharacters) return text.replace(target, replacement)

        // 0 when visible, otherwise how many chars to skip
        fun ignoredLength(source: String, index: Int): Int {
            return when {
                ignoreFormattingCodes && index + 1 < source.length &&
                        source[index] == FORMATTING_CODE && isFormattingCode(source[index + 1]) -> 2
                ignoreInvalidCharacters && isInvalidCharacter(source[index]) -> 1
                else -> 0
            }
        }

        // -1 when nothing matches at start
        fun matchEnd(start: Int): Int {
            var targetIndex = 0
            var textIndex = start
            while (targetIndex < target.length) {
                val targetIgnored = ignoredLength(target, targetIndex)
                if (targetIgnored != 0) {
                    targetIndex += targetIgnored
                    continue
                }

                if (textIndex >= text.length) return -1
                val textIgnored = ignoredLength(text, textIndex)
                if (textIgnored != 0) {
                    textIndex += textIgnored
                    continue
                }

                if (text[textIndex] != target[targetIndex]) return -1
                targetIndex++
                textIndex++
            }

            // if target contains only ignored chars, it matches nothing
            return if (textIndex > start) textIndex else -1
        }

        val hasIgnored = text.indices.any { ignoredLength(text, it) != 0 } ||
                target.indices.any { ignoredLength(target, it) != 0 }
        if (!hasIgnored) return text.replace(target, replacement)

        var result: StringBuilder? = null
        var lastMatchEnd = 0
        var index = 0
        while (index < text.length) {
            val ignored = ignoredLength(text, index)
            if (ignored != 0) {
                index += ignored
                continue
            }

            val end = matchEnd(index)
            if (end == -1) {
                index++
                continue
            }

            if (result == null) result = StringBuilder(text.length)
            result.append(text, lastMatchEnd, index).append(replacement)
            lastMatchEnd = end
            index = end
        }

        return result?.append(text, lastMatchEnd, text.length)?.toString() ?: text
    }

    private fun isFormattingCode(char: Char): Boolean {
        val lower = char.lowercaseChar()
        return lower in '0'..'9' || lower in 'a'..'f' || lower in 'k'..'o' || lower == 'r'
    }

    private fun isInvalidCharacter(char: Char): Boolean = when (char.category) {
        CharCategory.FORMAT,
        CharCategory.SURROGATE,
        CharCategory.PRIVATE_USE,
        CharCategory.UNASSIGNED -> true
        else -> false
    }
}
