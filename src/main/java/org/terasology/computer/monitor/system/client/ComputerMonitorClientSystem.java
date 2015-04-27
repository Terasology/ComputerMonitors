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
package org.terasology.computer.monitor.system.client;

import org.terasology.asset.AssetType;
import org.terasology.asset.Assets;
import org.terasology.computer.monitor.component.ComputerMonitorComponent;
import org.terasology.computer.monitor.component.ComputerRenderComponent;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.BeforeRemoveComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnAddedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.In;
import org.terasology.rendering.assets.mesh.MeshBuilder;
import org.terasology.rendering.logic.MeshComponent;
import org.terasology.rendering.nui.Color;
import org.terasology.world.block.BlockPart;
import org.terasology.world.block.shapes.BlockMeshPart;
import org.terasology.world.block.shapes.BlockShape;

@RegisterSystem(RegisterMode.CLIENT)
public class ComputerMonitorClientSystem extends BaseComponentSystem {
    @In
    private EntityManager entityManager;

    @ReceiveEvent
    public void onMonitorAdded(OnAddedComponent event, EntityRef monitorEntity, ComputerMonitorComponent monitor) {
        Vector3i monitorSize = monitor.getMonitorSize();
        Side front = monitor.getFront();
        Vector3f worldPosition = monitorEntity.getComponent(LocationComponent.class).getWorldPosition();

        ComputerRenderComponent computerRenderComponent = new ComputerRenderComponent();

        computerRenderComponent.monitorChassis = createChassisRenderingEntity(worldPosition, monitorSize, front);
        computerRenderComponent.screenBackground = createScreenBackgroundRenderingEntity(worldPosition, monitorSize, front);
        monitorEntity.addComponent(computerRenderComponent);

    }

    private EntityRef createScreenBackgroundRenderingEntity(Vector3f location, Vector3i monitorSize, Side front) {
        MeshBuilder meshBuilder = new MeshBuilder();

        addMeshForSide(meshBuilder, front, monitorSize);

        MeshComponent meshComponent = new MeshComponent();
        meshComponent.mesh = meshBuilder.build();
        meshComponent.material = Assets.getMaterial("engine:default");
        meshComponent.translucent = false;
        meshComponent.hideFromOwner = false;
        meshComponent.color = Color.BLACK;

        LocationComponent locationComponent = new LocationComponent(location);

        EntityBuilder entityBuilder = entityManager.newBuilder();
        entityBuilder.setPersistent(false);
        entityBuilder.addComponent(locationComponent);
        entityBuilder.addComponent(meshComponent);

        return entityBuilder.build();
    }

    private EntityRef createChassisRenderingEntity(Vector3f location, Vector3i monitorSize, Side front) {
        MeshBuilder meshBuilder = new MeshBuilder();

        for (Side side : Side.values()) {
            if (side != front) {
                addMeshForSide(meshBuilder, side, monitorSize);
            }
        }

        MeshComponent meshComponent = new MeshComponent();
        meshComponent.mesh = meshBuilder.build();
        meshComponent.material = Assets.getMaterial("ComputerMonitors:ComputerMonitor");
        meshComponent.translucent = false;
        meshComponent.hideFromOwner = false;
        meshComponent.color = Color.GREY;

        LocationComponent locationComponent = new LocationComponent(location);

        EntityBuilder entityBuilder = entityManager.newBuilder();
        entityBuilder.setPersistent(false);
        entityBuilder.addComponent(locationComponent);
        entityBuilder.addComponent(meshComponent);

        return entityBuilder.build();
    }

    private void addMeshForSide(MeshBuilder meshBuilder, Side side, Vector3i monitorSize) {
        BlockShape blockShape = (BlockShape) Assets.get(AssetType.SHAPE, "engine:cube");
        BlockMeshPart meshPart = blockShape.getMeshPart(BlockPart.fromSide(side));

        Vector3f v1 = meshPart.getVertex(0);
        Vector3f v2 = meshPart.getVertex(1);
        Vector3f v3 = meshPart.getVertex(2);
        Vector3f v4 = meshPart.getVertex(3);

        meshBuilder.addPoly(
                new Vector3f(applySize(v1.x, monitorSize.x), applySize(v1.y, monitorSize.y), applySize(v1.z, monitorSize.z)),
                new Vector3f(applySize(v4.x, monitorSize.x), applySize(v4.y, monitorSize.y), applySize(v4.z, monitorSize.z)),
                new Vector3f(applySize(v3.x, monitorSize.x), applySize(v3.y, monitorSize.y), applySize(v3.z, monitorSize.z)),
                new Vector3f(applySize(v2.x, monitorSize.x), applySize(v2.y, monitorSize.y), applySize(v2.z, monitorSize.z)));
        meshBuilder.addColor(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        meshBuilder.addTexCoord(0, 0);
        meshBuilder.addTexCoord(0, 0.0625f);
        meshBuilder.addTexCoord(0.0625f, 0.0625f);
        meshBuilder.addTexCoord(0.0625f, 0);
    }

    private float applySize(float coord, int size) {
        if (coord == -0.5f) {
            return -0.5f;
        } else {
            return coord + size - 1;
        }
    }

    @ReceiveEvent
    public void onMonitorRemoved(BeforeRemoveComponent event, EntityRef monitorEntity, ComputerMonitorComponent monitor) {
        ComputerRenderComponent renderComponent = monitorEntity.getComponent(ComputerRenderComponent.class);
        renderComponent.monitorChassis.destroy();
        renderComponent.screenBackground.destroy();
        monitorEntity.removeComponent(ComputerRenderComponent.class);
    }
}
