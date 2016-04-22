package com.base.engine.components;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.GameObject;
import com.base.engine.core.Input;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Transform;
import com.base.engine.rendering.Shader;

public abstract class GameComponent {
	private GameObject parent;
	private CoreEngine engine;
	
	public void input(Input input){
		
	}
	
	public void update(double delta){
		
	}
	
	public void render(Shader shader, RenderingEngine renderingEngine){
		
	}
	
	public void setParent(GameObject parent){
		this.parent = parent;
	}
	
	public Transform getTransform(){
		return parent.getTransform();
	}
	
	public void addToEngine(CoreEngine engine){}

	public void setEngine(CoreEngine engine) {
		this.engine = engine;
	}
}
