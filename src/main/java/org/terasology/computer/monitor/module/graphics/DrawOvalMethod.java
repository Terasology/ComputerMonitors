// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import org.terasology.modularcomputers.FunctionParamValidationUtil;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawOvalMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public DrawOvalMethod(String methodName) {
        super("Draws or fills an oval with the specified bounds with specified Paint.");
        this.methodName = methodName;

        addParameter("graphicsRenderBinding", "GraphicsRenderBinding", "Binding to draw oval on.");
        addParameter("x", "Number", "X coordinate of the top-left corner of the bounding rectangle.");
        addParameter("y", "Number", "Y coordinate of the top-left corner of the bounding rectangle.");
        addParameter("width", "Number", "Width of the bounding rectangle.");
        addParameter("height", "Number", "Height of the bounding rectangle.");
        addParameter("paint", "Paint", "Paint to use to draw the oval.");
        addParameter("fill", "Boolean", "If the oval should also be filled with the paint.");

        addExample("This example draws a red oval with bounds the size of the whole display. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                        "var red = graphicsMod.createColor(\"ff0000\");\n" +
                        "graphicsMod.drawOval(display, 0, 0, width, height, red, false);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink =
                GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, 
                        "renderBinding", methodName);
        return renderCommandSink.isInstantRendering() ? 0 : 100;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink =
                GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, 
                        "renderBinding", methodName);
        int x = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x", methodName);
        int y = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y", methodName);
        int width = FunctionParamValidationUtil.validateIntParameter(line, parameters, "width", methodName);
        int height = FunctionParamValidationUtil.validateIntParameter(line, parameters, "height", methodName);
        String paint = GraphicsRenderBindingValidator.validatePaint(line, parameters, "paint", methodName);
        boolean fill = FunctionParamValidationUtil.validateBooleanParameter(line, parameters, "fill", methodName);

        List<String> existingData = renderCommandSink.getExistingData(line);

        List<String> newData = new ArrayList<>(existingData);
        newData.add("drawOval:" + x + ":" + y + ":" + width + ":" + height + ":" + paint + ":" + fill);

        renderCommandSink.setData(line, newData);

        return null;
    }
}
