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

import com.gempukku.lang.ExecutionException;
import org.terasology.computer.monitor.component.ComputerMonitorComponent;
import org.terasology.entitySystem.entity.EntityRef;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityRenderCommandSink implements RenderCommandSink {
    private EntityRef entityRef;

    public EntityRenderCommandSink(EntityRef entityRef) {
        this.entityRef = entityRef;
    }

    protected abstract String getRequiredMode(int line) throws ExecutionException;

    @Override
    public List<String> getExistingData(int line) throws ExecutionException {
        ComputerMonitorComponent monitor = entityRef.getComponent(ComputerMonitorComponent.class);
        ensureMonitorInCorrectMode(line, monitor);
        return monitor.getData();
    }

    @Override
    public void setData(int line, List<String> data) throws ExecutionException {
        ComputerMonitorComponent monitor = entityRef.getComponent(ComputerMonitorComponent.class);
        ensureMonitorInCorrectMode(line, monitor);
        monitor.setData(data);
        entityRef.saveComponent(monitor);
    }

    private void ensureMonitorInCorrectMode(int line, ComputerMonitorComponent monitor) throws ExecutionException {
        String mode = getRequiredMode(line);
        if (monitor.getMode() == null || !monitor.getMode().equals(mode)) {
            monitor.setMode(mode);
            monitor.setData(new ArrayList<>());
            entityRef.saveComponent(monitor);
        }
    }
}
