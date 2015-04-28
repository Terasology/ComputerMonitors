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
import org.terasology.computer.monitor.system.client.renderer.TextComputerMonitorRenderer;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.BeforeRemoveComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnAddedComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.location.LocationComponent;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3f;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.In;
import org.terasology.registry.Share;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.assets.font.FontCharacter;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.assets.mesh.Mesh;
import org.terasology.rendering.assets.mesh.MeshBuilder;
import org.terasology.rendering.logic.MeshComponent;
import org.terasology.rendering.nui.Color;
import org.terasology.world.block.BlockPart;
import org.terasology.world.block.shapes.BlockMeshPart;
import org.terasology.world.block.shapes.BlockShape;

import javax.vecmath.Vector2f;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RegisterSystem(RegisterMode.CLIENT)
@Share(ComputerMonitorRenderModeRegistry.class)
public class ComputerMonitorClientSystem extends BaseComponentSystem implements ComputerMonitorRenderModeRegistry {
    public static final float MONITOR_BACKGROUND_DEPTH = 0.98f;

    private Map<String, ComputerMonitorRenderer> computerMonitorRendererMap = new HashMap<>();

    @In
    private EntityManager entityManager;

    @Override
    public void preBegin() {
        registerComputerMonitorRenderer("Text:", new TextComputerMonitorRenderer());
    }

    @Override
    public void registerComputerMonitorRenderer(String modePrefix, ComputerMonitorRenderer computerMonitorRenderer) {
        computerMonitorRendererMap.put(modePrefix, computerMonitorRenderer);
    }

    @ReceiveEvent
    public void onMonitorAdded(OnAddedComponent event, EntityRef monitorEntity, ComputerMonitorComponent monitor) {
        Vector3i monitorSize = monitor.getMonitorSize();
        Side front = monitor.getFront();
        Vector3f worldPosition = monitorEntity.getComponent(LocationComponent.class).getWorldPosition();

        ComputerRenderComponent computerRenderComponent = new ComputerRenderComponent();

        computerRenderComponent.monitorChassis = createChassisRenderingEntity(worldPosition, monitorSize, front);
        computerRenderComponent.screen = createScreenRenderingEntity(worldPosition, monitorSize, front, monitor.getMode(), monitor.getData());
        monitorEntity.addComponent(computerRenderComponent);
    }

    @ReceiveEvent
    public void onMonitorDataUpdated(OnChangedComponent event, EntityRef monitorEntity, ComputerMonitorComponent monitor) {
        ComputerRenderComponent computerRender = monitorEntity.getComponent(ComputerRenderComponent.class);
        EntityRef screen = computerRender.screen;
        MeshComponent mesh = screen.getComponent(MeshComponent.class);
        mesh.material.dispose();
        mesh.material = createMaterial(monitor.getMode(), monitor.getData());
        screen.saveComponent(mesh);
    }

    private EntityRef createScreenRenderingEntity(Vector3f location, Vector3i monitorSize, Side front, String mode, List<String> data) {
        MeshBuilder meshBuilder = new MeshBuilder();
        addNormalizedMeshForSide(meshBuilder, front, monitorSize);

        Material material = createMaterial(mode, data);

        MeshComponent meshComponent = new MeshComponent();
        meshComponent.mesh = meshBuilder.build();
        meshComponent.material = material;
        meshComponent.translucent = false;
        meshComponent.hideFromOwner = false;
        meshComponent.color = Color.WHITE;

        LocationComponent locationComponent = new LocationComponent(location);

        EntityBuilder entityBuilder = entityManager.newBuilder();
        entityBuilder.setPersistent(false);
        entityBuilder.addComponent(locationComponent);
        entityBuilder.addComponent(meshComponent);

        return entityBuilder.build();
    }

    private Material createMaterial(String mode, List<String> data) {
        Material material = render(mode, data);
        if (material == null) {
            material = Assets.getMaterial("ComputerMonitors:ComputerMonitor");
        }
        return material;
    }

    private Material render(String mode, List<String> data) {
        if (mode != null) {
            for (Map.Entry<String, ComputerMonitorRenderer> keyComputerMonitorRenderer : computerMonitorRendererMap.entrySet()) {
                if (mode.startsWith(keyComputerMonitorRenderer.getKey())) {
                    return keyComputerMonitorRenderer.getValue().renderMaterial(mode, data);
                }
            }
        }
        return null;
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
        addMeshForSide(meshBuilder, side, monitorSize, 0, 0.0625f);
    }

