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
package org.terasology.computer.display.component;

import org.joml.Vector3i;
import org.terasology.entitySystem.Component;
import org.terasology.math.Side;
import org.terasology.network.Replicate;

import java.util.List;

@Replicate
public class DisplayComponent implements Component {
    private Vector3i monitorSize;
    private Side front;
    private String mode;
    private List<String> data;

    public DisplayComponent() {
    }

    public DisplayComponent(Vector3i monitorSize, Side front, String mode, List<String> data) {
        this.monitorSize = monitorSize;
        this.front = front;
        this.mode = mode;
        this.data = data;
    }

    public Vector3i getMonitorSize() {
        return monitorSize;
    }

    public Side getFront() {
        return front;
    }

    public String getMode() {
        return mode;
    }

    public List<String> getData() {
        return data;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
