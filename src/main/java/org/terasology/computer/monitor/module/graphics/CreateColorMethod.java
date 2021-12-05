// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
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

public class CreateColorMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public CreateColorMethod(String methodName) {
        super("Creates a Paint object with a specified color.", "Paint", "Requested color paint.");
        this.methodName = methodName;

        addParameter("hex", "String", "Hexadecimal value of the color in the RRGGBBAA or RRGGBB format.");

        addExample("This example draws a red rectangle in the middle of the display with half of its width and height. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                        "var red = graphicsMod.createColor(\"ff0000\");\n" +
                        "graphicsMod.drawRectangle(display, width/4, height/4, width/2, height/2, red, false);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
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
