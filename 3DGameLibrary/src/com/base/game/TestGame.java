package com.base.game;

import com.base.engine.components.DirectionalLight;
import com.base.engine.components.FreeLook;
import com.base.engine.components.FreeMove;
import com.base.engine.components.MeshRenderer;
import com.base.engine.components.POVCamera;
import com.base.engine.components.PointLight;
import com.base.engine.components.SpotLight;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.Game;
import com.base.engine.core.GameObject;
import com.base.engine.core.Quaternion;
import com.base.engine.core.Transform;
import com.base.engine.core.Vector2f;
import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Vertex;

public class TestGame extends Game {
	private CoreEngine main;
	private GameObject planeObject;
	private Mesh mesh;
	private Transform transform;
	private Material material;
	// private Camera camera;
	// private GameObject obj;

	// PointLight pLight1 = new PointLight(new BaseLight(new Vector3f(1, 0.5f,
	// 0), 0.8f), new Attenuation(0, 0, 1), new Vector3f(-2f, 0f, 5f), 15);
	// PointLight pLight2 = new PointLight(new BaseLight(new Vector3f(0, 0.5f,
	// 1), 0.8f), new Attenuation(0, 0, 1), new Vector3f(2f, 0f, 7f), 15);
	//
	// SpotLight sLight = new SpotLight(new PointLight(new BaseLight(new
	// Vector3f(0, 1f, 1f), 0.8f), new Attenuation(0, 0, 0.1f), new
	// Vector3f(-2f, 0f, 5f), 10000), new Vector3f(1, 2, 1), 0.2f);

	public void init() {
		// camera = new Camera(main);
		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;

		Vertex[] vertices = new Vertex[] { new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)), new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)), new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)), new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f)) };
		fieldWidth /= 10f;
		fieldDepth /= 10f;
		Vertex[] vertices2 = new Vertex[] { new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)), new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)), new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)), new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f)) };

		int indices[] = { 0, 1, 2, 2, 1, 3/*
											 * , 0, 4, 5
											 */ };
		mesh = new Mesh(vertices, indices, true);
		Mesh mesh2 = new Mesh(vertices2, indices, true);
		material = new Material();//new Material(new Texture("test.png"), new Vector3f(1, 1, 1), 1, 8);
		material.addTexture("diffuse", new Texture("test.png"));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularExponent", 8);
		
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);

		planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);

		transform = new Transform();
		// Transform.setProjection(70f, main.width, main.height, 0.1f, 1000);
		// Transform.setCamera(camera);
		planeObject.setTransform(transform);

		GameObject directionalLightObject = new GameObject();
		DirectionalLight light = new DirectionalLight(new Vector3f(0f, 0f, 1f), 0.4f);
		directionalLightObject.addComponent(light);
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(0, 0, 0), (float)Math.toRadians(-45)));

		GameObject pointLightObject = new GameObject();
		int lightFieldWidth = 5;
		int lightFieldDepth = 5;
		float lightFieldStartX = 0;
		float lightFieldStartY = 0;
		float lightFieldStepX = 7;
		float lightFieldStepY = 7;
		for (int i = 0; i < lightFieldWidth; i++) {
			for (int j = 0; j < lightFieldDepth; j++) {
				pointLightObject.addComponent(new PointLight(new Vector3f(1, 0, 1), 0.8f, new Attenuation(0f, 0f, 0.4f), new Vector3f(lightFieldStartX + lightFieldStepX * i, 0, lightFieldStartY + lightFieldStepY * j)));
			}
		}
		GameObject spotLightObject = new GameObject();
		spotLightObject.addComponent(new SpotLight(new Vector3f(0, 1, 1), 0.4f, new Attenuation(0, 0, 0.1f), new Vector3f(1, 0, 0), 0.7f));
		spotLightObject.getTransform().setPos(new Vector3f(1, 0, 0));
		spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float) Math.toRadians(-90.0f)));

		addObject(planeObject);
		addObject(directionalLightObject);
		addObject(pointLightObject);
		addObject(spotLightObject);
		
		Mesh tempMesh = new Mesh("pallet.obj");
		GameObject testMesh1 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
		GameObject testMesh2 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
		GameObject testMesh3 = new GameObject().addComponent(new MeshRenderer(tempMesh, material));

		testMesh1.getTransform().getPos().set(0, 2, 0);
		testMesh1.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), 0.4f));

		testMesh2.getTransform().getPos().set(0, 0, 5);
		//testMesh3.getTransform().getPos().set(0, 0, 10);

		testMesh1.addChild(testMesh2);
		//testMesh1.addChild(testMesh3);

		addObject(testMesh1);
		testMesh3.getTransform().setPos(new Vector3f(10, 3, 10));
		addObject(testMesh3);
		
		Mesh tempMesh2 = new Mesh("monkey.obj");
		GameObject testMesh4 = new GameObject()/*.addComponent(new LookAtComponent())*/.addComponent(new MeshRenderer(tempMesh2, material)).addComponent(new PointLight(new Vector3f(1, 1, 1), 0.8f, new Attenuation(0f, 0f, 0.2f), new Vector3f(0,2,0))).addChild(new POVCamera((float) Math.toRadians(70.0f), ((float) main.width / (float) main.height), 0.01f, 1000f)).addComponent(new FreeLook()).addComponent(new FreeMove());
		testMesh4.getTransform().setPos(new Vector3f(15, 8, 10));
		addObject(testMesh4);
	}

	float temp = 0.0f;
	float tempAmt = 0.0f;

	@Override
	public void update(CoreEngine engine) {
		super.update(engine);
		transform.getPos().set(0, -1, 5);
	}

	public CoreEngine getMain() {
		return main;
	}

	public void setMain(CoreEngine main) {
		this.main = main;
	}

}
