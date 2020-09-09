// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.component;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.entitySystem.entity.EntityRef;

public class DisplayRenderComponent implements Component {
    public EntityRef monitorChassis;
    public EntityRef screen;
}
