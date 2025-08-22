package dev.cxntered.textreplacer.config;

import dev.cxntered.textreplacer.config.api.EntryController;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.platform.YACLPlatform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
                .title(Text.literal("TextReplacer"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Settings"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Enabled"))
                                .description(OptionDescription.of(Text.literal("Enable or disable the mod.")))
                                .binding(defaults.enabled, () -> config.enabled, newVal -> config.enabled = newVal)
                                .controller(opt -> BooleanControllerBuilder.create(opt).coloured(true))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Information"))
                                .option(LabelOption.create(
                                        Text.empty()
                                                .append(Text.literal("ⓘ ").formatted(Formatting.BLUE, Formatting.BOLD))
                                                .append(Text.literal("Replacements are applied in the order they are listed!").formatted(Formatting.BLUE))
                                ))
                                .option(ButtonOption.createBuilder()
                                        .name(Text.literal("Copy Section Sign"))
                                        .description(OptionDescription.of(
                                                Text.literal("Use the section sign to use formatting codes.")))
                                        .action((screen, opt) -> {
                                            MinecraftClient.getInstance().keyboard.setClipboard("§");
                                        })
                                        .text(Text.literal("✎"))
                                        .build())
                                .option(ButtonOption.createBuilder()
                                        .name(Text.literal("Copy Pilcrow"))
                                        .description(OptionDescription.of(
                                                Text.literal("Use the pilcrow to use variables.")
                                                        .append(Text.literal("\n\n"))
                                                        .append(Text.literal("Available variables:").formatted(Formatting.BOLD))
                                                        .append(Text.literal("\n"))
                                                        .append(Text.literal("username, serverIp, serverDomain")))
                                        )
                                        .action((screen, opt) -> {
                                            MinecraftClient.getInstance().keyboard.setClipboard("¶");
                                        })
                                        .text(Text.literal("✎"))
                                        .build())
                                .build())
                        .group(ListOption.<Map.Entry<String, String>>createBuilder()
                                .name(Text.literal("Replacements"))
                                .description(OptionDescription.of(Text.literal("List of text replacements to apply")))
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
                                .build())
                        .build())
        )).generateScreen(parent);
    }
}
