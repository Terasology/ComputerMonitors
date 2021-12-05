// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import org.joml.Vector2i;

import java.util.List;

public interface GraphicsBuffer {
    Vector2i getResolution();

    List<String> getData();
}
