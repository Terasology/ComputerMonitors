// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.component;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.gestalt.entitysystem.component.Component;

public class DisplayRenderComponent implements Component<DisplayRenderComponent> {
    public EntityRef monitorChassis;
    public EntityRef screen;

    @Override
    public void copyFrom(DisplayRenderComponent other) {
        this.monitorChassis = other.monitorChassis;
        this.screen = other.screen;
    }
}
