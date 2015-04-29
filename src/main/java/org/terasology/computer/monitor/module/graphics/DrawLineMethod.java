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
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawLineMethod implements ModuleMethodExecutable<Object> {

    private final String methodName;

    public DrawLineMethod(String methodName) {
        this.methodName = methodName;
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
    public String[] getParameterNames() {
        return new String[]{"renderBinding", "x1", "y1", "x2", "y2", "paint", "width"};
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
