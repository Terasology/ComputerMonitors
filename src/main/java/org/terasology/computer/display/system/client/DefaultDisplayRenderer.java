// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.display.system.client;

import org.terasology.engine.rendering.assets.material.Material;
import org.terasology.engine.rendering.assets.material.MaterialData;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureData;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.engine.utilities.Assets;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class DefaultDisplayRenderer implements DisplayRenderer {
    @Override
    public Material renderMaterial(String mode, List<String> data) {
        int width = 1;
        int height = 1;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = (Graphics2D) image.getGraphics();
        try {
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            gr.setColor(Color.BLACK);
            gr.fillRect(0, 0, width, height);
        } finally {
            gr.dispose();
        }

        ByteBuffer resultBuffer = TextureUtil.convertToByteBuffer(image);

        Texture texture = Assets.generateAsset(new TextureData(width, height, new ByteBuffer[]{resultBuffer},
                Texture.WrapMode.REPEAT, Texture.FilterMode.NEAREST), Texture.class);

        MaterialData terrainMatData = new MaterialData(Assets.getShader("engine:genericMeshMaterial").get());
        terrainMatData.setParam("diffuse", texture);
        terrainMatData.setParam("colorOffset", new float[]{1, 1, 1});
        terrainMatData.setParam("textured", true);
        return Assets.generateAsset(terrainMatData, Material.class);
    }
}
