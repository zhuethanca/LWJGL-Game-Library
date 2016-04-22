package com.base.engine.components;

import com.base.engine.core.RenderingEngine;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Shader;

//public class MeshRenderer2D extends GameComponent{
//	private Mesh mesh;
//	private Material material;
//	private Camera camera;
//	
//	public MeshRenderer2D(Mesh mesh, Material material, Camera camera){
//		this.mesh = mesh;
//		this.material = material;
//		this.camera = camera;
//	}
//	
//	@Override
//	public void render(Shader shader, RenderingEngine renderingEngine) {
//		shader.bind();
//		shader.updateUniforms(getTransform(), material, renderingEngine, camera);
//		mesh.draw();
//	}
//}
