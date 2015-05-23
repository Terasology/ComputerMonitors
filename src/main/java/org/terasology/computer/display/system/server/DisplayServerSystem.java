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

import com.google.common.base.Predicate;
import org.terasology.computer.display.component.DisplayComponent;
import org.terasology.computer.display.component.DisplayDataHolderComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.Region3i;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3i;
import org.terasology.multiBlock2.MultiBlockRegistry;
import org.terasology.multiBlock2.component.MultiBlockCandidateComponent;
import org.terasology.multiBlock2.event.BeforeMultiBlockUnloaded;
import org.terasology.multiBlock2.event.MultiBlockFormed;
import org.terasology.multiBlock2.event.MultiBlockLoaded;
import org.terasology.multiBlock2.recipe.UniformBaseMultiBlockRecipe;
import org.terasology.registry.In;
import org.terasology.world.BlockEntityRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockComponent;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.world.block.family.SideDefinedBlockFamily;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

@RegisterSystem(RegisterMode.AUTHORITY)
public class DisplayServerSystem extends BaseComponentSystem {
    public static final String MONITOR_MULTI_BLOCK_CANDIDATE_KEY = "ComputerMonitors:display";
    public static final String MONITOR_MULTI_BLOCK_TYPE = "ComputerMonitors:display";
    public static final int MAX_MONITOR_DIMENSION = 5;
    @In
    private MultiBlockRegistry multiBlockRegistry;
    @In
    private BlockEntityRegistry blockEntityRegistry;
    @In
    private WorldProvider worldProvider;

    @Override
    public void preBegin() {
        Predicate<EntityRef> baseBlockPredicate = new Predicate<EntityRef>() {
            @Override
            public boolean apply(@Nullable EntityRef input) {
                MultiBlockCandidateComponent multiBlockCandidateComponent = input.getComponent(MultiBlockCandidateComponent.class);
                return multiBlockCandidateComponent != null
                        && multiBlockCandidateComponent.getType().contains(MONITOR_MULTI_BLOCK_CANDIDATE_KEY);
            }
        };
        multiBlockRegistry.registerMultiBlockType(MONITOR_MULTI_BLOCK_CANDIDATE_KEY,
                new UniformBaseMultiBlockRecipe<Region3iMultiBlockDefinition>(blockEntityRegistry,
                        baseBlockPredicate,
                        new BiPredicate<EntityRef, EntityRef>() {
                            @Override
                            public boolean test(EntityRef baseBlockEntity, EntityRef otherBlockEntity) {
                                if (!baseBlockPredicate.apply(otherBlockEntity)) {
                                    return false;
                                }
                                Block baseBlock = baseBlockEntity.getComponent(BlockComponent.class).getBlock();
                                Block otherBlock = otherBlockEntity.getComponent(BlockComponent.class).getBlock();

                                return getBlockSide(baseBlock) == getBlockSide(otherBlock);
                            }
                        },
                        new Predicate<Vector3i>() {
                            @Override
                            public boolean apply(@Nullable Vector3i input) {
                                int maxValue = Math.max(Math.max(input.x, input.z), input.y);
                                int minValue = Math.min(input.x, input.z);
                                return minValue == 1 && maxValue <= MAX_MONITOR_DIMENSION;
                            }
                        }) {
                    @Override
                    protected Region3iMultiBlockDefinition createMultiBlockDefinition(Region3i multiBlockRegion) {
                        Vector3i mainBlock = multiBlockRegion.min();
                        Block block = worldProvider.getBlock(mainBlock);
                        Side frontSide = getBlockSide(block);

                        List<Vector3i> memberLocations = new LinkedList<Vector3i>();
                        for (Vector3i vector3i : multiBlockRegion) {
                            if (!vector3i.equals(mainBlock)) {
                                memberLocations.add(vector3i);
                            }
                        }

                        return new Region3iMultiBlockDefinition(MONITOR_MULTI_BLOCK_TYPE, mainBlock, memberLocations, multiBlockRegion, frontSide);
                    }
                });
    }

    private Side getBlockSide(Block block) {
        BlockFamily blockFamily = block.getBlockFamily();
        if (blockFamily instanceof SideDefinedBlockFamily) {
            return ((SideDefinedBlockFamily) blockFamily).getSide(block);
        }
        return null;
    }

    @ReceiveEvent
    public void computerMonitorMultiBlockCreated(MultiBlockFormed<Region3iMultiBlockDefinition> event, EntityRef multiBlockEntity) {
        if (event.getType().equals(MONITOR_MULTI_BLOCK_TYPE)) {
            Region3i region = event.getMultiBlockDefinition().getRegion();

            int height = region.sizeY();
            int x = region.sizeX();
            int z = region.sizeZ();

            Vector3i size = new Vector3i(x, height, z);

            Side front = event.getMultiBlockDefinition().getSide();

            DisplayComponent component = new DisplayComponent(size, front, null, new ArrayList<>());
            multiBlockEntity.addComponent(component);
        }
    }

    @ReceiveEvent
    public void computerMonitorMultiBlockUnloaded(BeforeMultiBlockUnloaded event, EntityRef multiBlockEntity) {
        if (event.getType().equals(MONITOR_MULTI_BLOCK_TYPE)) {
            EntityRef mainBlockEntity = event.getMainBlockEntity();

            DisplayComponent monitor = multiBlockEntity.getComponent(DisplayComponent.class);

            DisplayDataHolderComponent component = new DisplayDataHolderComponent(
                    monitor.getMonitorSize(), monitor.getFront(), monitor.getMode(), monitor.getData());
            mainBlockEntity.addComponent(component);
        }
    }

    @ReceiveEvent
    public void computerMonitorMultiBlockLoaded(MultiBlockLoaded event, EntityRef multiBlockEntity) {
        if (event.getType().equals(MONITOR_MULTI_BLOCK_TYPE)) {
            EntityRef mainBlockEntity = event.getMainBlockEntity();
            DisplayDataHolderComponent component = mainBlockEntity.getComponent(DisplayDataHolderComponent.class);

            DisplayComponent monitor = new DisplayComponent(
                    component.getMonitorSize(), component.getFront(), component.getMode(), component.getData());
            multiBlockEntity.addComponent(monitor);

            mainBlockEntity.removeComponent(DisplayDataHolderComponent.class);
        }
    }
}
