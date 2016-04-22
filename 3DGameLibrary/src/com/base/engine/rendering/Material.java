package com.base.engine.rendering;

import com.base.engine.core.Vector3f;

public class Material {
	private ValuesOfType<Texture> textureMap;
	private ValuesOfType<Vector3f> vector3fMap;
	private ValuesOfType<Float> floatMap;
	private Values values;

	// private Texture texture;
	// private Vector3f color;
	// private float specularIntensity;
	// private float specularExponent;
	public Material() {
		values = new Values();
		textureMap = new ValuesOfType<>();
		vector3fMap = new ValuesOfType<>();
		floatMap = new ValuesOfType<>();
		values.addValuesOfType("texture", textureMap);
		values.addValuesOfType("vector3f", vector3fMap);
		values.addValuesOfType("float", floatMap);
	}

	public void addTexture(String name, Texture texture) {
		textureMap.addValue(name, texture);
	}

	public Texture getTexture(String name) {
		if (textureMap.contains(name))
			return textureMap.getValue(name);
		return new Texture("test.png");
	}
	
	public boolean containsTexture(String name) {
		return textureMap.contains(name);
	}

	public void addVector3f(String name, Vector3f vector) {
		vector3fMap.addValue(name, vector);
	}

	public Vector3f getVector3f(String name) {
		if (vector3fMap.contains(name))
			return vector3fMap.getValue(name);
		return new Vector3f(0, 0, 0);
	}
	
	public boolean containsVector3f(String name) {
		return vector3fMap.contains(name);
	}

	public void addFloat(String name, float floatValue) {
		floatMap.addValue(name, floatValue);
	}

	public float getFloat(String name) {
		if (floatMap.contains(name))
			return floatMap.getValue(name);
		return 0;
	}
	
	public boolean containsFloat(String name) {
		return floatMap.contains(name);
	}
}
