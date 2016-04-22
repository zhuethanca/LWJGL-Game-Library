package com.base.engine.core;

import java.util.ArrayList;

import com.base.engine.components.GameComponent;
import com.base.engine.rendering.Shader;

public class GameObject {
	private ArrayList<GameObject> children;
	private ArrayList<GameComponent> components;
	private Transform transform;
	private CoreEngine engine;

	public GameObject() {
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
		transform = new Transform();
		engine = null;
	}
	
	public GameObject addChild(GameObject child){
		children.add(child);
		child.setEngine(engine);
		child.getTransform().setParent(transform);
		return this;
	}
	
	public GameObject addComponent(GameComponent component){
		components.add(component);
		component.setParent(this);
		return this;
	}
	
	public void inputAll(Input input){
		input(input);
		for(GameObject child : children)
			child.inputAll(input);
	}
	
	public void updateAll(CoreEngine engine){
		update(engine);
		for (GameObject child : children)
			child.updateAll(engine);
	}
	
	public void renderAll(Shader shader, RenderingEngine renderingEngine){
		render(shader, renderingEngine);
		for (GameComponent gameComponent : components)
			gameComponent.render(shader, renderingEngine);
		for (GameObject child : children)
			child.renderAll(shader, renderingEngine);
	}
	
	public void input(Input input){
		transform.update();
		for (GameComponent gameComponent : components)
			gameComponent.input(input);
	}
	
	public void update(CoreEngine engine){
		for (GameComponent gameComponent : components)
			gameComponent.update(engine.input.getDelta());
	}
	
	public void render(Shader shader, RenderingEngine renderingEngine){
		for (GameComponent gameComponent : components)
			gameComponent.render(shader, renderingEngine);
	}
	
	public ArrayList<GameObject> getAllAttached(){
		ArrayList<GameObject> result = new ArrayList<>();
		for(GameObject child:children)
			result.addAll(child.getAllAttached());
		result.add(this);
		return result;
	}
	
	public void setTransform(Transform transform){
		this.transform = transform;
	}
	
	public Transform getTransform(){
		return transform;
	}

	public CoreEngine getEngine() {
		return engine;
	}

	public void setEngine(CoreEngine engine) {
		if(this.engine != engine){
			this.engine = engine;
			for (GameComponent gameComponent : components){
				gameComponent.setEngine(engine);
				gameComponent.addToEngine(engine);
			}
			for (GameObject child : children)
				child.setEngine(engine);
		}
	}
}
