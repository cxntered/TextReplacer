package dev.cxntered.textreplacer.mixin;

import net.minecraft.client.gui.components.ComponentRenderUtils;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ComponentRenderUtils.class)
public abstract class ComponentRenderUtilsMixin {
    @ModifyArg(
            method = "wrapComponents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/StringSplitter;splitLines(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/network/chat/Style;Ljava/util/function/BiConsumer;)V"
            ),
            index = 0
    )
    private static FormattedText spoofWrapComponents(FormattedText formattedText) {
        // TODO: replace text
        return formattedText;
    }
}