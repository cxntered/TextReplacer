package dev.cxntered.textreplacer.config

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.CustomOption
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.data.InfoType
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.data.ModType
import cc.polyfrost.oneconfig.config.data.OptionSize
import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.config.elements.OptionPage
import dev.cxntered.textreplacer.TextReplacer
import dev.cxntered.textreplacer.elements.ReplacerListOption
import dev.cxntered.textreplacer.elements.WrappedReplacer
import java.awt.Toolkit
import java.lang.reflect.Field

object TextReplacerConfig : Config(
    Mod(
        TextReplacer.NAME,
        ModType.UTIL_QOL,
        "/TextReplacer.svg"
    ), "${TextReplacer.MODID}.json"
) {
    @Info(
        text = "Replacements are applied in the order they are listed!",
        type = InfoType.INFO,
        size = OptionSize.DUAL
    )
    private var info = false

    @Button(name = "Use the Section Sign (§) to use formatting codes!", text = "Copy", subcategory = "Info")
    var copySectionSign = Runnable {
        Toolkit.getDefaultToolkit().systemClipboard.setContents(java.awt.datatransfer.StringSelection("§"), null)
    }

    @Button(name = "Use the Pilcrow (¶) to use variables!", text = "Copy", subcategory = "Info")
    var copyPilcrow = Runnable {
        Toolkit.getDefaultToolkit().systemClipboard.setContents(java.awt.datatransfer.StringSelection("¶"), null)
    }

    @Info(
        text = "Available variables: username, serverIp, serverDomain, hypixelScoreboardIp",
        type = InfoType.INFO,
        subcategory = "Info",
        size = OptionSize.DUAL
    )
    private var info2 = false

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
}