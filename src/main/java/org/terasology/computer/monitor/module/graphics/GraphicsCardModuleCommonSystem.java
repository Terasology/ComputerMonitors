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
import java.util.LinkedHashMap;
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
                    put("getRenderBinding", "Returns Graphics Render Binding that allows to render graphics on a " +
                            "connected display. Specified resolution will be set on the device when binding is used.");
                    put("clear", "Clears the Graphics Render Binding of any data.");
                    put("createOffScreenBuffer", "Creates Graphics Off Screen Buffer that can be used for fast rendering " +
                            "and buffering and later rendered on a display. Please note that this off screen buffer also " +
                            "acts as a Graphics Render Binding.");
                    put("renderBuffer", "Renders a Graphics Off Screen Buffer to a Graphics Render Binding.");
                    put("createColor", "Creates a Paint object with a specified color.");
                    put("createGradient", "Creates a Paint object with a gradient defined.");
                    put("drawText", "Draws text on the Graphics Render Binding.");
                    put("drawRectangle", "Draws or fills specified rectangle with specified Paint.");
                    put("drawRoundedRectangle", "Draws or fills specified rounded rectangle with specified Paint.");
                    put("getRenderSize", "Returns the size of the Graphics Render Binding.");
                    put("getMaximumResolution", "Returns the maximum resolution possible for the display.");
                }},
                new HashMap<String, Map<String, String>>() {{
                    put("getRenderBinding",
                            new LinkedHashMap<String, String>() {{
                                put("direction", "[String] Direction of the binding in reference to computer.");
                                put("width", "[Number] Width of the graphics binding.");
                                put("height", "[Number] Height of the graphics binding.");
                            }});
                    put("clear",
                            new LinkedHashMap<String, String>() {{
                                put("graphicsRenderBinding", "[Graphics Render Binding] Binding to clear of any data.");
                            }});
                    put("createOffScreenBuffer",
                            new LinkedHashMap<String, String>() {{
                                put("width", "[Number] Width of the Graphics Off Screen Buffer.");
                                put("height", "[Number] Height of the Graphics Off Screen Buffer.");
                            }});
                    put("renderBuffer",
                            new LinkedHashMap<String, String>() {{
                                put("graphicsRenderBinding", "[Graphics Render Binding] Binding to render the buffer on.");
                                put("graphicsOffScreenBuffer", "[Graphics Off Screen Buffer] Buffer to render. Please note, that the resolution " +
                                        "of the buffer cannot exceed resolution of the binding.");
                            }});
                    put("createColor",
                            new LinkedHashMap<String, String>() {{
                                put("hex", "[String] Hexadecimal value of the color in the RRGGBBAA order.");
                            }});
                    put("createGradient",
                            new LinkedHashMap<String, String>() {{
                                put("hex1", "[String] Hexadecimal value of the first color in the RRGGBBAA order.");
                                put("x1", "[Number] X coordinate of the first color.");
                                put("y1", "[Number] Y coordinate of the first color.");
                                put("hex2", "[String] Hexadecimal value of the second color in the RRGGBBAA order.");
                                put("x2", "[Number] X coordinate of the second color.");
                                put("y2", "[Number] Y coordinate of the second color.");
                                put("cyclic", "[Boolean] If the gradient should be cyclic or not.");
                            }});
                    put("drawText",
                            new LinkedHashMap<String, String>() {{
                                put("graphicsRenderBinding", "[Graphics Render Binding] Binding to draw text on.");
                                put("text", "[String] Text to draw");
                                put("x", "[Number] X coordinate of the baseline for the font.");
                                put("y", "[Number] Y coordinate of the baseline for the font.");
                                put("font", "[String] Font to use (please note that if a player does not have the font it will be drawn with the default one.");
                                put("fontSize", "[Number] Size of the font.");
                                put("paint", "[Paint] Paint to use to draw the text.");
                            }});
                    put("drawRectangle",
                            new LinkedHashMap<String, String>() {{
                                put("graphicsRenderBinding", "[Graphics Render Binding] Binding to draw rectangle on.");
                                put("x", "[Number] X coordinate of the top-left corner of the rectangle.");
                                put("y", "[Number] Y coordinate of the top-left corner of the rectangle.");
                                put("width", "[Number] Width of the rectangle.");
                                put("height", "[Number] Height of the rectangle.");
                                put("paint", "[Paint] Paint to use to draw the rectangle.");
                                put("fill", "[Boolean] If the rectangle should also be filled with the paint.");
                            }});
                    put("drawRoundedRectangle",
                            new LinkedHashMap<String, String>() {{
                                put("graphicsRenderBinding", "[Graphics Render Binding] Binding to draw rounded rectangle on.");
                                put("x", "[Number] X coordinate of the top-left corner of the rounded rectangle.");
                                put("y", "[Number] Y coordinate of the top-left corner of the rounded rectangle.");
                                put("width", "[Number] Width of the rounded rectangle.");
                                put("height", "[Number] Height of the rounded rectangle.");
                                put("arcWidth", "[Number] Width of the arc used to round the rectangle.");
                                put("arcHeight", "[Number] Height of the arc used to round the rectangle.");
                                put("paint", "[Paint] Paint to use to draw the rounded rectangle.");
                                put("fill", "[Boolean] If the rounded rectangle should also be filled with the paint.");
                            }});
                    put("getRenderSize",
                            new LinkedHashMap<String, String>() {{
                                put("graphicsRenderBinding", "[Graphics Render Binding] Binding to get a size of.");
                            }});
                    put("getMaximumResolution",
                            new LinkedHashMap<String, String>() {{
                                put("direction", "[String] Direction of the display to query for maximum resolution.");
                            }});
                }},
                new HashMap<String, String>() {{
                    put("getRenderBinding", "[Graphics Render Binding] Binding for the specified direction and resolution.");
                    put("createOffScreenBuffer", "[Graphics Off Screen Buffer] Off screen buffer with the specified resolution.");
                    put("createColor", "[Paint] Requested color paint.");
                    put("createGradient", "[Paint] Requested gradient paint.");
                    put("getRenderSize", "[Map] Returns a map with two keys - " +
                            "\"width\" that has a value type of Number and contains width of the binding and " +
                            "\"height\" that has a value type of Number and contains height of the binding.");
                    put("getMaximumResolution", "[Map] Returns a map with two keys - " +
                            "\"width\" that has a value type of Number and contains maximum width of the display and " +
                            "\"height\" that has a value type of Number and contains maximum height of the display.");
                }},
                new HashMap<String, Collection<ParagraphData>>() {{
                }});
    }

}
