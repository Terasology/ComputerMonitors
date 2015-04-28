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
import java.util.List;

@RegisterSystem(RegisterMode.CLIENT)
public class ComputerMonitorClientSystem extends BaseComponentSystem {
    public static final float MONITOR_BACKGROUND_DEPTH = 0.98f;
    public static final int fontWidth = 10;
    public static final int fontHeight = 16;

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
        computerRenderComponent.screenForeground = createScreenForegroundRenderingEntity(worldPosition, monitorSize, front, monitor.getCharactersInLineCount(), monitor.getLineCount(), monitor.getLines());
        monitorEntity.addComponent(computerRenderComponent);
    }

    @ReceiveEvent
    public void onMonitorTextUpdated(OnChangedComponent event, EntityRef monitorEntity, ComputerMonitorComponent monitor) {
        Font font = Assets.getFont("ModularComputers:November");

        ComputerRenderComponent computerRender = monitorEntity.getComponent(ComputerRenderComponent.class);
        EntityRef screenForeground = computerRender.screenForeground;
        MeshComponent mesh = screenForeground.getComponent(MeshComponent.class);
        mesh.mesh.dispose();
        mesh.mesh = createMeshForMonitor(monitor.getMonitorSize(), monitor.getFront(), monitor.getCharactersInLineCount(), monitor.getLineCount(),
                monitor.getLines(), font);
        screenForeground.saveComponent(mesh);
    }

    private EntityRef createScreenBackgroundRenderingEntity(Vector3f location, Vector3i monitorSize, Side front) {
        MeshBuilder meshBuilder = new MeshBuilder();

        BlockShape blockShape = (BlockShape) Assets.get(AssetType.SHAPE, "engine:cube");
        BlockMeshPart meshPart = blockShape.getMeshPart(BlockPart.fromSide(front));

        Vector3f v1 = meshPart.getVertex(0);
        Vector3f v2 = meshPart.getVertex(1);
        Vector3f v3 = meshPart.getVertex(2);
        Vector3f v4 = meshPart.getVertex(3);

        float xMult = (front.getVector3i().x != 0) ? MONITOR_BACKGROUND_DEPTH : 1f;
        float yMult = (front.getVector3i().y != 0) ? MONITOR_BACKGROUND_DEPTH : 1f;
        float zMult = (front.getVector3i().z != 0) ? MONITOR_BACKGROUND_DEPTH : 1f;

        meshBuilder.addPoly(
                new Vector3f(xMult * applySize(v1.x, monitorSize.x), yMult * applySize(v1.y, monitorSize.y), zMult * applySize(v1.z, monitorSize.z)),
                new Vector3f(xMult * applySize(v4.x, monitorSize.x), yMult * applySize(v4.y, monitorSize.y), zMult * applySize(v4.z, monitorSize.z)),
                new Vector3f(xMult * applySize(v3.x, monitorSize.x), yMult * applySize(v3.y, monitorSize.y), zMult * applySize(v3.z, monitorSize.z)),
                new Vector3f(xMult * applySize(v2.x, monitorSize.x), yMult * applySize(v2.y, monitorSize.y), zMult * applySize(v2.z, monitorSize.z)));
        meshBuilder.addColor(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        meshBuilder.addTexCoord(0, 0);
        meshBuilder.addTexCoord(0, 0.0625f);
        meshBuilder.addTexCoord(0.0625f, 0.0625f);
        meshBuilder.addTexCoord(0.0625f, 0);

        MeshComponent meshComponent = new MeshComponent();
        meshComponent.mesh = meshBuilder.build();
        meshComponent.material = Assets.getMaterial("ComputerMonitors:ComputerMonitor");
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

    private EntityRef createScreenForegroundRenderingEntity(Vector3f location, Vector3i monitorSize, Side front, int lineLength, int lineCount, List<String> lines) {
        Font font = Assets.getFont("ModularComputers:November");

        Material material = font.getCharacterData(' ').getPageMat();


        Mesh mesh = createMeshForMonitor(monitorSize, front, lineLength, lineCount, lines, font);

        MeshComponent meshComponent = new MeshComponent();
        meshComponent.mesh = mesh;
        meshComponent.material = Assets.getMaterial("ComputerMonitors:ComputerText");
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

    private Mesh createMeshForMonitor(Vector3i monitorSize, Side front, int lineLength, int lineCount, List<String> lines, Font font) {
        BlockShape blockShape = (BlockShape) Assets.get(AssetType.SHAPE, "engine:cube");
        BlockMeshPart meshPart = blockShape.getMeshPart(BlockPart.fromSide(front));

        float xMult = 1f / lineLength;
        float yMult = 1f / lineCount;

        Vector3f topLeft = getTopLeft(monitorSize, meshPart);
        Vector3f topRight = getTopRight(monitorSize, meshPart);
        Vector3f bottomLeft = getBottomLeft(monitorSize, meshPart);
        Vector3f bottomRight = getBottomRight(monitorSize, meshPart);

        MeshBuilder meshBuilder = new MeshBuilder();
        int lineNo = 0;
        for (String line : lines) {
            if (line != null) {
                float yAdvance = yMult * lineNo;

                char[] chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    FontCharacter character = font.getCharacterData(chars[i]);

                    float cH = character.getHeight() * yMult / fontHeight;
                    float xAdvance = xMult * i;

                    Vector3f charTopLeft = convertToMonitorCoords(new Vector2f(xAdvance, yAdvance + (yMult - cH)), topLeft, topRight, bottomLeft, bottomRight);
                    Vector3f charTopRight = convertToMonitorCoords(new Vector2f(xAdvance + xMult, yAdvance + (yMult - cH)), topLeft, topRight, bottomLeft, bottomRight);
                    Vector3f charBottomRight = convertToMonitorCoords(new Vector2f(xAdvance + xMult, yAdvance + yMult), topLeft, topRight, bottomLeft, bottomRight);
                    Vector3f charBottomLeft = convertToMonitorCoords(new Vector2f(xAdvance, yAdvance + yMult), topLeft, topRight, bottomLeft, bottomRight);

                    meshBuilder.addPoly(charTopLeft, charTopRight, charBottomRight, charBottomLeft);
                    meshBuilder.addColor(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
                    float texTop = character.getY();
                    float texBottom = texTop + character.getTexHeight();
                    float texLeft = character.getX();
                    float texRight = texLeft + character.getTexWidth();
                    meshBuilder.addTexCoord(texLeft, texTop);
                    meshBuilder.addTexCoord(texRight, texTop);
                    meshBuilder.addTexCoord(texRight, texBottom);
                    meshBuilder.addTexCoord(texLeft, texBottom);
                }
            }
            lineNo++;
        }
        return meshBuilder.build();
    }

    private Vector3f convertToMonitorCoords(Vector2f planeCoords, Vector3f bottomRight, Vector3f bottomLeft, Vector3f topRight, Vector3f topLeft) {
        float xMult = planeCoords.x;
        float yMult = planeCoords.y;

        Vector3f left = fintPointBetween(topLeft, bottomLeft, yMult);
        Vector3f right = fintPointBetween(topRight, bottomRight, yMult);

        return fintPointBetween(left, right, xMult);
    }

    private Vector3f fintPointBetween(Vector3f from, Vector3f to, float yMult) {
        return new Vector3f(
                from.x + yMult * (to.x - from.x),
                from.y + yMult * (to.y - from.y),
                from.z + yMult * (to.z - from.z));
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

        meshBuilder.addPoly(
                getTopLeft(monitorSize, meshPart),
                getTopRight(monitorSize, meshPart),
                getBottomRight(monitorSize, meshPart),
                getBottomLeft(monitorSize, meshPart));
        meshBuilder.addColor(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
        meshBuilder.addTexCoord(0, 0);
        meshBuilder.addTexCoord(0, 0.0625f);
        meshBuilder.addTexCoord(0.0625f, 0.0625f);
        meshBuilder.addTexCoord(0.0625f, 0);
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
        renderComponent.screenBackground.getComponent(MeshComponent.class).mesh.dispose();
        renderComponent.screenBackground.destroy();
        renderComponent.screenForeground.getComponent(MeshComponent.class).mesh.dispose();
        renderComponent.screenForeground.destroy();
        monitorEntity.removeComponent(ComputerRenderComponent.class);
    }
}
