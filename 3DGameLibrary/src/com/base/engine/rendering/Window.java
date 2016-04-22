package com.base.engine.rendering;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_TRUE;

import java.util.HashMap;

import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Vector2f;

public class Window {
	private static HashMap<Long, Window> windows = new HashMap<>();
	private String windowTitle;
	private long window;
	private int windowWidth, windowHeight;
	private int bufferWidth, bufferHeight;
	@SuppressWarnings("unused")
	private static GLFWFramebufferSizeCallback framebufferSizeCallback;
	@SuppressWarnings("unused")
	private static GLFWWindowSizeCallback windowSizeCallback;
	
	private Vector2f center;
	
	private CoreEngine main;
    private Window(int width, int height, String title, long window, CoreEngine main){
    	windowWidth = width;
		windowHeight = height;
		windowTitle = title;
		this.window = window;
		this.main = main;
		center = new Vector2f(getWidth()/2, getHeight()/2);
    }
    
	public static Window createWindow(int width, int height, String title, CoreEngine main){
		long window;
		glfwInit();
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 4);
//		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); 
		window = glfwCreateWindow(width, height, title, 0, 0);
		Window windowObj = new Window(width, height, title, window, main);
		windows.put(window, windowObj);
        glfwSetFramebufferSizeCallback(window, (framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                windows.get(window).updateBufferSize(width, height);
            }
        }));
		glfwSetWindowSizeCallback(window, (windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long window, int width, int height) {
                windows.get(window).updateWindowSize(width, height);
            }
        }));
		glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
		return windowObj;
	}
	
	public void dispose(){
        glfwDestroyWindow(window);   
	}
	
	private void updateBufferSize(int width, int height){
		bufferWidth = width;
		bufferHeight = height;
		main.width = width;
		main.height = height;
	}
	
	private void updateWindowSize(int width, int height){
		windowWidth = width;
		windowHeight = height;
		center.setX(width/2);
		center.setY(height/2);
	}
	
	public void render(){
		glfwPollEvents();
		glfwSwapBuffers(window);
	}
	
	public boolean isCloseRequested(){
		return glfwWindowShouldClose(window) == GL_TRUE;
	}
	
	public int getWidth(){
		return windowWidth;
	}
	
	public int getHeight(){
		return windowHeight;
	}
	
	public String getTitle(){
		return windowTitle;
	}

	public long getWindow() {
		return window;
	}

	public CoreEngine getMain() {
		return main;
	}

	public int getBufferWidth() {
		return bufferWidth;
	}

	public void setBufferWidth(int bufferWidth) {
		this.bufferWidth = bufferWidth;
	}

	public int getBufferHeight() {
		return bufferHeight;
	}

	public void setBufferHeight(int bufferHeight) {
		this.bufferHeight = bufferHeight;
	}
	
	public Vector2f getCenter(){
		return center;
	}
}
