// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.computer.monitor.module;

import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;

public class ColorUtils {
    private ColorUtils() {
    }

    public static int[] parseColor(int line, String color) throws ExecutionException {
        int[] result = new int[4];

        if (color.length() != 6 && color.length() != 8) {
            throw new ExecutionException(line, "Invalid definition of color");
        }

        try {
            result[0] = Integer.parseInt(color.substring(0, 2), 16);
            result[1] = Integer.parseInt(color.substring(2, 4), 16);
            result[2] = Integer.parseInt(color.substring(4, 6), 16);
            if (color.length() == 8) {
                result[3] = Integer.parseInt(color.substring(6, 8), 16);
            } else {
                result[3] = 255;
            }
        } catch (NumberFormatException exp) {
            throw new ExecutionException(line, "Invalid definition of color.");
        }

        return result;
    }
}
