package com.base.engine.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_EQUAL;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.util.ArrayList;

import com.base.engine.components.BaseLight;
import com.base.engine.components.Camera;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Values;
import com.base.engine.rendering.ValuesOfType;

public class RenderingEngine {
	private Values values;
	private ValuesOfType<Integer> samplerMap;
	private Camera mainCamera;

	private ArrayList<BaseLight> lights;
	private BaseLight activeLight;

	private Shader forwardAmbient;
	private CoreEngine main;

	@SuppressWarnings("unchecked")
	public RenderingEngine(CoreEngine main) {
		this.main = main;
		initGraphics();
		lights = new ArrayList<>();
		forwardAmbient = new Shader("forward-Ambient");
		initValues();
		((ValuesOfType<Vector3f>) values.getValuesOfType(Values.ObjectMapping.Vector3f.getTypeString())).addValue("ambientIntensity", new Vector3f(0.1f, 0.1f, 0.1f));
	}

	private void initValues() {
		values = new Values();
		samplerMap = new ValuesOfType<>();
		samplerMap.addValue("diffuse", 0);
		values.addValuesOfType(Values.ObjectMapping.Float.getTypeString(), new ValuesOfType<Float>());
		values.addValuesOfType(Values.ObjectMapping.Vector3f.getTypeString(), new ValuesOfType<Float>());
	}

	public void updateUniformStruct(Transform transform, Material material, Shader shader, String uniformName, String uniformType) {
		throw new IllegalArgumentException(uniformType + " is not a supported type in RenderingEngine");
	}

	public int getSamplerSlot(String samplerName) {
		return samplerMap.getValue(samplerName);
	}

	public void addLight(BaseLight light) {
		lights.add(light);
	}

	public void render(GameObject object) {
		clearScreen();

		object.renderAll(forwardAmbient, this);

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);

		for (BaseLight light : lights) {
			activeLight = light;
			object.renderAll(light.getShader(), this);
		}

		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);

		glDisable(GL_CULL_FACE);
//		glPushMatrix();
//		glBegin(GL_QUADS); // Each set of 4 vertices form a quad
//		glColor3f(1.0f, 0.0f, 0.0f); // Red
//		glVertex2f(-0.5f, -0.5f); // x, y
//		glVertex2f(0.5f, -0.5f);
//		glVertex2f(0.5f, 0.5f);
//		glVertex2f(-0.5f, 0.5f);
//		glEnd();
//
//		glFlush();
		glEnable(GL_CULL_FACE);
	}

	public static void clearScreen() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void setTextures(boolean enabled) {
		if (enabled)
			glEnable(GL_TEXTURE_2D);
		else
			glDisable(GL_TEXTURE_2D);
	}

	public static void unbindTextures() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	@Deprecated
	public static void setClearColor(Vector3f color) {
		glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
	}

	private static void initGraphics() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		glEnable(GL_DEPTH_CLAMP);

		glEnable(GL_TEXTURE_2D);
		// glEnable(GL_FRAMEBUFFER_SRGB);
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	public void addCamera(Camera camera) {
		mainCamera = camera;
	}

	public Camera getMainCamera() {
		return mainCamera;
	}

	public void setMainCamera(Camera mainCamera) {
		this.mainCamera = mainCamera;
	}

	public BaseLight getActiveLight() {
		return activeLight;
	}

	public Values getUniforms() {
		return values;
	}

	// public void addPointLight(PointLight pointLight) {
	// pointLights.add(pointLight);
	// }

}
