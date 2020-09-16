// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import org.terasology.math.geom.Vector2i;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;

import java.util.HashMap;
import java.util.Map;

public class GetGraphicsRenderSizeMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public GetGraphicsRenderSizeMethod(String methodName) {
        super("Returns the size of the Graphics Render Binding.", "Map", "Returns a map with two keys - " +
                "\"width\" that has a value type of Number and contains width of the binding and " +
                "\"height\" that has a value type of Number and contains height of the binding.");
        this.methodName = methodName;

        addParameter("graphicsRenderBinding", "GraphicsRenderBinding", "Binding to get a size of.");

        addExample("This example queries the size of the Graphics Render Binding created from a display with its " +
                        "maximum resolution then displays it. " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var width = maxRes[\"width\"];\n" +
                        "var height = maxRes[\"height\"];\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\", width, height);" +
                        "var renderSize = graphicsMod.getRenderSize(display);\n" +
                        "console.append(\"Width: \"+renderSize[\"width\"]+\", height: \"+renderSize[\"height\"]);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink =
                GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, 
                        "renderBinding", methodName);

        Vector2i resolution = renderCommandSink.getResolution();

        Map<String, Variable> result = new HashMap<>();
        result.put("width", new Variable(resolution.x));
        result.put("height", new Variable(resolution.y));

        return result;
    }
}
