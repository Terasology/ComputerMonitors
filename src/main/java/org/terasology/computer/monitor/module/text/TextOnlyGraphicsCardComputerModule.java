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

import org.terasology.computer.system.server.lang.ComputerModule;
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Collection;

public class TextOnlyGraphicsCardComputerModule implements ComputerModule {
    private MultiBlockRegistry multiBlockRegistry;
    private String moduleType;
    private String moduleName;

    public TextOnlyGraphicsCardComputerModule(MultiBlockRegistry multiBlockRegistry, String moduleType, String moduleName) {
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
            return new TextRenderBindingMethod("getRenderBinding", multiBlockRegistry);
        if (name.equals("setCharacters"))
            return new TextSetCharactersMethod("setCharacters");
        if (name.equals("clear"))
            return new TextClearMethod("clear");
        if (name.equals("createOffScreenBuffer"))
            return new TextCreateOffScreenBufferMethod("createOffScreenBuffer");
        if (name.equals("renderBuffer"))
            return new TextRenderBufferMethod("renderBuffer");
        if (name.equals("getRenderSize"))
            return new GetTextRenderSizeMethod("getRenderSize");
        return null;
    }
}
