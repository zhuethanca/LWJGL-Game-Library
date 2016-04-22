//package com.base.engine.GUI;
//
//import java.util.ArrayList;
//import java.util.PriorityQueue;
//
//import com.base.engine.core.CoreEngine;
//import com.base.engine.core.GameObject;
//import com.base.engine.core.Input;
//import com.base.engine.core.RenderingEngine;
//import com.base.engine.rendering.Shader;
//import static org.lwjgl.opengl.GL11.*;
//
//public class GUIManager extends GameObject {
//	private PriorityQueue<Icon> drawQueue = new PriorityQueue<>();
//	private ArrayList<GUI> guis = new ArrayList<>();
//
//	@Override
//	public void input(Input input) {
//		for(GUI gui : guis)
//			gui.input(input);
//	}
//
//	@Override
//	public void update(CoreEngine engine) {
//		for(GUI gui : guis)
//			gui.update(engine.input.getDelta());
//	}
//
//	@Override
//	public void render(Shader shader, RenderingEngine renderingEngine) {
//		for(GUI gui : guis)
//			gui.render();
//		while(!drawQueue.isEmpty())
//			draw(drawQueue.poll());
//	}
//	
//	private void draw(Icon icon){
//
//	}
//	
//	public void addIcon(Icon icon){
//		drawQueue.add(icon);
//	}
//
//	public void addGUI(GUI gui) {
//		gui.setManager(this);
//		guis.add(gui);
//	}
//}
