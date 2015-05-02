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

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.monitor.module.graphics.GraphicsRenderBindingValidator;
import org.terasology.computer.monitor.module.graphics.GraphicsRenderCommandSink;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.math.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextSetCharactersMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public TextSetCharactersMethod(String methodName) {
        super("Sets characters on a Text Render Binding at the specified coordinates.");
        this.methodName = methodName;

        addParameter("textRenderBinding", "TextRenderBinding", "Binding to render on.");
        addParameter("x", "Number", "X coordinate of the rendered text - number of characters from the left.");
        addParameter("y", "Number", "Y coordinate of the rendered text - line number.");
        addParameter("text", "String", "Text to display. Please note, that the sum of x and text length cannot exceed display width.");

        addExample("This example prints \"Hello World!\" in the first line of the display below the computer. " +
                        "Please make sure this computer has a module of Text Graphics Card type in any of its slots.",
                "var textMod = computer.bindModuleOfType(\"" + TextOnlyGraphicsCardModuleCommonSystem.TEXT_GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var renderBinding = textMod.getRenderBinding(\"down\");\n" +
                        "textMod.setCharacters(renderBinding, 0, 0, \"Hello World!\");"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", methodName);
        return renderCommandSink.isInstantRendering() ? 0 : 30;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        TextRenderCommandSink renderCommandSink = TextRenderBindingValidator.validateTextRenderBinding(line, computer, parameters, "renderBinding", methodName);

        int x = FunctionParamValidationUtil.validateIntParameter(line, parameters, "x", methodName);
        int y = FunctionParamValidationUtil.validateIntParameter(line, parameters, "y", methodName);

        String text = FunctionParamValidationUtil.validateStringParameter(line, parameters, "text", methodName);

        Vector2i maxCharacters = renderCommandSink.getMaxCharacters();

        if (x + text.length() >= maxCharacters.x)
            throw new ExecutionException(line, "Text will not fit in the display horizontally");

        int lineCount = maxCharacters.y;
        if (y >= lineCount)
            throw new ExecutionException(line, "Line index out of bounds " + y + ">=" + lineCount);

        List<String> oldLines = renderCommandSink.getExistingData(line);

        List<String> result = new ArrayList<>(lineCount);
        for (int i = 0; i < lineCount; i++) {
            String oldLine = getLine(oldLines, i);
            if (i == y) {
                String before = getBefore(oldLine, x);
                String after = getAfter(oldLine, x + text.length());
                result.add(before + text + after);
            } else {
                result.add(oldLine);
            }
        }

        renderCommandSink.setData(line, result);

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
        if (y < lines.size()) {
            String line = lines.get(y);
            if (line == null)
                return "";
            return line;
        }
        return "";
    }
}
