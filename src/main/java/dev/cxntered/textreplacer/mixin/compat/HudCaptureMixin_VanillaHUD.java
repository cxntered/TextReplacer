package dev.cxntered.textreplacer.mixin.compat;

import dev.cxntered.textreplacer.TextReplacer;
import dev.cxntered.textreplacer.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// vanillahud captures text in TextRenderer#draw and renders it through compose before our drawLayer hook
@Pseudo
@Mixin(targets = "org.polyfrost.vanillahud.render.HudCapture", remap = false)
abstract class HudCaptureMixin_VanillaHUD {
    @ModifyVariable(method = "text(Lorg/polyfrost/vanillahud/compat/LegacyDrawContext;Ljava/lang/String;IIIZ)Z", at = @At("HEAD"), argsOnly = true, require = 0)
    private static String spoofText(String string) {
        if (string == null) return null;
        if (!ModConfig.isEnabled()) return string;
        return TextReplacer.getString(string);
    }
}
