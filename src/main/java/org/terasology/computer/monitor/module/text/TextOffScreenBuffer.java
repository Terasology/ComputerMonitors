// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.joml.Vector2i;
import org.terasology.computer.context.ComputerCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TextOffScreenBuffer implements CustomObject, TextRenderBinding, TextBuffer, TextRenderCommandSink {
    private Vector2i size;
    private List<String> data = new ArrayList<>();

    public TextOffScreenBuffer(Vector2i size) {
        this.size = size;
    }

    @Override
    public boolean isInstantRendering() {
        return true;
    }

    @Override
    public TextRenderCommandSink getTextRenderCommandSink(int line, ComputerCallback computerCallback) throws ExecutionException {
        return this;
    }

    @Override
    public Collection<String> getType() {
        return Arrays.asList("TEXT_RENDER_BINDING", "TEXT_BUFFER");
    }

    @Override
    public int sizeOf() {
        return 4 + 2 * (size.x * size.y);
    }

    @Override
    public Vector2i getSize() {
        return size;
    }

    @Override
    public List<String> getData() {
        return data;
    }

    @Override
    public Vector2i getMaxCharacters() {
        return size;
    }

    @Override
    public List<String> getExistingData(int line) {
        return data;
    }

    @Override
    public void setData(int line, List<String> dataList) {
        this.data = dataList;
    }
}
