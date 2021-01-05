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
package org.terasology.computer.display.system.server;

import org.joml.Vector3i;
import org.terasology.math.Side;
import org.terasology.multiBlock2.DefaultMultiBlockDefinition;
import org.terasology.world.block.BlockRegion;

import java.util.Collection;

public class Region3iMultiBlockDefinition extends DefaultMultiBlockDefinition {
    private BlockRegion region;
    private Side side;

    public Region3iMultiBlockDefinition(String multiBlockType, Vector3i mainBlock, Collection<Vector3i> memberBlocks, BlockRegion region, Side side) {
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
