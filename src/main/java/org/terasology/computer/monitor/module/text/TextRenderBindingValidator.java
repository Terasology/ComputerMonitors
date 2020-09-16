// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import org.terasology.modularcomputers.FunctionParamValidationUtil;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.CustomObject;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;

import java.util.Map;

public class TextRenderBindingValidator {
    private TextRenderBindingValidator() {
    }

    public static TextRenderCommandSink validateTextRenderBinding(
            int line, ComputerCallback computer, Map<String, Variable> parameters,
            String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName,
                functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("TEXT_RENDER_BINDING"))
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");

        TextRenderBinding binding = (TextRenderBinding) inventoryBinding.getValue();
        return binding.getTextRenderCommandSink(line, computer);
    }

    public static TextBuffer validateTextBuffer(int line, Map<String, Variable> parameters,
                                                String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName,
                functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("TEXT_BUFFER"))
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");

        return (TextBuffer) inventoryBinding.getValue();
    }
}
