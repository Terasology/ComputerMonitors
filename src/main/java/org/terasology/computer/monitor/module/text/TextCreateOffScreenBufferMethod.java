// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.joml.Vector2i;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Map;

public class TextCreateOffScreenBufferMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public TextCreateOffScreenBufferMethod(String methodName) {
        super("Creates a Text Off Screen Buffer that allows faster rendering and buffering. " +
                        "Please note that this off screen buffer also acts as a Text Render Binding.",
                "TextOffScreenBuffer", "Buffer that can be used to render text on and " +
                        "later displayed.");
        this.methodName = methodName;

        addParameter("width", "Number", "Width of the off screen buffer in characters.");
        addParameter("height", "Number", "Height of the off screen buffer in characters.");

        addExample("This example creates an off screen buffer with the size that the display below accepts, " +
                        "prints out \"Hello World!\" in each line of the buffer, then renders the buffer to " +
                        "the display. Please note, that all the text will be displayed at the same time " +
                        "on the display (buffering), and also that this process is much faster than displaying " +
                        "one line at the time on the display without the buffer. " +
                        "Please make sure this computer has a module of Text Graphics Card type in any of its slots.",
                "var textMod = computer.bindModuleOfType(\"" + TextOnlyGraphicsCardModuleCommonSystem.TEXT_GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var renderBinding = textMod.getRenderBinding(\"down\");\n" +
                        "var renderSize = textMod.getRenderSize(renderBinding);\n" +
                        "var width = renderSize[\"width\"];\n" +
                        "var height = renderSize[\"height\"];\n" +
                        "var buffer = textMod.createOffScreenBuffer(width, height);\n" +
                        "for (var y=0; y<height; y++) {\n" +
                        "  textMod.setCharacters(buffer, 0, y, \"Hello World!\");\n" +
                        "}\n" +
                        "textMod.renderBuffer(renderBinding, buffer);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 200;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        int width = FunctionParamValidationUtil.validateIntParameter(line, parameters, "width", methodName);
        int height = FunctionParamValidationUtil.validateIntParameter(line, parameters, "height", methodName);

        return new TextOffScreenBuffer(new Vector2i(width, height));
    }
}
