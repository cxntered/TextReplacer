package dev.cxntered.textreplacer.config;

import dev.cxntered.textreplacer.config.api.EntryController;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ModConfig {
    public static final ConfigClassHandler<ModConfig> CONFIG = ConfigClassHandler.createBuilder(ModConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(YACLPlatform.getConfigDir().resolve("textreplacer.json"))
                    .build())
            .build();

    @SerialEntry
    public boolean enabled = true;
    @SerialEntry
    public Map<String, String> replacements = new HashMap<>();

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Component.literal("TextReplacer"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.literal("Settings"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Component.literal("Enabled"))
                                .description(OptionDescription.of(Component.literal("Enable or disable the mod.")))
                                .binding(defaults.enabled, () -> config.enabled, newVal -> config.enabled = newVal)
                                .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Component.literal("Information"))
                                .option(LabelOption.create(
                                        Component.empty()
                                                .append(Component.literal("ⓘ ").withStyle(ChatFormatting.BLUE, ChatFormatting.BOLD))
                                                .append(Component.literal("Replacements are applied in the order they are listed!").withStyle(ChatFormatting.BLUE))
                                ))
                                .option(ButtonOption.createBuilder()
                                        .name(Component.literal("Copy Section Sign"))
                                        .description(OptionDescription.of(
                                                Component.literal("Use the section sign to use formatting codes.")))
                                        .action((screen, opt) -> {
                                            Minecraft.getInstance().keyboardHandler.setClipboard("§");
                                        })
                                        .text(Component.literal("✎"))
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(Component.literal("Copy Pilcrow"))
                                        .description(OptionDescription.of(
                                                Component.literal("Use the pilcrow to use variables.")
                                                        .append(Component.literal("\n\n"))
                                                        .append(Component.literal("Available variables:").withStyle(ChatFormatting.BOLD))
                                                        .append(Component.literal("\n"))
                                                        .append(Component.literal("username, serverIp, serverDomain")))
                                        )
                                        .action((screen, opt) -> {
                                            Minecraft.getInstance().keyboardHandler.setClipboard("¶");
                                        })
                                        .text(Component.literal("✎"))
                                        .build())
                                .build())
                        .group(ListOption.<Map.Entry<String, String>>createBuilder()
                                .name(Component.literal("Replacements"))
                                .description(OptionDescription.of(Component.literal("List of text replacements to apply")))
                                .customController(o -> new EntryController<>(
                                        o, StringControllerBuilder::create, StringControllerBuilder::create
                                ))
                                .binding(defaults.replacements.entrySet().stream().toList(), () -> config.replacements.entrySet().stream().toList(),
                                        newVal -> {
                                            config.replacements.clear();
                                            for (Map.Entry<String, String> entry : newVal) {
                                                config.replacements.put(entry.getKey(), entry.getValue());
                                            }
                                        })
                                .initial(new AbstractMap.SimpleEntry<>("", ""))
                                .insertEntriesAtEnd(true)
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
