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

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.display.component.DisplayComponent;
import org.terasology.computer.display.system.server.DisplayServerSystem;
import org.terasology.computer.monitor.module.EntityRenderCommandSink;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.Direction;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.multiBlock2.MultiBlockRegistry;

import java.util.Collection;
import java.util.Collections;

public class RelativeLiveGraphicsRenderBindingCustomObject implements CustomObject, GraphicsRenderBinding {
    private MultiBlockRegistry multiBlockRegistry;
    private Direction direction;
    private String mode;
    private Vector2i resolution;

    public RelativeLiveGraphicsRenderBindingCustomObject(MultiBlockRegistry multiBlockRegistry, Direction direction, String mode,
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
        EntityRef monitorEntity = multiBlockRegistry.getMultiBlockAtLocation(monitorLocation, DisplayServerSystem.MONITOR_MULTI_BLOCK_TYPE);

        if (monitorEntity == null)
            throw new ExecutionException(line, "Unable to locate device that could be rendered on");

        return new EntityGraphicsRenderCommandSink(monitorEntity, mode);
    }

    private class EntityGraphicsRenderCommandSink extends EntityRenderCommandSink implements GraphicsRenderCommandSink {
        private EntityRef entityRef;
        private String modePrefix;

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
