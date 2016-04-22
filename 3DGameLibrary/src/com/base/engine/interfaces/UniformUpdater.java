package com.base.engine.interfaces;

import com.base.engine.core.Matrix4f;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Transform;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Shader;

public interface UniformUpdater {
	public void updateUniform(String uniformName, String uniformType, Transform transform, Material material,Matrix4f worldMatrix,Matrix4f MVPMatrix, Shader shader, RenderingEngine renderingEngine);
}
