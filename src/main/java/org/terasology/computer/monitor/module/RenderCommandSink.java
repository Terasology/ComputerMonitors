// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module;

import com.gempukku.lang.ExecutionException;

import java.util.List;

public interface RenderCommandSink {
    List<String> getExistingData(int line) throws ExecutionException;

    void setData(int line, List<String> data) throws ExecutionException;

    boolean isInstantRendering();
}
