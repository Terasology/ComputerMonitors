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

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.monitor.component.ComputerMonitorComponent;
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;
import org.terasology.entitySystem.entity.EntityRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SetCharactersMethod implements ModuleMethodExecutable<Object> {
    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime() {
        return 100;
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"renderBinding", "x", "y", "text"};
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        EntityRef displayEntity = RenderBindingValidator.validateRenderBinding(line, computer, parameters, "renderBinding", "setCharacters");

        int x = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x", "setCharacters");
        int y = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y", "setCharacters");

        String text = FunctionParamValidationUtil.validateStringParameter(line, parameters, "text", "setCharacters");

        ComputerMonitorComponent monitor = displayEntity.getComponent(ComputerMonitorComponent.class);

        if (x + text.length() >= monitor.getCharactersInLineCount())
            throw new ExecutionException(line, "Text will not fit in the display horizontally");

        int lineCount = monitor.getLineCount();
        if (y >= lineCount)
            throw new ExecutionException(line, "Line index out of bounds " + y + ">=" + lineCount);

        List<String> oldLines = monitor.getLines();

        List<String> result = new ArrayList<>(lineCount);
        for (int i=0; i< lineCount; i++) {
            String oldLine = getLine(oldLines, i);
            if (i==y) {
                String before = getBefore(oldLine, x);
                String after = getAfter(oldLine, x + text.length());
                result.add(before+text+after);
            } else {
                result.add(oldLine);
            }
        }

        monitor.setLines(result);

        displayEntity.saveComponent(monitor);

        return null;
    }

    private String getAfter(String line, int i) {
        if (line.length() < i) {
            return "";
        }
        return line.substring(i);
    }

    private String getBefore(String line, int x) {
        if (line.length() < x) {
            int lineLength = line.length();
            StringBuilder sb = new StringBuilder(line);
            for (int i = 0; i < lineLength - x; i++) {
                sb.append(" ");
            }
            return sb.toString();
        }
        return line.substring(0, x);
    }

    private String getLine(List<String> lines, int y) {
        if (y<lines.size()) {
            String line = lines.get(y);
            if (line == null)
                return "";
            return line;
        }
        return "";
    }
}
