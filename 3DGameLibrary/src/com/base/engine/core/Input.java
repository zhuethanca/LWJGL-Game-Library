package com.base.engine.core;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import com.base.engine.rendering.Window;

public class Input {
	// private static final int NUM_KEYCODES = 256;
	@SuppressWarnings("unused")
	private static GLFWKeyCallback keyCallback;
	@SuppressWarnings("unused")
	private static GLFWMouseButtonCallback mouseButtonCallback;
	@SuppressWarnings("unused")
   private static GLFWCursorPosCallback cursorPosCallback;
	
	private static HashMap<Long, Input> inputs = new HashMap<>();

	private Vector2f mousePos = new Vector2f(0f, 0f);

	private Set<Integer> pressedKeys = Collections.synchronizedSet(new HashSet<Integer>());
	private Set<Integer> releasedKeys = Collections.synchronizedSet(new HashSet<Integer>());
	private Set<Integer> currentKeys = Collections.synchronizedSet(new HashSet<Integer>());

	private Set<Integer> pressedMouse = Collections.synchronizedSet(new HashSet<Integer>());
	private Set<Integer> releasedMouse = Collections.synchronizedSet(new HashSet<Integer>());
	private Set<Integer> currentMouse = Collections.synchronizedSet(new HashSet<Integer>());

	private Window window;
	
