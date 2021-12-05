// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.joml.RoundingMode;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.display.component.DisplayComponent;
import org.terasology.computer.display.system.server.DisplayServerSystem;
import org.terasology.computer.monitor.module.EntityRenderCommandSink;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.math.Direction;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Collection;
import java.util.Collections;

public class RelativeLiveTextRenderBindingCustomObject implements CustomObject, TextRenderBinding {
    private MultiBlockRegistry multiBlockRegistry;
    private Direction direction;
    private String mode;

    public RelativeLiveTextRenderBindingCustomObject(MultiBlockRegistry multiBlockRegistry, Direction direction, String mode) {
        this.multiBlockRegistry = multiBlockRegistry;
        this.direction = direction;
        this.mode = mode;
    }

    @Override
    public Collection<String> getType() {
        return Collections.singleton("TEXT_RENDER_BINDING");
    }

    @Override
    public int sizeOf() {
        return 4;
    }

    @Override
    public TextRenderCommandSink getTextRenderCommandSink(int line, ComputerCallback computerCallback) throws ExecutionException {
        Vector3f computerLocation = computerCallback.getComputerLocation();
        Vector3ic directionVector = direction.asVector3i();
        Vector3i monitorLocation = new Vector3i(new Vector3f(
                computerLocation.x + directionVector.x(),
                computerLocation.y + directionVector.y(),
                computerLocation.z + directionVector.z()), RoundingMode.FLOOR);
        EntityRef monitorEntity = multiBlockRegistry.getMultiBlockAtLocation(monitorLocation, DisplayServerSystem.MONITOR_MULTI_BLOCK_TYPE);

        if (monitorEntity == null) {
            throw new ExecutionException(line, "Unable to locate device that could be rendered on");
        }

        return new EntityTextRenderCommandSink(monitorEntity, mode);
    }

    private static class EntityTextRenderCommandSink extends EntityRenderCommandSink implements TextRenderCommandSink {
        private EntityRef entityRef;
        private String modePrefix;

        private EntityTextRenderCommandSink(EntityRef entityRef, String modePrefix) {
            super(entityRef);
            this.entityRef = entityRef;
            this.modePrefix = modePrefix;
        }

        @Override
        public boolean isInstantRendering() {
            return false;
        }

        @Override
        protected String getRequiredMode(int line) throws ExecutionException {
            Vector2ic maxCharacters = getMaxCharacters();
            return modePrefix + maxCharacters.x() + "," + maxCharacters.y();
        }

        @Override
        public Vector2ic getMaxCharacters() {
            DisplayComponent monitor = entityRef.getComponent(DisplayComponent.class);

            Vector3i monitorSize = monitor.getMonitorSize();

            int height = monitorSize.y;
            int width = Math.max(monitorSize.x, monitorSize.z);

            int lineCount = height * 5;
            int charsInLine = width * 10;

            return new Vector2i(charsInLine, lineCount);
        }
    }
}
