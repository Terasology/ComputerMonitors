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
    @In
    private ComputerLanguageRegistry computerLanguageRegistry;
    @In
    private ModuleConfigManager moduleConfigManager;

    @Override
    public void preBegin() {
        if (displayRenderModeRegistry != null
                && moduleConfigManager.getBooleanVariable("ComputerMonitors", "registerModule.text", true)) {
            displayRenderModeRegistry.registerComputerMonitorRenderer("Text:", new TextDisplayRenderer());

            computerLanguageRegistry.registerObjectType(
                    "TextRenderBinding",
                    Collections.singleton(HTMLLikeParser.parseHTMLLikeParagraph(null, "Binding that tells method where to render text to. Usually passed as " +
                            "a parameter to methods of Text Graphics Card computer module.")));

            computerLanguageRegistry.registerObjectType(
                    "TextOffScreenBuffer",
                    Collections.singleton(HTMLLikeParser.parseHTMLLikeParagraph(null, "In memory buffer for text, please note this object takes considerable amount " +
                            "of computer memory so should be used wisely. This object can also be passed wherever " +
                            "<h navigate:" + DocumentationBuilder.getObjectTypePageId("TextRenderBinding") + ">TextRenderBinding</h> " +
                            "is expected, as it can also be drawn text on.")));

            computerModuleRegistry.registerComputerModule(
                    TEXT_GRAPHICS_CARD_MODULE_TYPE,
                    new TextOnlyGraphicsCardComputerModule(multiBlockRegistry, TEXT_GRAPHICS_CARD_MODULE_TYPE, "Text Graphics Card"),
                    "This module allows computer to render text on displays.",
                    null);
        }
    }

}
