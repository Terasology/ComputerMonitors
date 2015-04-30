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
import org.terasology.computer.display.system.client.DisplayRenderModeRegistry;
import org.terasology.computer.system.common.ComputerModuleRegistry;
import org.terasology.computer.ui.documentation.DocumentationBuilder;
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
    @In
    private DisplayRenderModeRegistry displayRenderModeRegistry;

    @Override
    public void preBegin() {
        if (displayRenderModeRegistry != null) {
            displayRenderModeRegistry.registerComputerMonitorRenderer("Graphics:", new GraphicsDisplayRenderer());
        }

        computerModuleRegistry.registerComputerModule(
                GRAPHICS_CARD_MODULE_TYPE,
                new GraphicsCardComputerModule(multiBlockRegistry, GRAPHICS_CARD_MODULE_TYPE, "Graphics Card"),
                "This module allows computer to render graphics on displays.",
                null,
                new TreeMap<String, String>() {{
                    put("getRenderBinding", "Returns Graphics Render Binding that allows to render graphics on a " +
                            "connected display. Specified resolution will be set on the device when binding is used.");
                    put("getMaxRenderBinding", "Returns Graphics Render Binding that allows to render graphics on a " +
                            "connected display. Maximum resolution will be set on the device when binding is used.");
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
                    put("drawOval", "Draws or fills an oval with the specified bounds with specified Paint.");
                    put("drawLine", "Draws a line between two points with the specified Paint and width.");
                }},
                new HashMap<String, Map<String, String>>() {{
                    put("getRenderBinding",
                            new LinkedHashMap<String, String>() {{
                                put("direction", "[String] Direction of the binding in reference to computer.");
                                put("width", "[Number] Width of the graphics binding.");
                                put("height", "[Number] Height of the graphics binding.");
                            }});
                    put("getMaxRenderBinding",
                            new LinkedHashMap<String, String>() {{
                                put("direction", "[String] Direction of the binding in reference to computer.");
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
                    put("drawOval",
                            new LinkedHashMap<String, String>() {{
                                put("graphicsRenderBinding", "[Graphics Render Binding] Binding to draw oval on.");
                                put("x", "[Number] X coordinate of the top-left corner of the bounding rectangle.");
                                put("y", "[Number] Y coordinate of the top-left corner of the bounding rectangle.");
                                put("width", "[Number] Width of the bounding rectangle.");
                                put("height", "[Number] Height of the bounding rectangle.");
                                put("paint", "[Paint] Paint to use to draw the oval.");
                                put("fill", "[Boolean] If the oval should also be filled with the paint.");
                            }});
                    put("drawLine",
                            new LinkedHashMap<String, String>() {{
                                put("graphicsRenderBinding", "[Graphics Render Binding] Binding to draw line on.");
                                put("x1", "[Number] X coordinate of the first point.");
                                put("y1", "[Number] Y coordinate of the first point.");
                                put("x2", "[Number] X coordinate of the second point.");
                                put("y2", "[Number] Y coordinate of the second point.");
                                put("paint", "[Paint] Paint to use to draw the line.");
                                put("width", "[Number] Width of the line to draw.");
                            }});
                }},
                new HashMap<String, String>() {{
                    put("getRenderBinding", "[Graphics Render Binding] Binding for the specified direction and resolution.");
                    put("getMaxRenderBinding", "[Graphics Render Binding] Binding for the specified direction and maximum available resolution.");
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
                    put("getRenderBinding", DocumentationBuilder.createExampleParagraphs(
                            "This example gets render binding of the maximum size for the display below and clears the screen. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\",\n" +
                                    "  maxRes[\"width\"], maxRes[\"height\"]);\n" +
                                    "graphicsMod.clear(display);"
                    ));
                    put("getMaxRenderBinding", DocumentationBuilder.createExampleParagraphs(
                            "This example gets render binding of the maximum size for the display below and clears the screen. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var display = graphicsMod.getMaxRenderBinding(\"down\");\n" +
                                    "graphicsMod.clear(display);"
                    ));
                    put("clear", DocumentationBuilder.createExampleParagraphs(
                            "This example gets render binding of the maximum size for the display below and clears the screen. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\",\n" +
                                    "  maxRes[\"width\"], maxRes[\"height\"]);\n" +
                                    "graphicsMod.clear(display);"
                    ));
                    put("createOffScreenBuffer", DocumentationBuilder.createExampleParagraphs(
                            "This example creates an off screen buffer of maximum resolution accepted by the display below and " +
                                    "draws fiver rectangles on it in different colors, then renders the buffer to the display. " +
                                    "Please note that all the five rectangles appear on the display at the same time (buffering), " +
                                    "also rendering like this is much faster than rendering one rectangle on the screen at a time. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var buffer = graphicsMod.createOffScreenBuffer(width, height);\n" +
                                    "var red = graphicsMod.createColor(\"ff0000\");\n" +
                                    "for (var i=0; i<5; i++) {\n" +
                                    "  graphicsMod.drawRectangle(buffer, 0, i*10, width, 5, red, false);\n" +
                                    "}\n" +
                                    "graphicsMod.renderBuffer(display, buffer);"
                    ));
                    put("renderBuffer", DocumentationBuilder.createExampleParagraphs(
                            "This example creates an off screen buffer of maximum resolution accepted by the display below and " +
                                    "draws fiver rectangles on it in different colors, then renders the buffer to the display. " +
                                    "Please note that all the five rectangles appear on the display at the same time (buffering), " +
                                    "also rendering like this is much faster than rendering one rectangle on the screen at a time. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var buffer = graphicsMod.createOffScreenBuffer(width, height);\n" +
                                    "var red = graphicsMod.createColor(\"ff0000\");\n" +
                                    "for (var i=0; i<5; i++) {\n" +
                                    "  graphicsMod.drawRectangle(buffer, 0, i*10, width, 5, red, false);\n" +
                                    "}\n" +
                                    "graphicsMod.renderBuffer(display, buffer);"
                    ));
                    put("createColor", DocumentationBuilder.createExampleParagraphs(
                            "This example draws a red rectangle in the middle of the display with half of its width and height. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var red = graphicsMod.createColor(\"ff0000\");\n" +
                                    "graphicsMod.drawRectangle(display, width/4, height/4, width/2, height/2, red, false);"
                    ));
                    put("createGradient", DocumentationBuilder.createExampleParagraphs(
                            "This example draws a filled rectangle in the middle of the display with half of its width and height. " +
                                    "The fill of the rectangle is an acyclic gradient spanning between top-left and bottom-right corners of the " +
                                    "display, going from red to green color. Please note, that neither red, nor green colors will be " +
                                    "displayed, because the bounds of the gradient are outside of the drawn shape. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var gradient = graphicsMod.createGradient(\"ff0000\", 0, 0, \"00ff00\", width, height, false);\n" +
                                    "graphicsMod.drawRectangle(display, width/4, height/4, width/2, height/2, gradient, true);"
                    ));
                    put("drawText", DocumentationBuilder.createExampleParagraphs(
                            "This example draws the \"Hello World!\" text on the display. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var white = graphicsMod.createColor(\"ffffff\");\n" +
                                    "graphicsMod.drawText(display, \"Hello World!\", 0, 15, \"Arial\", 12, white);"
                    ));
                    put("drawRectangle", DocumentationBuilder.createExampleParagraphs(
                            "This example draws a red rectangle in the middle of the display with half of its width and height. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var red = graphicsMod.createColor(\"ff0000\");\n" +
                                    "graphicsMod.drawRectangle(display, width/4, height/4, width/2, height/2, red, false);"
                    ));
                    put("drawRoundedRectangle", DocumentationBuilder.createExampleParagraphs(
                            "This example draws a red rounded rectangle in the middle of the display with half of its width and height. The " +
                                    "arc of the rounding is 1/8 of the width and height respectively. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var red = graphicsMod.createColor(\"ff0000\");\n" +
                                    "graphicsMod.drawRoundedRectangle(display, width/4, height/4, width/2, height/2, width/8, height/8, red, false);"
                    ));
                    put("getRenderSize", DocumentationBuilder.createExampleParagraphs(
                            "This example queries the size of the Graphics Render Binding created from a display with its " +
                                    "maximum resolution then displays it. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);" +
                                    "var renderSize = graphicsMod.getRenderSize(display);\n" +
                                    "console.append(\"Width: \"+renderSize[\"width\"]+\", height: \"+renderSize[\"height\"]);"
                    ));
                    put("getMaximumResolution", DocumentationBuilder.createExampleParagraphs(
                            "This example queries the display below for its maximum resolution supported and prints it out " +
                                    "to console. "+
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "console.append(\"Maximum resolution is \" + width + \"x\" + height);"
                    ));
                    put("drawOval", DocumentationBuilder.createExampleParagraphs(
                            "This example draws a red oval with bounds the size of the whole display. " +
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var red = graphicsMod.createColor(\"ff0000\");\n" +
                                    "graphicsMod.drawOval(display, 0, 0, width, height, red, false);"
                    ));
                    put("drawLine", DocumentationBuilder.createExampleParagraphs(
                            "This example draws a blue line from top-left corner to bottom-right corner of the display with a width of " +
                                    "two pixels. "+
                                    "Please make sure this computer has a module of Graphics Card type in any of its slots.",
                            "var graphicsMod = computer.bindModuleOfType(\"" + GRAPHICS_CARD_MODULE_TYPE + "\");\n" +
                                    "var maxRes = graphicsMod.getMaximumResolution(\"down\");\n" +
                                    "var width = maxRes[\"width\"];\n" +
                                    "var height = maxRes[\"height\"];\n" +
                                    "var display = graphicsMod.getRenderBinding(\"down\", width, height);\n" +
                                    "var blue = graphicsMod.createColor(\"0000ff\");\n" +
                                    "graphicsMod.drawLine(display, 0, 0, width, height, blue, 2);"
                    ));
                }});
    }

}
