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
package org.terasology.computer.monitor.module;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.module.inventory.InventoryBinding;
import org.terasology.entitySystem.entity.EntityRef;

import java.util.Map;

public class RenderBindingValidator {
    private RenderBindingValidator() {
    }

    public static EntityRef validateRenderBinding(
            int line, ComputerCallback computer, Map<String, Variable> parameters,
            String parameterName, String functionName) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().equals("RENDER_BINDING"))
            throw new ExecutionException(line, "Invalid "+parameterName+" in "+functionName+"()");

        RenderBinding binding = (RenderBinding) inventoryBinding.getValue();
        return binding.getComputerMonitorEntity(line, computer);
    }


}
