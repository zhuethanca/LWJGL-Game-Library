package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.Vector3f;

public class Camera extends GameComponent{
	private Matrix4f projection;
	
	public Camera(float fov, float aspectRatio, float zNear, float zFar) {
		this.projection = new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar);
	}
	
	public Matrix4f getViewProjection(){
		Matrix4f cameraRotation = getTransform().getTransformedRot().conjugate().toRotationMatrix();
		Vector3f cameraPos = getTransform().getTransformedPos().mul(-1);
		Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	@Override
	public void addToEngine(CoreEngine engine){
		engine.getRenderingEngine().addCamera(this);
	}
}
