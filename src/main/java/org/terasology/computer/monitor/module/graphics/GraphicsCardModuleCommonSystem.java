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

import org.terasology.browser.data.ParagraphData;
import org.terasology.computer.system.common.ComputerModuleRegistry;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.multiBlock2.MultiBlockRegistry;
import org.terasology.registry.In;
import org.terasology.world.BlockEntityRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RegisterSystem(RegisterMode.ALWAYS)
public class GraphicsCardModuleCommonSystem extends BaseComponentSystem {
    public static final String GRAPHICS_CARD_MODULE_TYPE = "Graphics";
    public static final int MAXIMUM_WIDTH_PIXEL_DENSITY_PER_BLOCK = 128;
    public static final int MAXIMUM_HEIGHT_PIXEL_DENSITY_PER_BLOCK = 128;

    @In
    private ComputerModuleRegistry computerModuleRegistry;
    @In
    private BlockEntityRegistry blockEntityRegistry;
    @In
    private MultiBlockRegistry multiBlockRegistry;

    @Override
    public void preBegin() {
        computerModuleRegistry.registerComputerModule(
                GRAPHICS_CARD_MODULE_TYPE,
                new GraphicsCardComputerModule(multiBlockRegistry, GRAPHICS_CARD_MODULE_TYPE, "Graphics card"),
                "This module allows computer to render graphics on displays.",
                null,
                new TreeMap<String, String>() {{
                }},
                new HashMap<String, Map<String, String>>() {{
                }},
                new HashMap<String, String>() {{
                }},
                new HashMap<String, Collection<ParagraphData>>() {{
                }});
    }

}
