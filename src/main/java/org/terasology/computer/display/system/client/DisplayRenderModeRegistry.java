// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.system.client;

public interface DisplayRenderModeRegistry {
    void registerComputerMonitorRenderer(String modePrefix, DisplayRenderer displayRenderer);
}
