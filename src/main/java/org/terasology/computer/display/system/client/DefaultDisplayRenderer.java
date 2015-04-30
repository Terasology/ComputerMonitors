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
package org.terasology.computer.display.system.client;

import org.terasology.asset.AssetType;
import org.terasology.asset.Assets;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.assets.material.MaterialData;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.assets.texture.TextureData;
import org.terasology.rendering.assets.texture.TextureUtil;

import java.awt.*;
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
            gr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            gr.setColor(Color.BLACK);
            gr.fillRect(0, 0, width, height);
        } finally {
            gr.dispose();
        }

        ByteBuffer resultBuffer = TextureUtil.convertToByteBuffer(image);

        Texture texture = Assets.generateAsset(AssetType.TEXTURE, new TextureData(width, height, new ByteBuffer[]{resultBuffer}, Texture.WrapMode.REPEAT, Texture.FilterMode.NEAREST), Texture.class);

        MaterialData terrainMatData = new MaterialData(Assets.getShader("engine:genericMeshMaterial"));
        terrainMatData.setParam("diffuse", texture);
        terrainMatData.setParam("colorOffset", new float[]{1, 1, 1});
        terrainMatData.setParam("textured", true);
        return Assets.generateAsset(AssetType.MATERIAL, terrainMatData, Material.class);
    }
}
