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

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.ModuleMethodExecutable;
import org.terasology.math.Vector2i;

import java.util.Map;

public class GraphicsRenderBuffer implements ModuleMethodExecutable<Object> {
    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public int getMinimumExecutionTime(int line, ComputerCallback computer, Map<String, Variable> parameters) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", "drawText");
        return renderCommandSink.isInstantRendering() ? 0 : 300;
    }

    @Override
    public String[] getParameterNames() {
        return new String[]{"renderBinding", "offScreenBuffer"};
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        GraphicsRenderCommandSink renderCommandSink = GraphicsRenderBindingValidator.validateGraphicsRenderBinding(line, computer, parameters, "renderBinding", "setCharacters");
        GraphicsBuffer graphicsBuffer = GraphicsRenderBindingValidator.validateGraphicsBuffer(line, parameters, "offScreenBuffer", "renderBuffer");

        Vector2i size = graphicsBuffer.getResolution();
        Vector2i maxCharacters = renderCommandSink.getResolution();

        if (size.x>maxCharacters.x || size.y>maxCharacters.y)
            throw new ExecutionException(line, "OffScreenBuffer does not fit on the screen");

        renderCommandSink.setData(line, graphicsBuffer.getData());

        return null;
    }
}
