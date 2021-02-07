// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.system.server;

import org.joml.Vector3i;
import org.terasology.math.Side;
import org.terasology.multiBlock2.DefaultMultiBlockDefinition;
import org.terasology.world.block.BlockRegion;

import java.util.Collection;

public class RegionMultiBlockDefinition extends DefaultMultiBlockDefinition {
    private BlockRegion region;
    private Side side;

    public RegionMultiBlockDefinition(String multiBlockType, Vector3i mainBlock, Collection<Vector3i> memberBlocks, BlockRegion region, Side side) {
        super(multiBlockType, mainBlock, memberBlocks);
        this.region = region;
        this.side = side;
    }

    public BlockRegion getRegion() {
        return region;
    }

    public Side getSide() {
        return side;
    }
}
