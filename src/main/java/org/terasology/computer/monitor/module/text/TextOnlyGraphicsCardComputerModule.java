// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import org.terasology.modularcomputers.module.DefaultComputerModule;
import org.terasology.multiBlock2.MultiBlockRegistry;

public class TextOnlyGraphicsCardComputerModule extends DefaultComputerModule {
    public TextOnlyGraphicsCardComputerModule(MultiBlockRegistry multiBlockRegistry, String moduleType,
                                              String moduleName) {
        super(moduleType, moduleName);

        addMethod("getRenderBinding", new TextRenderBindingMethod("getRenderBinding", multiBlockRegistry));
        addMethod("setCharacters", new TextSetCharactersMethod("setCharacters"));
        addMethod("clear", new TextClearMethod("clear"));
        addMethod("createOffScreenBuffer", new TextCreateOffScreenBufferMethod("createOffScreenBuffer"));
        addMethod("renderBuffer", new TextRenderBufferMethod("renderBuffer"));
        addMethod("getRenderSize", new GetTextRenderSizeMethod("getRenderSize"));
    }
}
