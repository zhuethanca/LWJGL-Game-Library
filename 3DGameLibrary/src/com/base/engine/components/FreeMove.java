package com.base.engine.components;

import com.base.engine.core.Input;
import com.base.engine.core.Vector3f;

public class FreeMove extends GameComponent {
	private boolean yLook;
	private float speed = 0;
	private int forwardKey, backKey, leftKey, rightKey, upKey, downKey;

	public FreeMove(boolean yLook, float speed, int forwardKey, int backKey, int leftKey, int rightKey, int upKey, int downKey) {
		this.yLook = yLook;
		this.speed = speed;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.upKey = upKey;
		this.downKey = downKey;
	}

	public FreeMove() {
		this(false, 10f);
	}

	public FreeMove(boolean yLook, float speed) {
		this(yLook, speed, Input.GLFW_KEY_W, Input.GLFW_KEY_S, Input.GLFW_KEY_A, Input.GLFW_KEY_D, Input.GLFW_KEY_F, Input.GLFW_KEY_SPACE);
	}

	@Override
	public void input(Input input) {
		float movAmt = (float) (speed * input.getDelta());
		if (input.getKeyDown(Input.GLFW_KEY_LEFT_SHIFT))
			movAmt *= 2;
		if (yLook) {
			if (input.getKeyDown(forwardKey))
				move(getTransform().getRot().getForward(), movAmt);
			if (input.getKeyDown(backKey))
				move(getTransform().getRot().getForward(), -movAmt);
		} else {
			if (input.getKeyDown(Input.GLFW_KEY_W))
				move(getTransform().getRot().getLeft().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(90)), movAmt);
			if (input.getKeyDown(Input.GLFW_KEY_S))
				move(getTransform().getRot().getLeft().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(-90)), movAmt);
		}
		if (input.getKeyDown(leftKey))
			move(getTransform().getRot().getLeft(), movAmt);
		if (input.getKeyDown(rightKey))
			move(getTransform().getRot().getRight(), movAmt);
		if (input.getKeyDown(upKey))
			move(new Vector3f(0, -1, 0), movAmt);
		if (input.getKeyDown(downKey))
			move(new Vector3f(0, 1, 0), movAmt);
	}

	public void move(Vector3f dir, float amt) {
		getTransform().setPos(getTransform().getPos().add(dir.mul(amt)));
	}
}
