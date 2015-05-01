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
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CreateGradientMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;

    public CreateGradientMethod(String methodName) {
        super("Creates a Paint object with a gradient defined.", "Paint", "Requested gradient paint.");
        this.methodName = methodName;

        addParameter("hex1", "String", "Hexadecimal value of the first color in the RRGGBBAA or RRGGBB format.");
        addParameter("x1", "Number", "X coordinate of the first color.");
        addParameter("y1", "Number", "Y coordinate of the first color.");
        addParameter("hex2", "String", "Hexadecimal value of the second color in the RRGGBBAA or RRGGBB format.");
        addParameter("x2", "Number", "X coordinate of the second color.");
        addParameter("y2", "Number", "Y coordinate of the second color.");
        addParameter("cyclic", "Boolean", "If the gradient should be cyclic or not.");

        addExample("This example draws a filled rectangle in the middle of the display with half of its width and height. " +
                        "The fill of the rectangle is an acyclic gradient spanning between top-left and bottom-right corners of the " +
                        "display, going from red to green color. Please note, that neither red, nor green colors will be " +
                        "displayed, because the bounds of the gradient are outside of the drawn shape. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                        "var gradient = graphicsMod.createGradient(\"ff0000\", 0, 0, \"00ff00\", width, height, false);\n" +
                        "graphicsMod.drawRectangle(display, width/4, height/4, width/2, height/2, gradient, true);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
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
