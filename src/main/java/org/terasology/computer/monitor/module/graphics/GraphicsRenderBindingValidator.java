// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;

import java.util.Map;

public final class GraphicsRenderBindingValidator {
    private GraphicsRenderBindingValidator() {
    }

    public static GraphicsRenderCommandSink validateGraphicsRenderBinding(
            int line, ComputerCallback computer, Map<String, Variable> parameters,
            String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(
                line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("GRAPHICS_RENDER_BINDING")) {
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");
        }

        GraphicsRenderBinding binding = (GraphicsRenderBinding) inventoryBinding.getValue();
        return binding.getGraphicsRenderCommandSink(line, computer);
    }

    public static GraphicsBuffer validateGraphicsBuffer(int line, Map<String, Variable> parameters,
                                                        String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(
                line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("GRAPHICS_BUFFER")) {
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");
        }

        return (GraphicsBuffer) inventoryBinding.getValue();
    }

    public static String validatePaint(int line, Map<String, Variable> parameters,
                                       String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(
                line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("PAINT")) {
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");
        }

        PaintCustomObject paint = (PaintCustomObject) inventoryBinding.getValue();
        return paint.getPaintDescription();
    }
}
