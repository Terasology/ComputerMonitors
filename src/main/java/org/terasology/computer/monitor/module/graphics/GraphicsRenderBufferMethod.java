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
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.math.Vector2i;

import java.util.Map;

public class GraphicsRenderBufferMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public GraphicsRenderBufferMethod(String methodName) {
        super("Renders a Graphics Off Screen Buffer to a Graphics Render Binding.");
        this.methodName = methodName;

        addParameter("graphicsRenderBinding", "GraphicsRenderBinding", "Binding to render the buffer on.");
        addParameter("graphicsOffScreenBuffer", "GraphicsOffScreenBuffer", "Buffer to render. Please note, that the resolution " +
                "of the buffer cannot exceed resolution of the binding.");

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
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", methodName);
        return renderCommandSink.isInstantRendering() ? 0 : 300;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", methodName);
        GraphicsBuffer graphicsBuffer = GraphicsRenderBindingValidator.validateGraphicsBuffer(line, parameters, "offScreenBuffer", methodName);

        Vector2i size = graphicsBuffer.getResolution();
        Vector2i maxCharacters = renderCommandSink.getResolution();

        if (size.x > maxCharacters.x || size.y > maxCharacters.y)
            throw new ExecutionException(line, "OffScreenBuffer does not fit on the screen");

        renderCommandSink.setData(line, graphicsBuffer.getData());

        return null;
    }
}
