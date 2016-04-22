package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;

import static org.lwjgl.opengl.GL15.*;

public class ShaderResource {
	private HashMap<String, Integer> uniforms;
	private HashMap<String, String> uniformTypes;
	private int program;
	private int refCount = 0;
	
	public ShaderResource() {
		this.program = glCreateProgram();
		uniforms = new HashMap<>();
		uniformTypes = new HashMap<>();
		if (program == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location in constructor");
			System.exit(1);
		}
	}
	
	@Override
	protected void finalize(){
		glDeleteBuffers(program);
	}

	public void addReference(){
		refCount++;
	}
	
	public boolean removeReference(){
		refCount --;
		return refCount == 0;
	}

	public int getProgram() {
		return program;
	}

	public HashMap<String, Integer> getUniforms() {
		return uniforms;
	}

	public HashMap<String, String> getUniformTypes() {
		return uniformTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + program;
		result = prime * result + ((uniformTypes == null) ? 0 : uniformTypes.hashCode());
		result = prime * result + ((uniforms == null) ? 0 : uniforms.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShaderResource other = (ShaderResource) obj;
		if (program != other.program)
			return false;
		if (uniformTypes == null) {
			if (other.uniformTypes != null)
				return false;
		} else if (!uniformTypes.equals(other.uniformTypes))
			return false;
		if (uniforms == null) {
			if (other.uniforms != null)
				return false;
		} else if (!uniforms.equals(other.uniforms))
			return false;
		return true;
	}
}
