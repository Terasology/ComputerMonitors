// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import org.terasology.computer.module.DefaultComputerModule;
import org.terasology.multiBlock2.MultiBlockRegistry;

public class GraphicsCardComputerModule extends DefaultComputerModule {

    public GraphicsCardComputerModule(MultiBlockRegistry multiBlockRegistry, String moduleType, String moduleName) {
        super(moduleType, moduleName);

        addMethod("getRenderBinding", new GraphicsRenderBindingMethod("getRenderBinding", multiBlockRegistry));
        addMethod("getMaxRenderBinding", new GraphicsMaxRenderBindingMethod("getMaxRenderBinding", multiBlockRegistry));
        addMethod("clear", new GraphicsClearMethod("clear"));
        addMethod("createOffScreenBuffer", new GraphicsCreateOffScreenBufferMethod("createOffScreenBuffer"));
        addMethod("renderBuffer", new GraphicsRenderBufferMethod("renderBuffer"));
        addMethod("createColor", new CreateColorMethod("createColor"));
        addMethod("createGradient", new CreateGradientMethod("createGradient"));
        addMethod("drawText", new DrawTextMethod("drawText"));
        addMethod("drawRectangle", new DrawRectangleMethod("drawRectangle"));
        addMethod("drawRoundedRectangle", new DrawRoundedRectangleMethod("drawRoundedRectangle"));
        addMethod("getRenderSize", new GetGraphicsRenderSizeMethod("getRenderSize"));
        addMethod("getMaximumResolution", new GetGraphicsMaximumResolutionMethod("getMaximumResolution", multiBlockRegistry));
        addMethod("drawOval", new DrawOvalMethod("drawOval"));
        addMethod("drawLine", new DrawLineMethod("drawLine"));
    }
}
