// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import org.terasology.computer.display.system.client.DisplayRenderModeRegistry;
import org.terasology.computer.system.common.ComputerLanguageRegistry;
import org.terasology.computer.system.common.ComputerModuleRegistry;
import org.terasology.computer.ui.documentation.DocumentationBuilder;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.config.ModuleConfigManager;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Collections;

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
    @In
    private ComputerLanguageRegistry computerLanguageRegistry;
    @In
    private ModuleConfigManager moduleConfigManager;

    @Override
    public void preBegin() {
        if (displayRenderModeRegistry != null
                && moduleConfigManager.getBooleanVariable("ComputerMonitors", "registerModule.graphics", true)) {
            displayRenderModeRegistry.registerComputerMonitorRenderer("Graphics:", new GraphicsDisplayRenderer());

            computerLanguageRegistry.registerObjectType(
                    "GraphicsRenderBinding",
                    Collections.singleton(HTMLLikeParser.parseHTMLLikeParagraph(null, "Binding that tells method " +
                            "where to render graphics to. Usually passed as " +
                            "a parameter to methods of Graphics Card computer module.")));

            computerLanguageRegistry.registerObjectType(
                    "Paint",
                    Collections.singleton(HTMLLikeParser.parseHTMLLikeParagraph(null, "Object that tells the " +
                            "rendering engine how to paint the specified elements (color, gradient, etc).")));

            computerLanguageRegistry.registerObjectType(
                    "GraphicsOffScreenBuffer",
                    Collections.singleton(HTMLLikeParser.parseHTMLLikeParagraph(null, "In memory buffer for graphics," +
                            " please note this object takes considerable amount " +
                            "of computer memory so should be used wisely. This object can also be passed wherever " +
                            "<h navigate:" + DocumentationBuilder.getObjectTypePageId("GraphicsRenderBinding") + 
                            ">GraphicsRenderBinding</h> " +
                            "is expected, as it can also have graphics drawn on.")));

            computerModuleRegistry.registerComputerModule(
                    GRAPHICS_CARD_MODULE_TYPE,
                    new GraphicsCardComputerModule(multiBlockRegistry, GRAPHICS_CARD_MODULE_TYPE, "Graphics Card"),
                    "This module allows computer to render graphics on displays.",
                    null);
        }
    }

}
