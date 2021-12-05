// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import org.joml.Vector2i;

import java.util.List;

public interface TextBuffer {
    Vector2i getSize();
    List<String> getData();
}
