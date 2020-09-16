// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;

import java.util.ArrayList;
import java.util.Map;

public class GraphicsClearMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public GraphicsClearMethod(String methodName) {
        super("Clears the Graphics Render Binding of any data.");
        this.methodName = methodName;

        addParameter("renderBinding", "GraphicsRenderBinding", "Binding to clear of any data.");

        addExample("This example gets render binding of the maximum size for the display below and clears the screen." +
                        " " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                        "var display = graphicsMod.getRenderBinding(\"down\",\n" +
                        "  maxRes[\"width\"], maxRes[\"height\"]);\n" +
                        "graphicsMod.clear(display);"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink =
                GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, 
                        "renderBinding", methodName);
        return renderCommandSink.isInstantRendering() ? 0 : 50;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink =
                GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, 
                        "renderBinding", methodName);

        renderCommandSink.setData(line, new ArrayList<>());

        return null;
    }
}
