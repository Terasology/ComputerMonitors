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
import org.terasology.computer.monitor.module.graphics.GraphicsRenderBindingValidator;
import org.terasology.computer.monitor.module.graphics.GraphicsRenderCommandSink;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.math.geom.Vector2i;

import java.util.Map;

public class TextRenderBufferMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public TextRenderBufferMethod(String methodName) {
        super("Renders a Text Off Screen Buffer to the display.");
        this.methodName = methodName;

        addParameter("textRenderBinding", "TextRenderBinding", "Binding to render on.");
        addParameter("offScreenBuffer", "TextOffScreenBuffer", "Off screen buffer to render.");

        addExample("This example creates an off screen buffer with the size that the display below accepts, " +
                        "prints out \"Hello World!\" in each line of the buffer, then renders the buffer to " +
                        "the display. Please note, that all the text will be displayed at the same time " +
                        "on the display (buffering), and also that this process is much faster than displaying " +
                        "one line at the time on the display without the buffer. " +
                        "Please make sure this computer has a module of Text Graphics Card type in any of its slots.",
                "var textMod = computer.bindModuleOfType(\"" + TextOnlyGraphicsCardModuleCommonSystem.TEXT_GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var renderBinding = textMod.getRenderBinding(\"down\");\n" +
                        "var renderSize = textMod.getRenderSize(renderBinding);\n" +
                        "var width = renderSize[\"width\"];\n" +
                        "var height = renderSize[\"height\"];\n" +
                        "var buffer = textMod.createOffScreenBuffer(width, height);\n" +
                        "for (var y=0; y<height; y++) {\n" +
                        "  textMod.setCharacters(buffer, 0, y, \"Hello World!\");\n" +
                        "}\n" +
                        "textMod.renderBuffer(renderBinding, buffer);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", methodName);
        return renderCommandSink.isInstantRendering() ? 0 : 100;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        TextRenderCommandSink renderCommandSink = TextRenderBindingValidator.validateTextRenderBinding(line, computer, parameters, "renderBinding", methodName);
        TextBuffer textBuffer = TextRenderBindingValidator.validateTextBuffer(line, parameters, "offScreenBuffer", methodName);

        Vector2i size = textBuffer.getSize();
        Vector2i maxCharacters = renderCommandSink.getMaxCharacters();

        if (size.x > maxCharacters.x || size.y > maxCharacters.y)
            throw new ExecutionException(line, "OffScreenBuffer does not fit on the screen");

        renderCommandSink.setData(line, textBuffer.getData());

        return null;
    }
}
