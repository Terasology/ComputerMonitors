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
