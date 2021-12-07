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

public class DrawRoundedRectangleMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public DrawRoundedRectangleMethod(String methodName) {
        super("Draws or fills specified rounded rectangle with specified Paint.");
        this.methodName = methodName;

        addParameter("graphicsRenderBinding", "GraphicsRenderBinding", "Binding to draw rounded rectangle on.");
        addParameter("x", "Number", "X coordinate of the top-left corner of the rounded rectangle.");
        addParameter("y", "Number", "Y coordinate of the top-left corner of the rounded rectangle.");
        addParameter("width", "Number", "Width of the rounded rectangle.");
        addParameter("height", "Number", "Height of the rounded rectangle.");
        addParameter("arcWidth", "Number", "Width of the arc used to round the rectangle.");
        addParameter("arcHeight", "Number", "Height of the arc used to round the rectangle.");
        addParameter("paint", "Paint", "Paint to use to draw the rounded rectangle.");
        addParameter("fill", "Boolean", "If the rounded rectangle should also be filled with the paint.");

        addExample("This example draws a red rounded rectangle in the middle of the display with half of its width and height. The " +
                        "arc of the rounding is 1/8 of the width and height respectively. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                        "var red = graphicsMod.createColor(\"ff0000\");\n" +
                        "graphicsMod.drawRoundedRectangle(display, width/4, height/4, width/2, height/2, width/8, height/8, red, false);"
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
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult)
            throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer,
                parameters, "renderBinding", methodName);
        int x = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x", methodName);
        int y = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y", methodName);
        int width = FunctionParamValidationUtil.validateIntParameter(line, parameters, "width", methodName);
        int height = FunctionParamValidationUtil.validateIntParameter(line, parameters, "height", methodName);
        int arcWidth = FunctionParamValidationUtil.validateIntParameter(line, parameters, "arcWidth", methodName);
        int arcHeight = FunctionParamValidationUtil.validateIntParameter(line, parameters, "arcHeight", methodName);
        String paint = GraphicsRenderBindingValidator.validatePaint(line, parameters, "paint", methodName);
        boolean fill = FunctionParamValidationUtil.validateBooleanParameter(line, parameters, "fill", methodName);

        List<String> existingData = renderCommandSink.getExistingData(line);

        List<String> newData = new ArrayList<>(existingData);
        newData.add("drawRoundRect:" + x + ":" + y + ":" + width + ":" + height + ":" + arcWidth + ":" + arcHeight + ":" + paint + ":" + fill);

        renderCommandSink.setData(line, newData);

        return null;
    }
}
