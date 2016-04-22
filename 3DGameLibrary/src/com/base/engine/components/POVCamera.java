package com.base.engine.components;

import com.base.engine.core.GameObject;
import com.base.engine.core.Input;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Shader;

public class POVCamera extends GameObject {
	Camera camera;
	public POVCamera(float fov, float aspectRatio, float zNear, float zFar) {
		camera = new Camera(fov, aspectRatio, zNear, zFar);
		addComponent(camera);
	}

	private boolean thirdPerson = false;
	private final float xOffset = -5;
	private final double angle = 60;
	private final float yOffset = (float) -(xOffset * Math.sin(Math.toRadians(90d - angle))/Math.sin(Math.toRadians(angle)));
	@Override
	public void input(Input input) {
		super.input(input);
		if (input.getKeyPress(Input.GLFW_KEY_F5)){
			thirdPerson = !thirdPerson;
			if(thirdPerson){
				move(getTransform().getRot().getLeft().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(90)), xOffset);
				move(new Vector3f(0, 1, 0), yOffset);
				getTransform().rotate(getTransform().getRot().getLeft().normalized(), (float) Math.toRadians(-angle));
			}else{
				move(getTransform().getRot().getLeft().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(90)), -xOffset);
				move(new Vector3f(0, 1, 0), -yOffset);
				getTransform().rotate(getTransform().getRot().getLeft().normalized(), (float) Math.toRadians(angle));
//				getTransform().rotate(getTransform().getRot().getLeft().normalized(), (float) Math.toRadians(angle));
				//				move(getTransform().getRot().getLeft().rotate(new Vector3f(0, 0, 1), (float) Math.toRadians(90)), 5);
			}
		}
	}

	public void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}
//	@Override
//	public void render(Shader shader, RenderingEngine renderingEngine) {
//		if (thirdPerson)
//	}
}
