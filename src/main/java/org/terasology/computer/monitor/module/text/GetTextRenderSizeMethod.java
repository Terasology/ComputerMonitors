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
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.math.Vector2i;

import java.util.HashMap;
import java.util.Map;

public class GetTextRenderSizeMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public GetTextRenderSizeMethod(String methodName) {
        super("Returns the size of the bound display in characters.", "Map", "Returns a map with two keys - " +
                "\"width\" that has a value type of Number and contains width in characters and " +
                "\"height\" that has a value type of Number and contains height in characters.");
        this.methodName = methodName;

        addParameter("renderBinding", "Text Render Binding", "Binding to get size of.");

        addExample("This example gets render binding for the display below the computer and prints the display's " +
                        "width and height available in characters. Please make sure " +
                        "this computer has a module of Text Graphics Card type in any of its slots.",
                "var textMod = computer.bindModuleOfType(\"" + TextOnlyGraphicsCardModuleCommonSystem.TEXT_GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var renderBinding = textMod.getRenderBinding(\"down\");\n" +
                        "var renderSize = textMod.getRenderSize(renderBinding);\n" +
                        "console.append(\"The render size is \"+renderSize[\"width\"]+\n" +
                        "  \" by \"+renderSize[\"height\"]+\" characters.\");"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        TextRenderCommandSink renderCommandSink = TextRenderBindingValidator.validateTextRenderBinding(line, computer, parameters, "renderBinding", methodName);

        Vector2i maxCharacters = renderCommandSink.getMaxCharacters();

        Map<String, Variable> result = new HashMap<>();
        result.put("width", new Variable(maxCharacters.x));
        result.put("height", new Variable(maxCharacters.y));

        return result;
    }
}
