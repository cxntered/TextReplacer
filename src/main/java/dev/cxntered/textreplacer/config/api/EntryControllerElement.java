/*
 * This file is derived from EntryControllerElement.java in the McQoy mod.
 * Copyright (C) Sisby folk, 2025
 * Licensed under the European Union Public Licence v1.2 (EUPL v1.2).
 * Original source: https://github.com/sisby-folk/mcqoy/blob/44e2494/src/main/java/dev/sisby/mcqoy/yacl/EntryControllerElement.java
 *
 * Modifications by cxntered, 2025:
 *   - Added `isRendering` boolean to track if the element is currently rendering.
 *   - Overridden `charTyped` method to fix text input.
 *   - Overridden `setFocused` and `unfocus` methods to fix focus handling.
 *
 * In accordance with the compatibility clause of the EUPL v1.2,
 * this file is redistributed under the GNU General Public License v3 (GPLv3).
 * See the LICENSE file in the project root for details.
 */

package dev.cxntered.textreplacer.config.api;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import dev.isxander.yacl3.gui.controllers.TickBoxController;
import net.minecraft.client.gui.DrawContext;

public class EntryControllerElement<T> extends ControllerWidget<EntryController<T>> {
    public static boolean isRendering = false;
    private final AbstractWidget keyWidget;
    private final AbstractWidget valueWidget;

    public EntryControllerElement(EntryController<T> control, YACLScreen screen, Dimension<Integer> dim, AbstractWidget keyWidget, AbstractWidget valueWidget) {
        super(control, screen, dim);
        this.keyWidget = keyWidget;
        this.valueWidget = valueWidget;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return keyWidget.mouseClicked(mouseX, mouseY, button) || valueWidget.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return keyWidget.keyPressed(keyCode, scanCode, modifiers) || valueWidget.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return keyWidget.charTyped(chr, modifiers) || valueWidget.charTyped(chr, modifiers);
    }

    @Override
    public void setFocused(boolean focused) {
        keyWidget.setFocused(focused);
        valueWidget.setFocused(focused);
    }

    @Override
    public void unfocus() {
        keyWidget.unfocus();
        valueWidget.unfocus();
    }

    @Override
    public void render(DrawContext graphics, int mouseX, int mouseY, float delta) {
        isRendering = true;
        keyWidget.render(graphics, mouseX, mouseY, delta);
        valueWidget.render(graphics, mouseX, mouseY, delta);
        isRendering = false;
    }

    @Override
    public void setDimension(Dimension<Integer> dim) {
        super.setDimension(dim);
        if (valueWidget instanceof TickBoxController.TickBoxControllerElement) {
            this.keyWidget.setDimension(dim.withWidth(dim.width() - 20));
            this.valueWidget.setDimension(dim.withWidth(20).moved(dim.width() - 20, 0));
        } else {
            this.keyWidget.setDimension(dim.withWidth(dim.width() / 2));
            this.valueWidget.setDimension(dim.withWidth(dim.width() / 2).moved(dim.width() / 2, 0));
        }
    }

    @Override
    protected int getHoveredControlWidth() {
        return getUnhoveredControlWidth();
    }
}
