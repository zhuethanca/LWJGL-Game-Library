package com.base.engine.components;

import com.base.engine.core.Vector3f;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Shader;

public class PointLight extends BaseLight {
	private static final int COLOR_DEPTH = 256;
	
	private Vector3f offset;
	private float range;
	private Attenuation atten;
	
	public PointLight(Vector3f color, float intensity, Attenuation atten, Vector3f offset){
		super(color, intensity);
		this.atten = atten;
		this.offset = offset;
		
		float a = atten.getExponent();
		float b = atten.getLinear();
		float c = atten.getConstant() - COLOR_DEPTH  * getIntensity() * getColor().max();

		double d = Math.sqrt(b*b - 4 * a * c);
		this.range = (float) Math.max(((-b + d)/(2 * a)), (-b - d)/(2 * a));//1000.0f;
		setShader(new Shader("forward-point"));
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}

	public Attenuation getAttenuation(){
		return atten;
	}

	public Vector3f getOffset() {
		return offset;
	}

	public void setOffset(Vector3f offset) {
		this.offset = offset;
	}
}
