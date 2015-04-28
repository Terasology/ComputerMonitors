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
package org.terasology.computer.monitor.module;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.module.inventory.InventoryBinding;
import org.terasology.computer.monitor.system.server.ComputerMonitorServerSystem;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.inventory.InventoryAccessComponent;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.math.Direction;
import org.terasology.math.IntegerRange;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.multiBlock2.MultiBlockRegistry;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RelativeRenderBindingCustomObject implements CustomObject, RenderBinding {
    private MultiBlockRegistry multiBlockRegistry;
    private Direction direction;

    public RelativeRenderBindingCustomObject(MultiBlockRegistry multiBlockRegistry, Direction direction) {
        this.multiBlockRegistry = multiBlockRegistry;
        this.direction = direction;
    }

    @Override
    public String getType() {
        return "RENDER_BINDING";
    }

    @Override
    public EntityRef getComputerMonitorEntity(int line, ComputerCallback computerCallback) throws ExecutionException {
        Vector3f computerLocation = computerCallback.getComputerLocation();
        Vector3i directionVector = direction.getVector3i();
        Vector3i monitorLocation = new Vector3i(
                computerLocation.x + directionVector.x,
                computerLocation.y + directionVector.y,
                computerLocation.z + directionVector.z);
        EntityRef monitorEntity = multiBlockRegistry.getMultiBlockAtLocation(monitorLocation, ComputerMonitorServerSystem.MONITOR_MULTI_BLOCK_TYPE);

        if (monitorEntity == null)
            throw new ExecutionException(line, "Unable to locate device that could be rendered on");

        return monitorEntity;
    }
}
