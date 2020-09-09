// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.graphics;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.math.geom.Vector2i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GraphicsOffScreenBuffer implements CustomObject, GraphicsRenderBinding, GraphicsBuffer,
        GraphicsRenderCommandSink {
    private final Vector2i resolution;
    private List<String> data = new ArrayList<>();

    public GraphicsOffScreenBuffer(Vector2i resolution) {
        this.resolution = resolution;
    }

    @Override
    public boolean isInstantRendering() {
        return true;
    }

    @Override
    public Vector2i getResolution() {
        return resolution;
    }

    @Override
    public GraphicsRenderCommandSink getGraphicsRenderCommandSink(int line, ComputerCallback computerCallback) throws ExecutionException {
        return this;
    }

    @Override
    public Collection<String> getType() {
        return Arrays.asList("GRAPHICS_RENDER_BINDING", "GRAPHICS_BUFFER");
    }

    @Override
    public int sizeOf() {
        return 4 + (resolution.x * resolution.y) / 1000;
    }

    @Override
    public List<String> getData() {
        return data;
    }

    @Override
    public List<String> getExistingData(int line) {
        return data;
    }

    @Override
    public void setData(int line, List<String> data) {
        this.data = data;
    }
}
