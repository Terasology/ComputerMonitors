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

public class DrawTextMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public DrawTextMethod(String methodName) {
        super("Draws text on the Graphics Render Binding.");

        this.methodName = methodName;

        addParameter("graphicsRenderBinding", "GraphicsRenderBinding", "Binding to draw text on.");
        addParameter("text", "String", "Text to draw");
        addParameter("x", "Number", "X coordinate of the baseline for the font.");
        addParameter("y", "Number", "Y coordinate of the baseline for the font.");
        addParameter("font", "String", "Font to use (please note that if a player does not have the font it will be " +
                "drawn with the default one.");
        addParameter("fontSize", "Number", "Size of the font.");
        addParameter("paint", "Paint", "Paint to use to draw the text.");

        addExample("This example draws the \"Hello World!\" text on the display. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                        "var white = graphicsMod.createColor(\"ffffff\");\n" +
                        "graphicsMod.drawText(display, \"Hello World!\", 0, 15, \"Arial\", 12, white);"
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
        return renderCommandSink.isInstantRendering() ? 0 : 50;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink =
                GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, 
                        "renderBinding", methodName);
        String text = FunctionParamValidationUtil.validateStringParameter(line, parameters, "text", methodName);
        int x = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x", methodName);
        int y = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y", methodName);
        String font = FunctionParamValidationUtil.validateStringParameter(line, parameters, "font", methodName);
        int fontSize = FunctionParamValidationUtil.validateIntParameter(line, parameters, "fontSize", methodName);
        String paint = GraphicsRenderBindingValidator.validatePaint(line, parameters, "paint", methodName);

        List<String> existingData = renderCommandSink.getExistingData(line);

        List<String> newData = new ArrayList<>(existingData);
        newData.add("text:" + x + ":" + y + ":" + paint + ":" + font + ":" + fontSize + ":" + text);

        renderCommandSink.setData(line, newData);

        return null;
    }
}
