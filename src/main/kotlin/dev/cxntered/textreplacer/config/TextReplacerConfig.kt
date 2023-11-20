package dev.cxntered.textreplacer.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.CustomOption
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.config.elements.OptionPage
import dev.cxntered.textreplacer.TextReplacer
import dev.cxntered.textreplacer.elements.ReplacerListOption
import dev.cxntered.textreplacer.elements.WrappedReplacer
import java.lang.reflect.Field

object TextReplacerConfig : Config(
        Mod(
                TextReplacer.NAME,
                ModType.UTIL_QOL
        ), "${TextReplacer.MODID}.json"
) {
    @CustomOption
    private var entries: Array<Replacer> = emptyArray()

    override fun getCustomOption(
            field: Field,
            annotation: CustomOption,
            page: OptionPage,
            mod: Mod,
            migrate: Boolean
    ): BasicOption {
        val option = ReplacerListOption
        ConfigUtils.getSubCategory(page, "General", "").options.add(option)
        return option
    }

    override fun load() {
        super.load()

        ReplacerListOption.wrappedReplacers = entries.mapTo(mutableListOf()) { replacer ->
            WrappedReplacer(replacer)
        }
    }

    override fun save() {
        entries = ReplacerListOption.wrappedReplacers.map { wrapped ->
            wrapped.replacer
        }.toTypedArray()

        super.save()
    }

    init {
        initialize()
    }
}