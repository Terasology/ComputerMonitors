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

public class DrawTextMethod implements ModuleMethodExecutable<Object> {
    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", "drawText");
        return renderCommandSink.isInstantRendering() ? 0 : 50;
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"renderBinding", "text", "x", "y", "font", "fontSize", "paint"};
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", "drawText");
        String text = FunctionParamValidationUtil.validateStringParameter(line, parameters, "text", "drawText");
        int x = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x", "drawText");
        int y = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y", "drawText");
        String font = FunctionParamValidationUtil.validateStringParameter(line, parameters, "font", "drawText");
        int fontSize = FunctionParamValidationUtil.validateIntParameter(line, parameters, "fontSize", "drawText");
        String paint = GraphicsRenderBindingValidator.validatePaint(line, parameters, "paint", "drawText");

        List<String> existingData = renderCommandSink.getExistingData();

        List<String> newData = new ArrayList<>(existingData);
        newData.add("text:" + x + ":" + y + ":" + paint + ":" + font + ":" + fontSize + ":" + text);

        renderCommandSink.setData(newData);

        return null;
    }
}
