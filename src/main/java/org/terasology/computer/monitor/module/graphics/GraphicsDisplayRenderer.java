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
package org.terasology.computer.monitor.module.graphics;

import org.terasology.asset.AssetType;
import org.terasology.asset.Assets;
import org.terasology.computer.display.system.client.DisplayRenderer;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.assets.material.MaterialData;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.assets.texture.TextureData;
import org.terasology.rendering.assets.texture.TextureUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

public class GraphicsDisplayRenderer implements DisplayRenderer {
    @Override
    public Material renderMaterial(String mode, List<String> data) {
        String[] size = mode.split(":")[1].split(",");
        int width = Integer.parseInt(size[0]);
        int height = Integer.parseInt(size[1]);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = (Graphics2D) image.getGraphics();
        try {
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            gr.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            gr.setColor(Color.BLACK);
            gr.fillRect(0, 0, width, height);

            if (data != null) {
                for (String renderable : data) {
                    if (renderable.startsWith("text:")) {
                        renderText(gr, renderable.substring(5));
                    } else if (renderable.startsWith("drawRect:")) {
                        renderDrawRect(gr, renderable.substring(9));
                    } else if (renderable.startsWith("drawRoundRect:")) {
                        renderDrawRoundRect(gr, renderable.substring(14));
                    } else if (renderable.startsWith("drawOval:")) {
                        renderDrawOval(gr, renderable.substring(9));
                    } else if (renderable.startsWith("drawLine:")) {
                        renderDrawLine(gr, renderable.substring(9));
                    }
                }
            }
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

    private void renderText(Graphics2D gr, String renderable) {
        String[] split = renderable.split(":", 6);
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        String paintStr = split[2];
        String font = split[3];
        int fontSize = Integer.parseInt(split[4]);
        String text = split[5];

        gr.setPaint(createPaint(paintStr));
        gr.setFont(new Font(font, Font.PLAIN, fontSize));
        gr.drawString(text, x, y);
    }

    private void renderDrawRect(Graphics2D gr, String renderable) {
        String[] split = renderable.split(":", 6);
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        int width = Integer.parseInt(split[2]);
        int height = Integer.parseInt(split[3]);
        String paintStr = split[4];
        boolean fill = Boolean.parseBoolean(split[5]);

        gr.setStroke(new BasicStroke(1));
        gr.setPaint(createPaint(paintStr));
        if (fill) {
            gr.fillRect(x, y, width, height);
        } else {
            gr.drawRect(x, y, width, height);
        }
    }

    private void renderDrawLine(Graphics2D gr, String renderable) {
        String[] split = renderable.split(":", 6);
        int x1 = Integer.parseInt(split[0]);
        int y1 = Integer.parseInt(split[1]);
        int x2 = Integer.parseInt(split[2]);
        int y2 = Integer.parseInt(split[3]);
        String paintStr = split[4];
        int lineWidth = Integer.parseInt(split[5]);

        gr.setStroke(new BasicStroke(lineWidth));
        gr.setPaint(createPaint(paintStr));
        gr.drawLine(x1, y1, x2, y2);
    }

    private void renderDrawOval(Graphics2D gr, String renderable) {
        String[] split = renderable.split(":", 6);
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        int width = Integer.parseInt(split[2]);
        int height = Integer.parseInt(split[3]);
        String paintStr = split[4];
        boolean fill = Boolean.parseBoolean(split[5]);

        gr.setStroke(new BasicStroke(1));
        gr.setPaint(createPaint(paintStr));
        if (fill) {
            gr.fillOval(x, y, width, height);
        } else {
            gr.drawOval(x, y, width, height);
        }
    }

    private void renderDrawRoundRect(Graphics2D gr, String renderable) {
        String[] split = renderable.split(":", 8);
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        int width = Integer.parseInt(split[2]);
        int height = Integer.parseInt(split[3]);
        int arcWidth = Integer.parseInt(split[4]);
        int arcHeight = Integer.parseInt(split[5]);
        String paintStr = split[6];
        boolean fill = Boolean.parseBoolean(split[7]);

        gr.setStroke(new BasicStroke(1));
        gr.setPaint(createPaint(paintStr));
        if (fill) {
            gr.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
        } else {
            gr.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        }
    }

    private Paint createPaint(String paintStr) {
        if (paintStr.startsWith("color(")) {
            String[] split = paintStr.substring(6, paintStr.length() - 1).split(",");
            int r = Integer.parseInt(split[0]);
            int g = Integer.parseInt(split[1]);
            int b = Integer.parseInt(split[2]);
            int a = Integer.parseInt(split[3]);

            return new Color(r, g, b, a);
        } else if (paintStr.startsWith("gradient(")) {
            String[] split = paintStr.substring(9, paintStr.length() - 1).split(",");
            int r1 = Integer.parseInt(split[0]);
            int g1 = Integer.parseInt(split[1]);
            int b1 = Integer.parseInt(split[2]);
            int a1 = Integer.parseInt(split[3]);
            int x1 = Integer.parseInt(split[4]);
            int y1 = Integer.parseInt(split[5]);
            int r2 = Integer.parseInt(split[6]);
            int g2 = Integer.parseInt(split[7]);
            int b2 = Integer.parseInt(split[8]);
            int a2 = Integer.parseInt(split[9]);
            int x2 = Integer.parseInt(split[10]);
            int y2 = Integer.parseInt(split[11]);
            boolean cyclic = Boolean.parseBoolean(split[12]);

            return new GradientPaint(x1, y1, new Color(r1, g1, b1, a1), x2, y2, new Color(r2, g2, b2, a2), cyclic);
        }
        return null;
    }
}
