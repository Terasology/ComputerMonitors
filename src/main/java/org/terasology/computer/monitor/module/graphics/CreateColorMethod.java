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

public class CreateColorMethod implements ModuleMethodExecutable<Object> {

    private final String methodName;

    public CreateColorMethod(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"hex"};
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        String hex = FunctionParamValidationUtil.validateStringParameter(line, parameters, "hex", methodName);

        int[] c = ColorUtils.parseColor(line, hex);

        return new ColorCustomObject(c[0], c[1], c[2], c[3]);
    }

    private static class ColorCustomObject implements CustomObject, PaintCustomObject {
        private int r;
        private int g;
        private int b;
        private int a;

        private ColorCustomObject(int r, int g, int b, int a) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
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
            return "color(" + r + "," + g + "," + b + "," + a + ")";
        }
    }
}
