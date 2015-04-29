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

import org.terasology.browser.data.ParagraphData;
import org.terasology.computer.display.system.client.DisplayRenderModeRegistry;
import org.terasology.computer.system.common.ComputerModuleRegistry;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.multiBlock2.MultiBlockRegistry;
import org.terasology.registry.In;
import org.terasology.world.BlockEntityRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@RegisterSystem(RegisterMode.ALWAYS)
public class TextOnlyGraphicsCardModuleCommonSystem extends BaseComponentSystem {
    public static final String TEXT_GRAPHICS_CARD_MODULE_TYPE = "TextGraphics";

    @In
    private ComputerModuleRegistry computerModuleRegistry;
    @In
    private BlockEntityRegistry blockEntityRegistry;
    @In
    private MultiBlockRegistry multiBlockRegistry;
    @In
    private DisplayRenderModeRegistry displayRenderModeRegistry;

    @Override
    public void preBegin() {
        if (displayRenderModeRegistry != null) {
            displayRenderModeRegistry.registerComputerMonitorRenderer("Text:", new TextDisplayRenderer());
        }

        computerModuleRegistry.registerComputerModule(
                TEXT_GRAPHICS_CARD_MODULE_TYPE,
                new TextOnlyGraphicsCardComputerModule(multiBlockRegistry, TEXT_GRAPHICS_CARD_MODULE_TYPE, "Text Graphics card"),
                "This module allows computer to render text on displays.",
                null,
                new TreeMap<String, String>() {{
                    put("getRenderBinding", "Creates a Text Render Binding that allows to render text on a display.");
                    put("setCharacters", "Sets characters on a Text Render Binding at the specified coordinates.");
                    put("clear", "Clear the display of Text Render Binding.");
                    put("createOffScreenBuffer", "Creates a Text Off Screen Buffer that allows faster rendering and buffering. " +
                            "Please note that this off screen buffer also acts as a Text Render Binding.");
                    put("renderBuffer", "Renders a Text Off Screen Buffer to the display.");
                    put("getRenderSize", "Returns the size of the bound display in characters.");
                }},
                new HashMap<String, Map<String, String>>() {{
                    put("getRenderBinding",
                            new LinkedHashMap<String, String>() {{
                                put("direction", "[String] Specifies direction of the display relative to computer.");
                            }});
                    put("setCharacters",
                            new LinkedHashMap<String, String>() {{
                                put("textRenderBinding", "[Text Render Binding] Binding to render on.");
                                put("x", "[Number] X coordinate of the rendered text - number of characters from the left.");
                                put("y", "[Number] Y coordinate of the rendered text - line number.");
                                put("text", "[String] Text to display. Please note, that the sum of x and text length cannot exceed display width.");
                            }});
                    put("clear",
                            new LinkedHashMap<String, String>() {{
                                put("textRenderBinding", "[Text Render Binding] Binding to clear.");
                            }});
                    put("createOffScreenBuffer",
                            new LinkedHashMap<String, String>() {{
                                put("width", "[Number] Width of the off screen buffer in characters.");
                                put("height", "[Number] Height of the off screen buffer in characters.");
                            }});
                    put("renderBuffer",
                            new LinkedHashMap<String, String>() {{
                                put("textRenderBinding", "[Text Render Binding] Binding to render on.");
                                put("offScreenBuffer", "[Text Off Screen Buffer] Off screen buffer to render.");
                            }});
                    put("getRenderSize",
                            new LinkedHashMap<String, String>() {{
                                put("textRenderBinding", "[Text Render Binding] Binding to get size of.");
                            }});
                }},
                new HashMap<String, String>() {{
                    put("getRenderBinding", "[Text Render Binding] Binding that can be used to render text on.");
                    put("creteOffScreenBuffer", "[Text Off Screen Buffer] Buffer that can be used to render text on and " +
                            "later displayed.");
                    put("getRenderSize", "[Map] Returns a map with two keys - " +
                            "\"width\" that has a value type of Number and contains width in characters and " +
                            "\"height\" that has a value type of Number and contains height in characters.");
                }},
                new HashMap<String, Collection<ParagraphData>>() {{
                }});
    }

}
