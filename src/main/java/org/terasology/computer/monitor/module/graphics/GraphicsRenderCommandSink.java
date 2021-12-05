// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import org.joml.Vector2i;
import org.terasology.computer.monitor.module.RenderCommandSink;

public interface GraphicsRenderCommandSink extends RenderCommandSink {
    Vector2i getResolution();
}