	private double delta = 0d;
	private Input(Window window){
		this.window = window;
	}
	public static Input createInput(Window window) {
		if (window == null) {
			NullPointerException e = new NullPointerException();
			e.printStackTrace();
			System.exit(1);
		}
		Input input = new Input(window);
		inputs.put(window.getWindow(), input);
		glfwSetKeyCallback(window.getWindow(), (keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				inputs.get(window).updateKey(key, action);
			}
		}));
		glfwSetMouseButtonCallback(window.getWindow(), (mouseButtonCallback = new GLFWMouseButtonCallback() {
			@Override
			public void invoke(long window, int button, int action, int mods) {
				inputs.get(window).updateMouse(button, action);
			}
		}));
		glfwSetCursorPosCallback(window.getWindow(), (cursorPosCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				inputs.get(window).updateMousePos(xpos, ypos);
			}
		}));
		return input;
	}

	
	
	public void clearKeys() {
		pressedKeys.clear();
		releasedKeys.clear();
		pressedMouse.clear();
		releasedMouse.clear();
	}
	
	private  void updateMousePos(double x, double y){
		mousePos.setX((float)x);
		mousePos.setY((float)y);
	}
	
	public Vector2f getMousePos(){
		return mousePos;
	}

	private void updateKey(int key, int action) {
		if (action == GLFW_PRESS) {
			pressedKeys.add(key);
			currentKeys.add(key);
		} else if (action == GLFW_RELEASE) {
			releasedKeys.add(key);
			currentKeys.remove(new Integer(key));
		}
	}

	private void updateMouse(int key, int action) {
		if (action == GLFW_PRESS) {
			pressedMouse.add(key);
			currentMouse.add(key);
		} else if (action == GLFW_RELEASE) {
			releasedMouse.add(key);
			currentMouse.remove(new Integer(key));
		}
	}

	public static boolean getKey(Window window, int keyCode) {
		return glfwGetKey(window.getWindow(), keyCode) == GLFW_PRESS;
	}

	public boolean getKeyDown(int code) {
		return currentKeys.contains(code);
	}

	public boolean getKeyPress(int code) {
		return pressedKeys.contains(code);
	}

	public boolean getKeyRelease(int code) {
		return releasedKeys.contains(code);
	}

	public static boolean getMouse(Window window, int mouseCode) {
		return glfwGetKey(window.getWindow(), mouseCode) == GLFW_PRESS;
	}

	public boolean getMouseDown(int code) {
		return currentMouse.contains(code);
	}

	public boolean getMousePress(int code) {
		return pressedMouse.contains(code);
	}

	public boolean getMouseRelease(int code) {
		return releasedMouse.contains(code);
	}
	
	public void setMousePosition(Vector2f pos){
		GLFW.glfwSetCursorPos(getWindow().getWindow(), pos.getX(), pos.getY());
		updateMousePos(pos.getX(), pos.getY());
//		Mouse.setCursorPosition((int)pos.getX(), (int)pos.getY());
	}
	
	public void setCursor(boolean enabled){
		GLFW.glfwSetInputMode(getWindow().getWindow(), 
				  GLFW.GLFW_CURSOR, 
				  !enabled ? GLFW.GLFW_CURSOR_DISABLED : GLFW.GLFW_CURSOR_NORMAL);
	}
	
	public Window getWindow() {
		return window;
	}

	public double getDelta() {
		return delta;
	}
	public void setDelta(double delta) {
		this.delta = delta;
	}

	public static final int GLFW_KEY_SPACE         = 0x20,
			GLFW_KEY_APOSTROPHE    = 0x27,
			GLFW_KEY_COMMA         = 0x2C,
			GLFW_KEY_MINUS         = 0x2D,
			GLFW_KEY_PERIOD        = 0x2E,
			GLFW_KEY_SLASH         = 0x2F,
			GLFW_KEY_0             = 0x30,
			GLFW_KEY_1             = 0x31,
			GLFW_KEY_2             = 0x32,
			GLFW_KEY_3             = 0x33,
			GLFW_KEY_4             = 0x34,
			GLFW_KEY_5             = 0x35,
			GLFW_KEY_6             = 0x36,
			GLFW_KEY_7             = 0x37,
			GLFW_KEY_8             = 0x38,
			GLFW_KEY_9             = 0x39,
			GLFW_KEY_SEMICOLON     = 0x3B,
			GLFW_KEY_EQUAL         = 0x3D,
			GLFW_KEY_A             = 0x41,
			GLFW_KEY_B             = 0x42,
			GLFW_KEY_C             = 0x43,
			GLFW_KEY_D             = 0x44,
			GLFW_KEY_E             = 0x45,
			GLFW_KEY_F             = 0x46,
			GLFW_KEY_G             = 0x47,
			GLFW_KEY_H             = 0x48,
			GLFW_KEY_I             = 0x49,
			GLFW_KEY_J             = 0x4A,
			GLFW_KEY_K             = 0x4B,
			GLFW_KEY_L             = 0x4C,
			GLFW_KEY_M             = 0x4D,
			GLFW_KEY_N             = 0x4E,
			GLFW_KEY_O             = 0x4F,
			GLFW_KEY_P             = 0x50,
			GLFW_KEY_Q             = 0x51,
			GLFW_KEY_R             = 0x52,
			GLFW_KEY_S             = 0x53,
			GLFW_KEY_T             = 0x54,
			GLFW_KEY_U             = 0x55,
			GLFW_KEY_V             = 0x56,
			GLFW_KEY_W             = 0x57,
			GLFW_KEY_X             = 0x58,
			GLFW_KEY_Y             = 0x59,
			GLFW_KEY_Z             = 0x5A,
			GLFW_KEY_LEFT_BRACKET  = 0x5B,
			GLFW_KEY_BACKSLASH     = 0x5C,
			GLFW_KEY_RIGHT_BRACKET = 0x5D,
			GLFW_KEY_GRAVE_ACCENT  = 0x60,
			GLFW_KEY_WORLD_1       = 0xA1,
			GLFW_KEY_WORLD_2       = 0xA2;
	public static final int
	GLFW_KEY_ESCAPE        = 0x100,
	GLFW_KEY_ENTER         = 0x101,
	GLFW_KEY_TAB           = 0x102,
	GLFW_KEY_BACKSPACE     = 0x103,
	GLFW_KEY_INSERT        = 0x104,
	GLFW_KEY_DELETE        = 0x105,
	GLFW_KEY_RIGHT         = 0x106,
	GLFW_KEY_LEFT          = 0x107,
	GLFW_KEY_DOWN          = 0x108,
	GLFW_KEY_UP            = 0x109,
	GLFW_KEY_PAGE_UP       = 0x10A,
	GLFW_KEY_PAGE_DOWN     = 0x10B,
	GLFW_KEY_HOME          = 0x10C,
	GLFW_KEY_END           = 0x10D,
	GLFW_KEY_CAPS_LOCK     = 0x118,
	GLFW_KEY_SCROLL_LOCK   = 0x119,
	GLFW_KEY_NUM_LOCK      = 0x11A,
	GLFW_KEY_PRINT_SCREEN  = 0x11B,
	GLFW_KEY_PAUSE         = 0x11C,
	GLFW_KEY_F1            = 0x122,
	GLFW_KEY_F2            = 0x123,
	GLFW_KEY_F3            = 0x124,
	GLFW_KEY_F4            = 0x125,
	GLFW_KEY_F5            = 0x126,
	GLFW_KEY_F6            = 0x127,
	GLFW_KEY_F7            = 0x128,
	GLFW_KEY_F8            = 0x129,
	GLFW_KEY_F9            = 0x12A,
	GLFW_KEY_F10           = 0x12B,
	GLFW_KEY_F11           = 0x12C,
	GLFW_KEY_F12           = 0x12D,
	GLFW_KEY_F13           = 0x12E,
	GLFW_KEY_F14           = 0x12F,
	GLFW_KEY_F15           = 0x130,
	GLFW_KEY_F16           = 0x131,
	GLFW_KEY_F17           = 0x132,
	GLFW_KEY_F18           = 0x133,
	GLFW_KEY_F19           = 0x134,
	GLFW_KEY_F20           = 0x135,
	GLFW_KEY_F21           = 0x136,
	GLFW_KEY_F22           = 0x137,
	GLFW_KEY_F23           = 0x138,
	GLFW_KEY_F24           = 0x139,
	GLFW_KEY_F25           = 0x13A,
	GLFW_KEY_KP_0          = 0x140,
	GLFW_KEY_KP_1          = 0x141,
	GLFW_KEY_KP_2          = 0x142,
	GLFW_KEY_KP_3          = 0x143,
	GLFW_KEY_KP_4          = 0x144,
	GLFW_KEY_KP_5          = 0x145,
	GLFW_KEY_KP_6          = 0x146,
	GLFW_KEY_KP_7          = 0x147,
	GLFW_KEY_KP_8          = 0x148,
	GLFW_KEY_KP_9          = 0x149,
	GLFW_KEY_KP_DECIMAL    = 0x14A,
	GLFW_KEY_KP_DIVIDE     = 0x14B,
	GLFW_KEY_KP_MULTIPLY   = 0x14C,
	GLFW_KEY_KP_SUBTRACT   = 0x14D,
	GLFW_KEY_KP_ADD        = 0x14E,
	GLFW_KEY_KP_ENTER      = 0x14F,
	GLFW_KEY_KP_EQUAL      = 0x150,
	GLFW_KEY_LEFT_SHIFT    = 0x154,
	GLFW_KEY_LEFT_CONTROL  = 0x155,
	GLFW_KEY_LEFT_ALT      = 0x156,
	GLFW_KEY_LEFT_SUPER    = 0x157,
	GLFW_KEY_RIGHT_SHIFT   = 0x158,
	GLFW_KEY_RIGHT_CONTROL = 0x159,
	GLFW_KEY_RIGHT_ALT     = 0x15A,
	GLFW_KEY_RIGHT_SUPER   = 0x15B,
	GLFW_KEY_MENU          = 0x15C,
	GLFW_KEY_LAST          = GLFW_KEY_MENU;
	
	public static final int
	GLFW_MOUSE_BUTTON_1      = 0x0,
	GLFW_MOUSE_BUTTON_2      = 0x1,
	GLFW_MOUSE_BUTTON_3      = 0x2,
	GLFW_MOUSE_BUTTON_4      = 0x3,
	GLFW_MOUSE_BUTTON_5      = 0x4,
	GLFW_MOUSE_BUTTON_6      = 0x5,
	GLFW_MOUSE_BUTTON_7      = 0x6,
	GLFW_MOUSE_BUTTON_8      = 0x7,
	GLFW_MOUSE_BUTTON_LAST   = GLFW_MOUSE_BUTTON_8,
	GLFW_MOUSE_BUTTON_LEFT   = GLFW_MOUSE_BUTTON_1,
	GLFW_MOUSE_BUTTON_RIGHT  = GLFW_MOUSE_BUTTON_2,
	GLFW_MOUSE_BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_3;
}
