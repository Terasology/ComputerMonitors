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

import org.terasology.computer.module.DefaultComputerModule;
import org.terasology.computer.system.server.lang.ComputerModule;
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Collection;

public class TextOnlyGraphicsCardComputerModule extends DefaultComputerModule {
    public TextOnlyGraphicsCardComputerModule(MultiBlockRegistry multiBlockRegistry, String moduleType, String moduleName) {
        super(moduleType, moduleName);

        addMethod("getRenderBinding", new TextRenderBindingMethod("getRenderBinding", multiBlockRegistry));
        addMethod("setCharacters", new TextSetCharactersMethod("setCharacters"));
        addMethod("clear", new TextClearMethod("clear"));
        addMethod("createOffScreenBuffer", new TextCreateOffScreenBufferMethod("createOffScreenBuffer"));
        addMethod("renderBuffer", new TextRenderBufferMethod("renderBuffer"));
        addMethod("getRenderSize", new GetTextRenderSizeMethod("getRenderSize"));
    }
}
