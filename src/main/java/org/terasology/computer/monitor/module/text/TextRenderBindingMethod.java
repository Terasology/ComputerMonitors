// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import org.terasology.engine.math.Direction;
import org.terasology.modularcomputers.FunctionParamValidationUtil;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Map;

public class TextRenderBindingMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;
    private final MultiBlockRegistry multiBlockRegistry;

    public TextRenderBindingMethod(String methodName, MultiBlockRegistry multiBlockRegistry) {
        super("Creates a Text Render Binding that allows to render text on a display.",
                "TextRenderBinding", "Binding that can be used to render text on.");
        this.multiBlockRegistry = multiBlockRegistry;
        this.methodName = methodName;

        addParameter("direction", "Direction", "Specifies direction of the display relative to computer.");

        addExample("This example gets render binding for the display below the computer and prints the display's " +
                        "width and height available in characters. Please make sure " +
                        "this computer has a module of Text Graphics Card type in any of its slots.",
                "var textMod = computer.bindModuleOfType(\"" + TextOnlyGraphicsCardModuleCommonSystem.TEXT_GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var renderBinding = textMod.getRenderBinding(\"down\");\n" +
                        "var renderSize = textMod.getRenderSize(renderBinding);\n" +
                        "console.append(\"The render size is \"+renderSize[\"width\"]+\n" +
                        "  \" by \"+renderSize[\"height\"]+\" characters.\");"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 10;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        Direction direction = FunctionParamValidationUtil.validateDirectionParameter(line, parameters,
                "direction", methodName);

        return new RelativeLiveTextRenderBindingCustomObject(multiBlockRegistry, direction, "Text:");
    }
}
