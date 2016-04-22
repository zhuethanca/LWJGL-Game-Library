package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;

public class FreeLook extends GameComponent {
	public static final Vector3f yAxis = new Vector3f(0, 1, 0);
	boolean mouseLocked = false;
	Vector2f centerPosition;

	@Override
	public void input(Input input) {
		centerPosition = new Vector2f(input.getWindow().getWidth() / 2, input.getWindow().getHeight() / 2);
		float sensitivity = 1.0f;
		// float rotAmt = (float)(100 * Time.getDelta());
//		System.out.println(input.getWindow().getWindow());
//		System.out.println(input.getKeyDown(Input.GLFW_KEY_ESCAPE));
		if (input.getKeyDown(Input.GLFW_KEY_ESCAPE)) {
			input.setCursor(true);
			mouseLocked = false;
		}
		if (input.getMouseDown(0)) {
			input.setMousePosition(centerPosition);
			input.setCursor(false);
			mouseLocked = true;
		}

		if (mouseLocked) {
			Vector2f deltaPos = input.getMousePos().sub(centerPosition);

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if (rotY)
				getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if (rotX)
				getTransform().rotate(getTransform().getRot().getRight(), (float) Math.toRadians(deltaPos.getY() * sensitivity));

			if (rotY || rotX)
				input.setMousePosition(centerPosition);
		}

		// if(Input.getKey(Input.KEY_UP))
		// rotateX(-rotAmt);
		// if(Input.getKey(Input.KEY_DOWN))
		// rotateX(rotAmt);
		// if(Input.getKey(Input.KEY_LEFT))
		// rotateY(-rotAmt);
		// if(Input.getKey(Input.KEY_RIGHT))
		// rotateY(rotAmt);
		// PhongShader.setDirectionalLight(new DirectionalLight(new
		// BaseLight(new Vector3f(1f, 1f, 1f), 0.8f), new
		// Vector3f(forward.getX(), -forward.getY(), -forward.getZ())));
	}

}
