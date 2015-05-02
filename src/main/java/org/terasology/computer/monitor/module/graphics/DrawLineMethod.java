/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.computer.monitor.module.graphics;

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawLineMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public DrawLineMethod(String methodName) {
        super("Draws a line between two points with the specified Paint and width.");
        this.methodName = methodName;

        addParameter("graphicsRenderBinding", "GraphicsRenderBinding", "Binding to draw line on.");
        addParameter("x1", "Number", "X coordinate of the first point.");
        addParameter("y1", "Number", "Y coordinate of the first point.");
        addParameter("x2", "Number", "X coordinate of the second point.");
        addParameter("y2", "Number", "Y coordinate of the second point.");
        addParameter("paint", "Paint", "Paint to use to draw the line.");
        addParameter("width", "Number", "Width of the line to draw.");

        addExample("This example draws a blue line from top-left corner to bottom-right corner of the display with a width of " +
                        "two pixels. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                        "var blue = graphicsMod.createColor(\"0000ff\");\n" +
                        "graphicsMod.drawLine(display, 0, 0, width, height, blue, 2);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", methodName);
        return renderCommandSink.isInstantRendering() ? 0 : 100;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", methodName);
        int x1 = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x1", methodName);
        int y1 = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y1", methodName);
        int x2 = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x2", methodName);
        int y2 = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y2", methodName);
        String paint = GraphicsRenderBindingValidator.validatePaint(line, parameters, "paint", methodName);
        int lineWidth = FunctionParamValidationUtil.validateIntParameter(line, parameters, "width", methodName);

        List<String> existingData = renderCommandSink.getExistingData(line);

        List<String> newData = new ArrayList<>(existingData);
        newData.add("drawLine:" + x1 + ":" + y1 + ":" + x2 + ":" + y2 + ":" + paint + ":" + lineWidth);

        renderCommandSink.setData(line, newData);

        return null;
    }
}
