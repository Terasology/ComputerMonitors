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
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.math.Direction;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Map;

public class TextRenderBindingMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;
    private MultiBlockRegistry multiBlockRegistry;

    public TextRenderBindingMethod(String methodName, MultiBlockRegistry multiBlockRegistry) {
        super("Creates a Text Render Binding that allows to render text on a display.",
                "TextRenderBinding", "Binding that can be used to render text on.");
        this.multiBlockRegistry = multiBlockRegistry;
        this.methodName = methodName;

        addParameter("direction", "Direction", "Specifies direction of the display relative to computer.");

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
        return 10;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        Direction direction = FunctionParamValidationUtil.validateDirectionParameter(line, parameters,
                "direction", methodName);

        return new RelativeLiveTextRenderBindingCustomObject(multiBlockRegistry, direction, "Text:");
    }
}
