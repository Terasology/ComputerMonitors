// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module;

import com.gempukku.lang.ExecutionException;
import org.terasology.computer.display.component.DisplayComponent;
import org.terasology.engine.entitySystem.entity.EntityRef;

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
        DisplayComponent monitor = entityRef.getComponent(DisplayComponent.class);
        ensureMonitorInCorrectMode(line, monitor);
        return monitor.getData();
    }

    @Override
    public void setData(int line, List<String> data) throws ExecutionException {
        DisplayComponent monitor = entityRef.getComponent(DisplayComponent.class);
        ensureMonitorInCorrectMode(line, monitor);
        monitor.setData(data);
        entityRef.saveComponent(monitor);
    }

    private void ensureMonitorInCorrectMode(int line, DisplayComponent monitor) throws ExecutionException {
        String mode = getRequiredMode(line);
        if (monitor.getMode() == null || !monitor.getMode().equals(mode)) {
            monitor.setMode(mode);
            monitor.setData(new ArrayList<>());
            entityRef.saveComponent(monitor);
        }
    }
}
