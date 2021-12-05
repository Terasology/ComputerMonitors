// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.joml.Vector2i;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.monitor.module.graphics.GraphicsRenderBindingValidator;
import org.terasology.computer.monitor.module.graphics.GraphicsRenderCommandSink;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextClearMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public TextClearMethod(String methodName) {
        super("Clear the display of Text Render Binding.");
        this.methodName = methodName;

        addParameter("renderBinding", "TextRenderBinding", "Binding to clear.");

        addExample("This example prints \"Hello World!\" then immediately clears the display below the computer. " +
                        "Please make sure this computer has a module of Text Graphics Card type in any of its slots.",
                "var textMod = computer.bindModuleOfType(\"" + TextOnlyGraphicsCardModuleCommonSystem.TEXT_GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var renderBinding = textMod.getRenderBinding(\"down\");\n" +
                        "textMod.setCharacters(renderBinding, 0, 0, \"Hello World!\");\n" +
                        "textMod.clear(renderBinding);"
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

        Vector2i maxCharacters = new Vector2i(renderCommandSink.getMaxCharacters());

        int lineCount = maxCharacters.y;

        List<String> result = new ArrayList<>(lineCount);
        for (int i = 0; i < lineCount; i++) {
            result.add("");
        }

        renderCommandSink.setData(line, result);

        return null;
    }
}
