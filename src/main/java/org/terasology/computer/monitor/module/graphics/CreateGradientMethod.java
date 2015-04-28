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

        int hexParsed1;
        int hexParsed2;
        try {
            hexParsed1 = Integer.parseInt(hex1, 16);
            hexParsed2 = Integer.parseInt(hex2, 16);
        } catch (NumberFormatException exp) {
            throw new ExecutionException(line, "Invalid format of the RGBA hex color.");
        }

        int r1 = (hexParsed1 & 0xff000000) >> 24;
        int g1 = (hexParsed1 & 0x00ff0000) >> 16;
        int b1 = (hexParsed1 & 0x0000ff00) >> 8;
        int a1 = (hexParsed1 & 0x000000ff);

        int r2 = (hexParsed2 & 0xff000000) >> 24;
        int g2 = (hexParsed2 & 0x00ff0000) >> 16;
        int b2 = (hexParsed2 & 0x0000ff00) >> 8;
        int a2 = (hexParsed2 & 0x000000ff);

        return new GradientCustomObject(r1, g1, b1, a1, x1, y1, r2, g2, b2, a2, x2, y2, cyclic);
    }

    private static class GradientCustomObject implements CustomObject, PaintCustomObject {
        private final int r1;
        private final int g1;
        private final int b1;
        private final int a1;
        private final int x1;
        private final int y1;
        private final int r2;
        private final int g2;
        private final int b2;
        private final int a2;
        private final int x2;
        private final int y2;
        private final boolean cyclic;

        private GradientCustomObject(int r1, int g1, int b1, int a1, int x1, int y1,
                                     int r2, int g2, int b2, int a2, int x2, int y2,
                                     boolean cyclic) {
            this.r1 = r1;
            this.g1 = g1;
            this.b1 = b1;
            this.a1 = a1;
            this.x1 = x1;
            this.y1 = y1;
            this.r2 = r2;
            this.g2 = g2;
            this.b2 = b2;
            this.a2 = a2;
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
                    r1 + "," + g1 + "," + b1 + "," + a1 + "," + x1 + "," + y1 + "," +
                    r2 + "," + g2 + "," + b2 + "," + a2 + "," + x2 + "," + y2 + "," + cyclic + ")";
        }
    }
}
