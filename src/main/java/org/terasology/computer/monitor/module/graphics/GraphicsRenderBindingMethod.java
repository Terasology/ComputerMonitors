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
import org.terasology.engine.math.Direction;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Map;

public class GraphicsRenderBindingMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;
    private MultiBlockRegistry multiBlockRegistry;

    public GraphicsRenderBindingMethod(String methodName, MultiBlockRegistry multiBlockRegistry) {
        super("Returns Graphics Render Binding that allows to render graphics on a " +
                        "connected display. Specified resolution will be set on the device when binding is used.",
                "GraphicsRenderBinding", "Binding for the specified direction and resolution.");
        this.multiBlockRegistry = multiBlockRegistry;
        this.methodName = methodName;

        addParameter("direction", "Direction", "Direction of the binding in reference to computer.");
        addParameter("width", "Number", "Width of the graphics binding.");
        addParameter("height", "Number", "Height of the graphics binding.");

        addExample("This example gets render binding of the maximum size for the display below and clears the screen. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\",\n" +
                        "  maxRes[\"width\"], maxRes[\"height\"]);\n" +
                        "graphicsMod.clear(display);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 10;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        Direction direction = FunctionParamValidationUtil.validateDirectionParameter(line, parameters,
                "direction", methodName);

        int width = FunctionParamValidationUtil.validateIntParameter(line, parameters, "width", methodName);
        int height = FunctionParamValidationUtil.validateIntParameter(line, parameters, "height", methodName);


        return new RelativeLiveGraphicsRenderBindingCustomObject(multiBlockRegistry, direction, "Graphics:", width, height);
    }
}
