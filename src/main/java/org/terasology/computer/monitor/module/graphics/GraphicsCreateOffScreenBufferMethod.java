// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.joml.Vector2i;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Map;

public class GraphicsCreateOffScreenBufferMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public GraphicsCreateOffScreenBufferMethod(String methodName) {
        super("Creates Graphics Off Screen Buffer that can be used for fast rendering " +
                        "and buffering and later rendered on a display. Please note that this off screen buffer also " +
                        "acts as a Graphics Render Binding.",
                "GraphicsOffScreenBuffer", "Off screen buffer with the specified resolution.");
        this.methodName = methodName;

        addParameter("width", "Number", "Width of the Graphics Off Screen Buffer.");
        addParameter("height", "Number", "Height of the Graphics Off Screen Buffer.");

        addExample("This example creates an off screen buffer of maximum resolution accepted by the display below and " +
                        "draws fiver rectangles on it in different colors, then renders the buffer to the display. " +
                        "Please note that all the five rectangles appear on the display at the same time (buffering), " +
                        "also rendering like this is much faster than rendering one rectangle on the screen at a time. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                        "var buffer = graphicsMod.createOffScreenBuffer(width, height);\n" +
                        "var red = graphicsMod.createColor(\"ff0000\");\n" +
                        "for (var i=0; i<5; i++) {\n" +
                        "  graphicsMod.drawRectangle(buffer, 0, i*10, width, 5, red, false);\n" +
                        "}\n" +
                        "graphicsMod.renderBuffer(display, buffer);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 200;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult)
            throws ExecutionException {
        int width = FunctionParamValidationUtil.validateIntParameter(line, parameters, "width", methodName);
        int height = FunctionParamValidationUtil.validateIntParameter(line, parameters, "height", methodName);

        return new GraphicsOffScreenBuffer(new Vector2i(width, height));
    }
}
