// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import org.terasology.computer.display.component.DisplayComponent;
import org.terasology.computer.display.system.server.DisplayServerSystem;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.math.Direction;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.modularcomputers.FunctionParamValidationUtil;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Map;

public class GraphicsMaxRenderBindingMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;
    private final MultiBlockRegistry multiBlockRegistry;

    public GraphicsMaxRenderBindingMethod(String methodName, MultiBlockRegistry multiBlockRegistry) {
        super("Returns Graphics Render Binding that allows to render graphics on a " +
                        "connected display. Maximum resolution will be set on the device when binding is used.",
                "GraphicsRenderBinding", "Binding for the specified direction and maximum available resolution.");
        this.multiBlockRegistry = multiBlockRegistry;
        this.methodName = methodName;

        addParameter("direction", "Direction", "Direction of the binding in reference to computer.");

        addExample("This example gets render binding of the maximum size for the display below and clears the screen." +
                        " " +
                        "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                "var graphicsMod = computer.bindModuleOfType(\"" + GraphicsCardModuleCommonSystem.GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                        "var display = graphicsMod.getMaxRenderBinding(\"down\");\n" +
                        "graphicsMod.clear(display);"
        );

    }

    @Override
    public int getCpuCycleDuration() {
        return 10;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        Direction direction = FunctionParamValidationUtil.validateDirectionParameter(line, parameters,
                "direction", methodName);

        Vector3f computerLocation = computer.getComputerLocation();
        Vector3i directionVector = direction.getVector3i();
        Vector3i monitorLocation = new Vector3i(
                computerLocation.x + directionVector.x,
                computerLocation.y + directionVector.y,
                computerLocation.z + directionVector.z);
        EntityRef monitorEntity = multiBlockRegistry.getMultiBlockAtLocation(monitorLocation,
                DisplayServerSystem.MONITOR_MULTI_BLOCK_TYPE);

        if (monitorEntity == null) {
            throw new ExecutionException(line, "Unable to locate device that could be rendered on");
        }

        DisplayComponent monitor = monitorEntity.getComponent(DisplayComponent.class);

        Vector3i monitorSize = monitor.getMonitorSize();

        int width = Math.max(monitorSize.x, monitorSize.z);
        int height = monitorSize.y;

        return new RelativeLiveGraphicsRenderBindingCustomObject(multiBlockRegistry, direction, "Graphics:", width,
                height);
    }
}
