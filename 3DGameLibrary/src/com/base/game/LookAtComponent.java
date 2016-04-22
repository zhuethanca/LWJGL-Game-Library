package com.base.game;

import com.base.engine.components.GameComponent;
import com.base.engine.core.Quaternion;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Shader;

public class LookAtComponent extends GameComponent
{
	RenderingEngine renderingEngine;

	@Override
	public void update(double delta)
	{
		super.update(delta);
		if(renderingEngine != null)
		{
			Quaternion newRot = getTransform().getLookAtDirection(renderingEngine.getMainCamera().getTransform().getTransformedPos(), new Vector3f(0,1,0));
					//getTransform().getRot().getUp());

//			getTransform().setRot(getTransform().getRot().nlerp(newRot, (float) (delta * 5.0d), true));
			getTransform().setRot(getTransform().getRot().slerp(newRot, (float) (delta * 5.0d), false));
		}
	}

	@Override
	public void render(Shader shader, RenderingEngine renderingEngine)
	{
		super.render(shader, renderingEngine);
		this.renderingEngine = renderingEngine;
	}
}