    private void addMeshForSide(MeshBuilder meshBuilder, Side side, Vector3i monitorSize, float textureMin, float textureMax) {
        BlockShape blockShape = (BlockShape) Assets.get(AssetType.SHAPE, "engine:cube");
        BlockMeshPart meshPart = blockShape.getMeshPart(BlockPart.fromSide(side));

        meshBuilder.addPoly(
                getTopLeft(monitorSize, meshPart),
                getTopRight(monitorSize, meshPart),
                getBottomRight(monitorSize, meshPart),
                getBottomLeft(monitorSize, meshPart));
        meshBuilder.addColor(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        meshBuilder.addTexCoord(textureMin, textureMin);
        meshBuilder.addTexCoord(textureMin, textureMax);
        meshBuilder.addTexCoord(textureMax, textureMax);
        meshBuilder.addTexCoord(textureMax, textureMin);
    }

    private void addNormalizedMeshForSide(MeshBuilder meshBuilder, Side side, Vector3i monitorSize) {
        BlockShape blockShape = (BlockShape) Assets.get(AssetType.SHAPE, "engine:cube");
        BlockMeshPart meshPart = blockShape.getMeshPart(BlockPart.fromSide(side));

        Vector3f[] sideVectors =new Vector3f[] {
                getTopLeft(monitorSize, meshPart),
                getTopRight(monitorSize, meshPart),
                getBottomRight(monitorSize, meshPart),
                getBottomLeft(monitorSize, meshPart) };

        int startIndex = findIndexOfTopLeft(sideVectors);

        meshBuilder.addPoly(
                sideVectors[startIndex],
                sideVectors[(startIndex+1)%4],
                sideVectors[(startIndex+2)%4],
                sideVectors[(startIndex+3)%4]);
        meshBuilder.addColor(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        meshBuilder.addTexCoord(0, 0);
        meshBuilder.addTexCoord(1, 0);
        meshBuilder.addTexCoord(1, 1);
        meshBuilder.addTexCoord(0, 1);
    }

    private int findIndexOfTopLeft(Vector3f[] sideVectors) {
        float maxY = Float.MIN_VALUE;
        for (Vector3f sideVector : sideVectors) {
            maxY = Math.max(maxY, sideVector.y);
        }

        boolean[] indicesWithValue = new boolean[4];
        for (int i=0; i<sideVectors.length; i++) {
            indicesWithValue[i] = (sideVectors[i].y == maxY);
        }

        if (indicesWithValue[3] && indicesWithValue[0])
            return 3;

        for (int i=0; i<3; i++) {
            if (indicesWithValue[i]) {
                return i;
            }
        }
        return -1;
    }

    private Vector3f getBottomLeft(Vector3i monitorSize, BlockMeshPart meshPart) {
        return applySizeToSideVector(monitorSize, meshPart.getVertex(1));
    }

    private Vector3f getBottomRight(Vector3i monitorSize, BlockMeshPart meshPart) {
        return applySizeToSideVector(monitorSize, meshPart.getVertex(2));
    }

    private Vector3f getTopRight(Vector3i monitorSize, BlockMeshPart meshPart) {
        return applySizeToSideVector(monitorSize, meshPart.getVertex(3));
    }

    private Vector3f getTopLeft(Vector3i monitorSize, BlockMeshPart meshPart) {
        return applySizeToSideVector(monitorSize, meshPart.getVertex(0));
    }

    private Vector3f applySizeToSideVector(Vector3i monitorSize, Vector3f v2) {
        return new Vector3f(applySize(v2.x, monitorSize.x), applySize(v2.y, monitorSize.y), applySize(v2.z, monitorSize.z));
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
        renderComponent.monitorChassis.getComponent(MeshComponent.class).mesh.dispose();
        renderComponent.monitorChassis.destroy();
        renderComponent.screen.getComponent(MeshComponent.class).mesh.dispose();
        renderComponent.screen.getComponent(MeshComponent.class).material.dispose();
        renderComponent.screen.destroy();
        monitorEntity.removeComponent(ComputerRenderComponent.class);
    }
}
