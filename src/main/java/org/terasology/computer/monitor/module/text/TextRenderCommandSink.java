// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import org.joml.Vector2ic;
import org.terasology.computer.monitor.module.RenderCommandSink;

public interface TextRenderCommandSink extends RenderCommandSink {
    Vector2ic getMaxCharacters();
}
