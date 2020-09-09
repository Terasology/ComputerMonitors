// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.system.server;

import org.terasology.engine.math.Region3i;
import org.terasology.engine.math.Side;
import org.terasology.math.geom.Vector3i;
import org.terasology.multiBlock2.DefaultMultiBlockDefinition;

import java.util.Collection;

public class Region3iMultiBlockDefinition extends DefaultMultiBlockDefinition {
    private final Region3i region;
    private final Side side;

    public Region3iMultiBlockDefinition(String multiBlockType, Vector3i mainBlock, Collection<Vector3i> memberBlocks,
                                        Region3i region, Side side) {
        super(multiBlockType, mainBlock, memberBlocks);
        this.region = region;
        this.side = side;
    }

    public Region3i getRegion() {
        return region;
    }

    public Side getSide() {
        return side;
    }
}
