package com.base.engine.rendering;

import java.util.HashMap;

public class Values {
	HashMap<String, ValuesOfType<?>> values;

	public Values() {
		values = new HashMap<String, ValuesOfType<?>>();
	}

	public void addValuesOfType(String type, ValuesOfType<?> values) {
		this.values.put(type, values);
	}

	public ValuesOfType<?> getValuesOfType(String type) {
		return values.get(type);
	}

	public enum ObjectMapping {
		Vector3f("vec3"), Float("float"), Matrix4f("mat4"), DirectionalLight("DirectionalLight"), PointLight("PointLight"), SpotLight("SpotLight");
		
		private String type;
		
		ObjectMapping(String type) {
			this.type = type;
		}
		
		public String getTypeString() {
			return type;
		}
	}
}
