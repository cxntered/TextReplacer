package dev.cxntered.textreplacer.mixin;

import dev.cxntered.textreplacer.TextReplacer;
import dev.cxntered.textreplacer.config.ModConfig;
import dev.cxntered.textreplacer.text.TextTransformerKt;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.ChatMessages;
import net.minecraft.text.StringVisitable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChatMessages.class)
public abstract class ChatMessagesMixin {
    @ModifyArg(
            method = "breakRenderedChatMessageLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/TextHandler;wrapLines(Lnet/minecraft/text/StringVisitable;ILnet/minecraft/text/Style;Ljava/util/function/BiConsumer;)V"
            ),
            index = 0
    )
    private static StringVisitable spoofChatMessageLine(StringVisitable stringVisitable) {
        if (ModConfig.CONFIG.instance().enabled && !(MinecraftClient.getInstance().currentScreen instanceof YACLScreen))
            return TextReplacer.getReplacedText(TextTransformerKt.toText(stringVisitable));
        return stringVisitable;
    }
}