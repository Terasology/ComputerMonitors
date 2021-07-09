// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.component;

import org.joml.Vector3i;
import org.terasology.engine.math.Side;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

public class DisplayDataHolderComponent implements Component<DisplayDataHolderComponent> {
    private Vector3i monitorSize;
    private Side front;
    private String mode;
    private List<String> data;

    public DisplayDataHolderComponent() {
    }

    public DisplayDataHolderComponent(Vector3i monitorSize, Side front, String mode, List<String> data) {
        this.monitorSize = monitorSize;
        this.front = front;
        this.mode = mode;
        this.data = data;
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
}
