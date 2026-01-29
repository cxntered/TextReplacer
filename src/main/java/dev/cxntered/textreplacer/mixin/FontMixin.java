package dev.cxntered.textreplacer.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// TODO: replace text
@Mixin(Font.class)
public abstract class FontMixin {
    @ModifyVariable(
            //? if >=1.21.11 {
            method = "prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZZI)Lnet/minecraft/client/gui/Font$PreparedText;",
            //?} elif >=1.21.6 {
            /*method = "prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZI)Lnet/minecraft/client/gui/Font$PreparedText;",
            *///?} else
            /*method = "renderText(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)F",*/
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private FormattedCharSequence spoofPrepareText$FormattedCharSequence(FormattedCharSequence formattedCharSequence) {
        return formattedCharSequence;
    }

    @ModifyVariable(
            //? if >=1.21.6 {
            method = "prepareText(Ljava/lang/String;FFIZI)Lnet/minecraft/client/gui/Font$PreparedText;",
            //?} else
            //method = "renderText(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;IIZ)F",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private String spoofPrepareText$String(String string) {
        return string;
    }

    @ModifyVariable(
            method = "width(Lnet/minecraft/util/FormattedCharSequence;)I",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private FormattedCharSequence spoofWidth$FormattedCharSequence(FormattedCharSequence formattedCharSequence) {
        return formattedCharSequence;
    }

    @ModifyVariable(
            method = "width(Lnet/minecraft/network/chat/FormattedText;)I",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private FormattedText spoofWidth$FormattedText(FormattedText formattedText) {
        return formattedText;
    }

    @ModifyVariable(
            method = "width(Ljava/lang/String;)I",
            at = @At(value = "HEAD"),
            argsOnly = true
    )
    private String spoofWidth$String(String string) {
        return string;
    }
}
