package dev.cxntered.textreplacer.mixin;

import dev.cxntered.textreplacer.TextReplacer;
import dev.cxntered.textreplacer.config.Replacer;
import dev.cxntered.textreplacer.config.TextReplacerConfig;
import dev.cxntered.textreplacer.elements.WrappedReplacer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FontRenderer.class, priority = 999)
public abstract class FontRendererMixin {
    @ModifyVariable(method = "renderString", at = @At("HEAD"), argsOnly = true)
    private String textreplacer$spoofRenderString(String string) {
        return TextReplacer.INSTANCE.getString(string);
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true)
    private String textreplacer$spoofGetStringWidth(String string) {
        return TextReplacer.INSTANCE.getString(string);
    }
}
