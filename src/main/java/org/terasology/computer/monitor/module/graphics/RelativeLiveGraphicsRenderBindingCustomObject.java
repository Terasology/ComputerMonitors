// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.display.component.DisplayComponent;
import org.terasology.computer.display.system.server.DisplayServerSystem;
import org.terasology.computer.monitor.module.EntityRenderCommandSink;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.math.Direction;
import org.terasology.math.geom.Vector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Collection;
import java.util.Collections;

public class RelativeLiveGraphicsRenderBindingCustomObject implements CustomObject, GraphicsRenderBinding {
    private final MultiBlockRegistry multiBlockRegistry;
    private final Direction direction;
    private final String mode;
    private final Vector2i resolution;

    public RelativeLiveGraphicsRenderBindingCustomObject(MultiBlockRegistry multiBlockRegistry, Direction direction,
                                                         String mode,
                                                         int width, int height) {
        this.multiBlockRegistry = multiBlockRegistry;
        this.direction = direction;
        this.mode = mode;
        this.resolution = new Vector2i(width, height);
    }

    @Override
    public Collection<String> getType() {
        return Collections.singleton("GRAPHICS_RENDER_BINDING");
    }

    @Override
    public int sizeOf() {
        return 4;
    }

    @Override
    public GraphicsRenderCommandSink getGraphicsRenderCommandSink(int line, ComputerCallback computerCallback) throws ExecutionException {
        Vector3f computerLocation = computerCallback.getComputerLocation();
        Vector3i directionVector = direction.getVector3i();
        Vector3i monitorLocation = new Vector3i(
                computerLocation.x + directionVector.x,
                computerLocation.y + directionVector.y,
                computerLocation.z + directionVector.z);
        EntityRef monitorEntity = multiBlockRegistry.getMultiBlockAtLocation(monitorLocation,
                DisplayServerSystem.MONITOR_MULTI_BLOCK_TYPE);

        if (monitorEntity == null)
            throw new ExecutionException(line, "Unable to locate device that could be rendered on");

        return new EntityGraphicsRenderCommandSink(monitorEntity, mode);
    }

    private class EntityGraphicsRenderCommandSink extends EntityRenderCommandSink implements GraphicsRenderCommandSink {
        private final EntityRef entityRef;
        private final String modePrefix;

        private EntityGraphicsRenderCommandSink(EntityRef entityRef, String modePrefix) {
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
            DisplayComponent monitor = entityRef.getComponent(DisplayComponent.class);
            Vector3i monitorSize = monitor.getMonitorSize();

            Vector2i resolution = getResolution();

            int width = Math.max(monitorSize.x, monitorSize.z);
            int height = monitorSize.y;

            if (width * GraphicsCardModuleCommonSystem.MAXIMUM_WIDTH_PIXEL_DENSITY_PER_BLOCK < resolution.x
                    || height * GraphicsCardModuleCommonSystem.MAXIMUM_HEIGHT_PIXEL_DENSITY_PER_BLOCK < resolution.y)
                throw new ExecutionException(line, "Graphics mode exceeds the maximum display resolution");

            return modePrefix + resolution.x + "," + resolution.y;
        }

        @Override
        public Vector2i getResolution() {
            return resolution;
        }
    }
}
