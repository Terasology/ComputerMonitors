// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.system.client;

import org.terasology.engine.rendering.assets.material.Material;

import java.util.List;

public interface DisplayRenderer {
    Material renderMaterial(String mode, List<String> data);
}
