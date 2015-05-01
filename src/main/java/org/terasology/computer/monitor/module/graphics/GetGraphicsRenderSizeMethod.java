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
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;
import org.terasology.math.Vector2i;

import java.util.HashMap;
import java.util.Map;

public class GetGraphicsRenderSizeMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public GetGraphicsRenderSizeMethod(String methodName) {
        super("Returns the size of the Graphics Render Binding.", "Map", "Returns a map with two keys - " +
                "\"width\" that has a value type of Number and contains width of the binding and " +
                "\"height\" that has a value type of Number and contains height of the binding.");
        this.methodName = methodName;

        addParameter("graphicsRenderBinding", "Graphics Render Binding", "Binding to get a size of.");

        addExample(                            "This example queries the size of the Graphics Render Binding created from a display with its " +
                        "maximum resolution then displays it. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);" +
                        "var renderSize = graphicsMod.getRenderSize(display);\n" +
                        "console.append(\"Width: \"+renderSize[\"width\"]+\", height: \"+renderSize[\"height\"]);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", methodName);

        Vector2i resolution = renderCommandSink.getResolution();

        Map<String, Variable> result = new HashMap<>();
        result.put("width", new Variable(resolution.x));
        result.put("height", new Variable(resolution.y));

        return result;
    }
}
