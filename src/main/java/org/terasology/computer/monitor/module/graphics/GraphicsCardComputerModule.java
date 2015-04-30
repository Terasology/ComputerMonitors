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

import org.terasology.computer.system.server.lang.ComputerModule;
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Collection;

public class GraphicsCardComputerModule implements ComputerModule {
    private MultiBlockRegistry multiBlockRegistry;
    private String moduleType;
    private String moduleName;

    public GraphicsCardComputerModule(MultiBlockRegistry multiBlockRegistry, String moduleType, String moduleName) {
        this.multiBlockRegistry = multiBlockRegistry;
        this.moduleType = moduleType;
        this.moduleName = moduleName;
    }

    @Override
    public String getModuleType() {
        return moduleType;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public boolean canBePlacedInComputer(Collection<ComputerModule> computerModulesInstalled) {
        return true;
    }

    @Override
    public boolean acceptsNewModule(ComputerModule computerModule) {
        return true;
    }

    @Override
    public ModuleMethodExecutable getFunctionByName(String name) {
        if (name.equals("getRenderBinding"))
            return new GraphicsRenderBindingMethod("getRenderBinding", multiBlockRegistry);
        if (name.equals("getMaxRenderBinding"))
            return new GraphicsMaxRenderBindingMethod("getMaxRenderBinding", multiBlockRegistry);
        if (name.equals("clear"))
            return new GraphicsClearMethod("clear");
        if (name.equals("createOffScreenBuffer"))
            return new GraphicsCreateOffScreenBufferMethod("createOffScreenBuffer");
        if (name.equals("renderBuffer"))
            return new GraphicsRenderBufferMethod("renderBuffer");
        if (name.equals("createColor"))
            return new CreateColorMethod("createColor");
        if (name.equals("createGradient"))
            return new CreateGradientMethod("createGradient");
        if (name.equals("drawText"))
            return new DrawTextMethod("drawText");
        if (name.equals("drawRectangle"))
            return new DrawRectangleMethod("drawRectangle");
        if (name.equals("drawRoundedRectangle"))
            return new DrawRoundedRectangleMethod("drawRoundedRectangle");
        if (name.equals("getRenderSize"))
            return new GetGraphicsRenderSizeMethod("getRenderSize");
        if (name.equals("getMaximumResolution"))
            return new GetGraphicsMaximumResolutionMethod("getMaximumResolution", multiBlockRegistry);
        if (name.equals("drawOval"))
            return new DrawOvalMethod("drawOval");
        if (name.equals("drawLine"))
            return new DrawLineMethod("drawLine");
        return null;
    }
}
