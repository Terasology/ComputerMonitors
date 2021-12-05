// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawRectangleMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public DrawRectangleMethod(String methodName) {
        super("Draws or fills specified rectangle with specified Paint.");
        this.methodName = methodName;

        addParameter("graphicsRenderBinding", "GraphicsRenderBinding", "Binding to draw rectangle on.");
        addParameter("x", "Number", "X coordinate of the top-left corner of the rectangle.");
        addParameter("y", "Number", "Y coordinate of the top-left corner of the rectangle.");
        addParameter("width", "Number", "Width of the rectangle.");
        addParameter("height", "Number", "Height of the rectangle.");
        addParameter("paint", "Paint", "Paint to use to draw the rectangle.");
        addParameter("fill", "Boolean", "If the rectangle should also be filled with the paint.");

        addExample("This example draws a red rectangle in the middle of the display with half of its width and height. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                        "var red = graphicsMod.createColor(\"ff0000\");\n" +
                        "graphicsMod.drawRectangle(display, width/4, height/4, width/2, height/2, red, false);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer,
                parameters, "renderBinding", methodName);
        return renderCommandSink.isInstantRendering() ? 0 : 100;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer,
                parameters, "renderBinding", methodName);
        int x = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x", methodName);
        int y = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y", methodName);
        int width = FunctionParamValidationUtil.validateIntParameter(line, parameters, "width", methodName);
        int height = FunctionParamValidationUtil.validateIntParameter(line, parameters, "height", methodName);
        String paint = GraphicsRenderBindingValidator.validatePaint(line, parameters, "paint", methodName);
        boolean fill = FunctionParamValidationUtil.validateBooleanParameter(line, parameters, "fill", methodName);

        List<String> existingData = renderCommandSink.getExistingData(line);

        List<String> newData = new ArrayList<>(existingData);
        newData.add("drawRect:" + x + ":" + y + ":" + width + ":" + height + ":" + paint + ":" + fill);

        renderCommandSink.setData(line, newData);

        return null;
    }
}
