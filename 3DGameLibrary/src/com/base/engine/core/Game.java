package com.base.engine.core;

public abstract class Game {
	private GameObject root;
	
	public void init(){
		
	}
	public void input(Input input){
		getRoot().inputAll(input);
	}
	public void update(CoreEngine engine){
		getRoot().updateAll(engine);
	}
	
	public void render(RenderingEngine renderingEngine){
		renderingEngine.render(getRoot());
	}
	
	public void addObject (GameObject object){
		getRoot().addChild(object);
	}
	
	private GameObject getRoot() {
		if(root == null)
			root = new GameObject();
		return root;
	}

	public void setEngine(CoreEngine engine) {
		getRoot().setEngine(engine);
	}
}
