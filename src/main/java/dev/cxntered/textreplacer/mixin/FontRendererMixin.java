package dev.cxntered.textreplacer.mixin;

import dev.cxntered.textreplacer.TextReplacer;
import dev.cxntered.textreplacer.config.TextReplacerConfig;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = FontRenderer.class, priority = 999)
public abstract class FontRendererMixin {
    @ModifyVariable(method = "renderString", at = @At("HEAD"), argsOnly = true)
    private String textreplacer$spoofRenderString(String string) {
        if (string == null) return null;
        if (!TextReplacerConfig.INSTANCE.enabled) return string;
        return TextReplacer.getString(string);
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true)
    private String textreplacer$spoofGetStringWidth(String string) {
        if (string == null) return null;
        if (!TextReplacerConfig.INSTANCE.enabled) return string;
        return TextReplacer.getString(string);
    }
}
