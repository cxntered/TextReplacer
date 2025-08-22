package dev.cxntered.textreplacer.mixin;

import dev.cxntered.textreplacer.TextReplacer;
import dev.cxntered.textreplacer.config.ModConfig;
import dev.cxntered.textreplacer.text.TextTransformerKt;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextRenderer.class)
public abstract class TextRendererMixin {
    @ModifyVariable(
            method = "prepare(Lnet/minecraft/text/OrderedText;FFIZI)Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private OrderedText spoofPrepareOrderedText(OrderedText orderedText) {
        if (ModConfig.CONFIG.instance().enabled && !(MinecraftClient.getInstance().currentScreen instanceof YACLScreen))
            return TextReplacer.getReplacedText(TextTransformerKt.toText(orderedText)).asOrderedText();
        return orderedText;
    }

    @ModifyVariable(
            method = "prepare(Ljava/lang/String;FFIZI)Lnet/minecraft/client/font/TextRenderer$GlyphDrawable;",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private String spoofPrepareString(String string) {
        if (ModConfig.CONFIG.instance().enabled && !(MinecraftClient.getInstance().currentScreen instanceof YACLScreen))
            return TextReplacer.getReplacedString(string);
        return string;
    }

    @ModifyVariable(
            method = "getWidth(Lnet/minecraft/text/OrderedText;)I",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private OrderedText spoofGetWidthOrderedText(OrderedText orderedText) {
        if (ModConfig.CONFIG.instance().enabled && !(MinecraftClient.getInstance().currentScreen instanceof YACLScreen))
            return TextReplacer.getReplacedText(TextTransformerKt.toText(orderedText)).asOrderedText();
        return orderedText;
    }

    @ModifyVariable(
            method = "getWidth(Lnet/minecraft/text/StringVisitable;)I",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private StringVisitable spoofGetWidthStringVisitable(StringVisitable stringVisitable) {
        if (ModConfig.CONFIG.instance().enabled && !(MinecraftClient.getInstance().currentScreen instanceof YACLScreen))
            return TextReplacer.getReplacedText(TextTransformerKt.toText(stringVisitable));
        return stringVisitable;
    }

    @ModifyVariable(
            method = "getWidth(Ljava/lang/String;)I",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private String spoofGetWidthString(String string) {
        if (ModConfig.CONFIG.instance().enabled && !(MinecraftClient.getInstance().currentScreen instanceof YACLScreen))
            return TextReplacer.getReplacedString(string);
        return string;
    }
}
