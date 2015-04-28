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
package org.terasology.computer.monitor.module.text;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;

import java.util.Map;

public class TextRenderBindingValidator {
    private TextRenderBindingValidator() {
    }

    public static TextRenderCommandSink validateTextRenderBinding(
            int line, ComputerCallback computer, Map<String, Variable> parameters,
            String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("TEXT_RENDER_BINDING"))
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");

        TextRenderBinding binding = (TextRenderBinding) inventoryBinding.getValue();
        return binding.getTextRenderCommandSink(line, computer);
    }

    public static TextBuffer validateTextBuffer(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                                String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("TEXT_BUFFER"))
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");

        return (TextBuffer) inventoryBinding.getValue();
    }
}
