package dev.cxntered.textreplacer.mixin;

import dev.cxntered.textreplacer.TextReplacer;
import dev.cxntered.textreplacer.config.ModConfig;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = TextRenderer.class, priority = 999)
abstract class TextRendererMixin {
    @ModifyVariable(method = "drawLayer(Ljava/lang/String;FFIZ)I", at = @At("HEAD"), argsOnly = true)
    private String spoofDrawLayer(String string) {
        if (string == null) return null;
        if (!ModConfig.isEnabled()) return string;
        return TextReplacer.getString(string);
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true)
    private String spoofGetStringWidth(String string) {
        if (string == null) return null;
        if (!ModConfig.isEnabled()) return string;
        return TextReplacer.getString(string);
    }
}
