// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.component;

import org.joml.Vector3i;
import org.terasology.engine.math.Side;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.List;

@Replicate
public class DisplayComponent implements Component<DisplayComponent> {
    private Vector3i monitorSize;
    private Side front;
    private String mode;
    private List<String> data;

    public DisplayComponent() {
    }

    public DisplayComponent(Vector3i monitorSize, Side front, String mode, List<String> data) {
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

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
