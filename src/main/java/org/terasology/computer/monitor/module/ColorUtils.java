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
package org.terasology.computer.monitor.module;

import com.gempukku.lang.ExecutionException;

public class ColorUtils {
    private ColorUtils() { }

    public static int[] parseColor(int line, String color) throws ExecutionException {
        int[] result = new int[4];

        if (color.length()!= 6 && color.length() != 8) {
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
