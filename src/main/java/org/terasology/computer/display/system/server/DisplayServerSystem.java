// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.system.server;

import com.google.common.base.Predicate;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.terasology.computer.display.component.DisplayComponent;
import org.terasology.computer.display.component.DisplayDataHolderComponent;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.math.Side;
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
import org.terasology.world.block.BlockRegion;
import org.terasology.world.block.BlockRegionc;
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
                new UniformBaseMultiBlockRecipe<RegionMultiBlockDefinition>(blockEntityRegistry,
                        baseBlockPredicate,
                        new BiPredicate<EntityRef, EntityRef>() {
                            @Override
                            public boolean test(EntityRef baseBlockEntity, EntityRef otherBlockEntity) {
                                if (!baseBlockPredicate.apply(otherBlockEntity)) {
                                    return false;
                                }
                                Block baseBlock = baseBlockEntity.getComponent(BlockComponent.class).block;
                                Block otherBlock = otherBlockEntity.getComponent(BlockComponent.class).block;

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
                    protected RegionMultiBlockDefinition createMultiBlockDefinition(BlockRegionc multiBlockRegion) {
                        Vector3i mainBlock = multiBlockRegion.getMin(new Vector3i());
                        Block block = worldProvider.getBlock(mainBlock);
                        Side frontSide = getBlockSide(block);

                        List<Vector3i> memberLocations = new LinkedList<Vector3i>();
                        for (Vector3ic vector3i : multiBlockRegion) {
                            if (!vector3i.equals(mainBlock)) {
                                memberLocations.add(new Vector3i(vector3i));
                            }
                        }

                        return new RegionMultiBlockDefinition(MONITOR_MULTI_BLOCK_TYPE, mainBlock, memberLocations, new BlockRegion(multiBlockRegion), frontSide);
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
    public void computerMonitorMultiBlockCreated(MultiBlockFormed<RegionMultiBlockDefinition> event, EntityRef multiBlockEntity) {
        if (event.getType().equals(MONITOR_MULTI_BLOCK_TYPE)) {
            BlockRegionc region = event.getMultiBlockDefinition().getRegion();

            int height = region.getSizeY();
            int x = region.getSizeX();
            int z = region.getSizeZ();

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
