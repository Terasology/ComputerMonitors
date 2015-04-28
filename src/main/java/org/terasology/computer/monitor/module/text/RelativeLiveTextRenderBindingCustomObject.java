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

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.monitor.component.ComputerMonitorComponent;
import org.terasology.computer.monitor.module.EntityRenderCommandSink;
import org.terasology.computer.monitor.system.server.ComputerMonitorServerSystem;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.Direction;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
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
        Vector3i directionVector = direction.getVector3i();
        Vector3i monitorLocation = new Vector3i(
                computerLocation.x + directionVector.x,
                computerLocation.y + directionVector.y,
                computerLocation.z + directionVector.z);
        EntityRef monitorEntity = multiBlockRegistry.getMultiBlockAtLocation(monitorLocation, ComputerMonitorServerSystem.MONITOR_MULTI_BLOCK_TYPE);

        if (monitorEntity == null)
            throw new ExecutionException(line, "Unable to locate device that could be rendered on");

        return new EntityTextRenderCommandSink(monitorEntity, mode);
    }

    private class EntityTextRenderCommandSink extends EntityRenderCommandSink implements TextRenderCommandSink {
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
            Vector2i maxCharacters = getMaxCharacters();
            return modePrefix + maxCharacters.x + "," + maxCharacters.y;
        }

        @Override
        public Vector2i getMaxCharacters() {
            ComputerMonitorComponent monitor = entityRef.getComponent(ComputerMonitorComponent.class);

            Vector3i monitorSize = monitor.getMonitorSize();

            int height = monitorSize.y;
            int width = Math.max(monitorSize.x, monitorSize.z);

            int lineCount = height * 5;
            int charsInLine = width * 8;

            return new Vector2i(charsInLine, lineCount);
        }
    }
}