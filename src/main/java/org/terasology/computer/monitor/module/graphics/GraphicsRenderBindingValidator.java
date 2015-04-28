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

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;

import java.util.Map;

public class GraphicsRenderBindingValidator {
    private GraphicsRenderBindingValidator() {
    }

    public static GraphicsRenderCommandSink validateGraphicsRenderBinding(
            int line, ComputerCallback computer, Map<String, Variable> parameters,
            String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("GRAPHICS_RENDER_BINDING"))
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");

        GraphicsRenderBinding binding = (GraphicsRenderBinding) inventoryBinding.getValue();
        return binding.getGraphicsRenderCommandSink(line, computer);
    }

    public static GraphicsBuffer validateGraphicsBuffer(int line, Map<String, Variable> parameters,
                                                String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("GRAPHICS_BUFFER"))
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");

        return (GraphicsBuffer) inventoryBinding.getValue();
    }

    public static String validatePaint(int line, Map<String, Variable> parameters,
                                       String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("PAINT"))
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");

        PaintCustomObject paint = (PaintCustomObject) inventoryBinding.getValue();
        return paint.getPaintDescription();
    }
}
