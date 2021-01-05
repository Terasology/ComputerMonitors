/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.computer.monitor.module.graphics;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.joml.Vector2i;
import org.terasology.computer.context.ComputerCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class GraphicsOffScreenBuffer implements CustomObject, GraphicsRenderBinding, GraphicsBuffer, GraphicsRenderCommandSink {
    private Vector2i resolution;
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
