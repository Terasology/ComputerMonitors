// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;

public interface TextRenderBinding {
    TextRenderCommandSink getTextRenderCommandSink(int line, ComputerCallback computerCallback) throws ExecutionException;
}
