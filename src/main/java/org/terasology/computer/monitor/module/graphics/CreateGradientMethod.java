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
import org.terasology.computer.monitor.module.ColorUtils;
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CreateGradientMethod implements ModuleMethodExecutable<Object> {

    private final String methodName;

    public CreateGradientMethod(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"hex1", "x1", "y1", "hex2", "x2", "y2", "cyclic"};
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        String hex1 = FunctionParamValidationUtil.validateStringParameter(line, parameters, "hex1", methodName);
        String hex2 = FunctionParamValidationUtil.validateStringParameter(line, parameters, "hex2", methodName);

        int x1 = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x1", methodName);
        int y1 = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y1", methodName);

        int x2 = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x2", methodName);
        int y2 = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y2", methodName);

        boolean cyclic = FunctionParamValidationUtil.validateBooleanParameter(line, parameters, "cyclic", methodName);

        int[] c1 = ColorUtils.parseColor(line, hex1);
        int[] c2 = ColorUtils.parseColor(line, hex2);

        return new GradientCustomObject(c1, x1, y1, c2, x2, y2, cyclic);
    }

    private static class GradientCustomObject implements CustomObject, PaintCustomObject {
        private final int[] c1;
        private final int x1;
        private final int y1;
        private final int[] c2;
        private final int x2;
        private final int y2;
        private final boolean cyclic;

        private GradientCustomObject(int[] c1, int x1, int y1, int[] c2, int x2, int y2, boolean cyclic) {
            this.c1 = c1;
            this.x1 = x1;
            this.y1 = y1;
            this.c2 = c2;
            this.x2 = x2;
            this.y2 = y2;
            this.cyclic = cyclic;
        }

        @Override
        public Collection<String> getType() {
            return Collections.singleton("PAINT");
        }

        @Override
        public int sizeOf() {
            return 16;
        }

        @Override
        public String getPaintDescription() {
            return "gradient(" +
                    c1[0] + "," + c1[1] + "," + c1[2] + "," + c1[3] + "," + x1 + "," + y1 + "," +
                    c2[0] + "," + c2[1] + "," + c2[2] + "," + c2[3] + "," + x2 + "," + y2 + "," + cyclic + ")";
        }
    }
}
