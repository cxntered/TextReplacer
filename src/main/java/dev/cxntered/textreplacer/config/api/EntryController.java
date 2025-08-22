/*
 * This file is derived from EntryController.java in the McQoy mod.
 * Copyright (C) Sisby folk, 2025
 * Licensed under the European Union Public Licence v1.2 (EUPL v1.2).
 * Original source: https://github.com/sisby-folk/mcqoy/blob/44e2494/src/main/java/dev/sisby/mcqoy/yacl/EntryController.java
 *
 * In accordance with the compatibility clause of the EUPL v1.2,
 * this file is redistributed under the GNU General Public License v3 (GPLv3).
 * See the LICENSE file in the project root for details.
 */

package dev.cxntered.textreplacer.config.api;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public class EntryController<T> implements Controller<Map.Entry<String, T>> {
    private final Option<Map.Entry<String, T>> option;
    private final Controller<String> keyController;
    private final Controller<T> valueController;

    public EntryController(Option<Map.Entry<String, T>> option, @NotNull Function<Option<String>, ControllerBuilder<String>> keyController, @NotNull Function<Option<T>, ControllerBuilder<T>> valueController) {
        this.option = option;
        this.keyController = new MapKeyOption<>(option, keyController).controller();
        this.valueController = new MapValueOption<>(option, valueController).controller();
    }

    @Override
    public Option<Map.Entry<String, T>> option() {
        return option;
    }

    @Override
    public Text formatValue() {
        return Text.literal(option.pendingValue().toString());
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        return new EntryControllerElement<>(this, screen, widgetDimension, keyController.provideWidget(screen, widgetDimension), valueController.provideWidget(screen, widgetDimension));
    }
}
