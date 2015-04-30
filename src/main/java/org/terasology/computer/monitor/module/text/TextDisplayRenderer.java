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
package org.terasology.computer.monitor.module.text;

import org.terasology.asset.AssetType;
import org.terasology.asset.Assets;
import org.terasology.computer.display.system.client.DisplayRenderer;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.assets.font.FontCharacter;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.assets.material.MaterialData;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.assets.texture.TextureData;
import org.terasology.rendering.assets.texture.TextureUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class TextDisplayRenderer implements DisplayRenderer {
    private Font font = Assets.getFont("ModularComputers:November");
    private BufferedImage fontImage = TextureUtil.convertToImage(font.getCharacterData(' ').getPage());
    private int fontImageWidth = fontImage.getWidth();
    private int fontImageHeight = fontImage.getHeight();

    private int characterWidth = 8;
    private int characterHeight = 16;

    @Override
    public Material renderMaterial(String mode, List<String> data) {
        String[] size = mode.split(":")[1].split(",");
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);

        int pixelWidth = width * characterWidth;
        int pixelHeight = height * characterHeight;

        BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = (Graphics2D) image.getGraphics();
        try {
//            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            gr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//            gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            gr.setColor(Color.BLACK);
            gr.fillRect(0, 0, pixelWidth, pixelHeight);

            int lineCount = data.size();
            for (int y = 0; y < lineCount; y++) {
                String line = data.get(y);
                char[] chars = line.toCharArray();
                for (int x = 0; x < chars.length; x++) {
                    FontCharacter character = font.getCharacterData(chars[x]);

                    if (character != null) {
                        int top = y * characterHeight + character.getyOffset();
                        int bottom = top + character.getHeight();
                        int left = x * characterWidth + character.getxOffset();
                        int right = left + character.getWidth();

                        int texTop = Math.round(fontImageHeight * character.getY());
                        int texBottom = texTop + Math.round(fontImageHeight * character.getTexHeight());
                        int texLeft = Math.round(fontImageWidth * character.getX());
                        int texRight = texLeft + Math.round(fontImageWidth * character.getTexWidth());

                        gr.drawImage(fontImage, left, top, right, bottom, texLeft, texTop, texRight, texBottom, null);
                    }
                }
            }
        } finally {
            gr.dispose();
        }

        ByteBuffer resultBuffer = TextureUtil.convertToByteBuffer(image);

        Texture texture = Assets.generateAsset(AssetType.TEXTURE, new TextureData(pixelWidth, pixelHeight, new ByteBuffer[]{resultBuffer}, Texture.WrapMode.REPEAT, Texture.FilterMode.NEAREST), Texture.class);

        MaterialData terrainMatData = new MaterialData(Assets.getShader("engine:genericMeshMaterial"));
        terrainMatData.setParam("diffuse", texture);
        terrainMatData.setParam("colorOffset", new float[]{1, 1, 1});
        terrainMatData.setParam("textured", true);
        return Assets.generateAsset(AssetType.MATERIAL, terrainMatData, Material.class);
    }
}
