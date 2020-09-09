// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.component;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.math.Side;
import org.terasology.math.geom.Vector3i;

import java.util.List;

public class DisplayDataHolderComponent implements Component {
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
