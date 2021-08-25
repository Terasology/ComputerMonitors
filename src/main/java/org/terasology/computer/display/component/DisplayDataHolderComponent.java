// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.component;

import com.google.common.collect.Lists;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.terasology.engine.math.Side;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class DisplayDataHolderComponent implements Component<DisplayDataHolderComponent> {
    private Vector3i monitorSize = new Vector3i();
    private Side front;
    private String mode;
    private List<String> data = Lists.newArrayList();

    public DisplayDataHolderComponent() {
    }

    public DisplayDataHolderComponent(Vector3ic monitorSize, Side front, String mode, List<String> data) {
        this.monitorSize.set(monitorSize);
        this.front = front;
        this.mode = mode;
        this.data.addAll(data);
    }

    public Vector3i getMonitorSize() {
        return monitorSize;
    }

    public Side getFront() {
        return front;
    }

    public String getMode() {
        return mode;
    }

    public List<String> getData() {
        return data;
    }

    @Override
    public void copyFrom(DisplayDataHolderComponent other) {
        this.monitorSize.set(other.monitorSize);
        this.front = other.front;
        this.mode = other.mode;
        this.data.clear();
        this.data.addAll(other.data);
    }
}
