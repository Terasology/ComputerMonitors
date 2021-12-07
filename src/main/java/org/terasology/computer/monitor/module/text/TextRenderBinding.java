// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module.text;

import com.gempukku.lang.ExecutionException;
import org.terasology.computer.context.ComputerCallback;

public interface TextRenderBinding {
    TextRenderCommandSink getTextRenderCommandSink(int line, ComputerCallback computerCallback) throws ExecutionException;
}
