// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.system.client;

import org.joml.Vector3f;
import org.joml.Vector3i;
import org.terasology.computer.display.component.DisplayComponent;
import org.terasology.computer.display.component.DisplayRenderComponent;
import org.terasology.engine.entitySystem.entity.EntityBuilder;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeRemoveComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnAddedComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.math.Side;
import org.terasology.engine.registry.In;
import org.terasology.engine.registry.Share;
import org.terasology.engine.rendering.assets.material.Material;
import org.terasology.engine.rendering.assets.mesh.MeshBuilder;
import org.terasology.engine.rendering.logic.MeshComponent;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.block.BlockPart;
import org.terasology.engine.world.block.shapes.BlockMeshPart;
import org.terasology.engine.world.block.shapes.BlockShape;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.nui.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RegisterSystem(RegisterMode.CLIENT)
@Share(DisplayRenderModeRegistry.class)
public class DisplayClientSystem extends BaseComponentSystem implements DisplayRenderModeRegistry {
    private Map<String, DisplayRenderer> computerMonitorRendererMap = new HashMap<>();

    @In
    private EntityManager entityManager;

    private DefaultDisplayRenderer defaultDisplayRenderer = new DefaultDisplayRenderer();

    @Override
    public void registerComputerMonitorRenderer(String modePrefix, DisplayRenderer displayRenderer) {
        computerMonitorRendererMap.put(modePrefix, displayRenderer);
    }

    @ReceiveEvent
    public void onMonitorAdded(OnAddedComponent event, EntityRef monitorEntity, DisplayComponent monitor) {
        Vector3i monitorSize = monitor.getMonitorSize();
        Side front = monitor.getFront();
        Vector3f worldPosition = monitorEntity.getComponent(LocationComponent.class).getWorldPosition(new Vector3f());

        DisplayRenderComponent computerRenderComponent = new DisplayRenderComponent();

        computerRenderComponent.monitorChassis = createChassisRenderingEntity(worldPosition, monitorSize, front);
        computerRenderComponent.screen = createScreenRenderingEntity(worldPosition, monitorSize, front, monitor.getMode(), monitor.getData());
        monitorEntity.addComponent(computerRenderComponent);
    }

    @ReceiveEvent
    public void onMonitorDataUpdated(OnChangedComponent event, EntityRef monitorEntity, DisplayComponent monitor) {
        DisplayRenderComponent computerRender = monitorEntity.getComponent(DisplayRenderComponent.class);
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
            material = defaultDisplayRenderer.renderMaterial(mode, data);
        }
        return material;
    }

    private Material render(String mode, List<String> data) {
        if (mode != null) {
            for (Map.Entry<String, DisplayRenderer> keyComputerMonitorRenderer : computerMonitorRendererMap.entrySet()) {
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
        meshComponent.material = Assets.getMaterial("ComputerMonitors:ComputerMonitor").get();
        meshComponent.translucent = false;
        meshComponent.hideFromOwner = false;
        meshComponent.color = new Color(Color.grey);

        LocationComponent locationComponent = new LocationComponent(location);

        EntityBuilder entityBuilder = entityManager.newBuilder();
        entityBuilder.setPersistent(false);
        entityBuilder.addComponent(locationComponent);
        entityBuilder.addComponent(meshComponent);

        return entityBuilder.build();
    }

    private void addMeshForSide(MeshBuilder meshBuilder, Side side, Vector3i monitorSize) {
        BlockShape blockShape = Assets.get("engine:cube", BlockShape.class).get();
        BlockMeshPart meshPart = blockShape.getMeshPart(BlockPart.fromSide(side));

        meshBuilder.addPoly(
                getTopLeft(monitorSize, meshPart),
                getTopRight(monitorSize, meshPart),
                getBottomRight(monitorSize, meshPart),
                getBottomLeft(monitorSize, meshPart));
        meshBuilder.addColor(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        meshBuilder.addTexCoord((float) 0, (float) 0);
        meshBuilder.addTexCoord((float) 0, 0.0625f);
        meshBuilder.addTexCoord(0.0625f, 0.0625f);
        meshBuilder.addTexCoord(0.0625f, (float) 0);
    }

    private void addNormalizedMeshForSide(MeshBuilder meshBuilder, Side side, Vector3i monitorSize) {
        BlockShape blockShape = Assets.get("engine:cube", BlockShape.class).get();
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

        if (indicesWithValue[3] && indicesWithValue[0]) {
            return 3;
        }

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
    public void onMonitorRemoved(BeforeRemoveComponent event, EntityRef monitorEntity, DisplayComponent monitor) {
        DisplayRenderComponent renderComponent = monitorEntity.getComponent(DisplayRenderComponent.class);
        renderComponent.monitorChassis.getComponent(MeshComponent.class).mesh.dispose();
        renderComponent.monitorChassis.destroy();
        renderComponent.screen.getComponent(MeshComponent.class).mesh.dispose();
        renderComponent.screen.getComponent(MeshComponent.class).material.dispose();
        renderComponent.screen.destroy();
        monitorEntity.removeComponent(DisplayRenderComponent.class);
    }
}
