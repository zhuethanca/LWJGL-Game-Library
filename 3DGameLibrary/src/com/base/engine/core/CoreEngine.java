package com.base.engine.core;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import com.base.engine.rendering.Window;

public class CoreEngine {
	public int width, height;
	public final String title;
	
    @SuppressWarnings("unused")
	private GLFWErrorCallback errorCallback;
	private boolean isRunning;
	
	public Input input;
	
	private Game game;
	private RenderingEngine renderingEngine;
	
	private float frameRate, frameTime;
	
	private Window window;
	
	public CoreEngine(Game game){
		this(800, 600, 60, "3D Game Engine", game);
	}
	
	public CoreEngine(int width, int height, float frameRate, String title, Game game){
		this.width = width;
		this.height = height;
		this.title = title;
		this.game = game;
		this.frameRate = frameRate;
		init();
	}
	private void init(){
		isRunning = false;
		initGraphics();
		input = Input.createInput(window);
		System.out.println(RenderingEngine.getOpenGLVersion());
		game.setEngine(this);
	}
	
	private void initGraphics(){
		glfwInit();
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		window = Window.createWindow(width, height, title, this);
		GL.createCapabilities();
		this.renderingEngine = new RenderingEngine(this);
	}
	
	public void start(){
		if(isRunning)
			return;
		isRunning = true;
		run();
	}
	
	public void stop(){
		if(!isRunning)
			return;
		isRunning = false;
	}
	
	private void run(){
		frameTime = 1.0f / frameRate;
		int frames = 0;
		double frameCounter = 0;

		game.init();

		double lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while(isRunning)
		{
			boolean render = false;
			
			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime)
			{
				render = true;
				
				unprocessedTime -= frameTime;
				
				if(window.isCloseRequested())
					stop();
				input.setDelta(frameTime);
				update();
				if(frameCounter >= 1)
				{
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if(render)
			{
				render();
				//System.out.println(1000 * (double)(Time.getTime() - startTime)/((double)Time.SECOND) + "ms");
				frames++;
			}
			else
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		cleanUp();
	}

	private void update() {
		game.input(input);
		game.update(this);
		input.clearKeys();
	}
	
	private void render(){
		game.render(renderingEngine);
		//game.render();
		window.render();
	}
	
	private void cleanUp(){
		window.dispose();
		glfwTerminate();
	}

	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}

}
