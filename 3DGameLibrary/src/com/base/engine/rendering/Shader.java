package com.base.engine.rendering;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Set;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.PointLight;
import com.base.engine.components.SpotLight;
import com.base.engine.core.Matrix4f;
import com.base.engine.core.RenderingEngine;
import com.base.engine.core.Transform;
import com.base.engine.core.Util;
import com.base.engine.core.Vector3f;
import com.base.engine.interfaces.UniformUpdater;
import com.base.engine.rendering.resourceManagement.ShaderResource;

public class Shader {
	private ShaderResource resource;
	private static HashMap<String, ShaderResource> loadedShaders = new HashMap<>();
	private static HashMap<String, UniformUpdater> updaters;
	private String fileName;

	public Shader(String fileName) {
		this.fileName = fileName;
		if ((resource = loadedShaders.get(fileName)) == null) {
			resource = new ShaderResource();

			String vertexShaderText = loadShader(fileName + ".vs");
			String fragmentShaderText = loadShader(fileName + ".fs");

			addVertexShader(vertexShaderText);
			addFragmentShader(fragmentShaderText);

			parseStructs(vertexShaderText);
			parseStructs(fragmentShaderText);

			addAllAttributes(vertexShaderText);

			compileShader();

			addAllUniforms(vertexShaderText);
			addAllUniforms(fragmentShaderText);
			loadedShaders.put(fileName, resource);
		}
		resource.addReference();
		updaters = new HashMap<>();
	}
	
	@Override
	protected void finalize(){
		if(resource.removeReference() && !fileName.isEmpty()){
			loadedShaders.remove(fileName);
		}
	}

	public void bind() {
		glUseProgram(resource.getProgram());
	}

	public void setAttribLocation(String attributeName, int location) {
		glBindAttribLocation(resource.getProgram(), location, attributeName);
	}

	public void updateUniforms(Transform transform, Material material, RenderingEngine renderingEngine) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f MVPMatrix = renderingEngine.getMainCamera().getViewProjection().mul(worldMatrix);
		material.getTexture("diffuse").bind();
		for (String uniformName : resource.getUniformTypes().keySet()) {
			String uniformType = resource.getUniformTypes().get(uniformName);
			if (updaters.containsKey(uniformName))
				updaters.get(uniformName).updateUniform(uniformName, uniformType, transform, material, worldMatrix, MVPMatrix, this, renderingEngine);
			else if (uniformType.equals("sampler2D")) {
				int samplerSlot = renderingEngine.getSamplerSlot(uniformName);
				material.getTexture(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			} else if (uniformName.equals("MVP"))
				setUniform(uniformName, MVPMatrix);
			else if (uniformName.equals("model"))
				setUniform(uniformName, worldMatrix);
			else if (uniformName.equals("eyePos"))
				setUniform(uniformName, renderingEngine.getMainCamera().getTransform().getTransformedPos());
			else if (uniformType.equals("vec3")) {
				if (material.containsVector3f(uniformName))
					setUniform(uniformName, material.getVector3f(uniformName));
				if (renderingEngine.getUniforms().getValuesOfType(uniformType).contains(uniformName))
					setUniform(uniformName, (Vector3f) renderingEngine.getUniforms().getValuesOfType(uniformType).getValue(uniformName));
			} else if (uniformType.equals("float")) {
				if (material.containsFloat(uniformName))
					setUniformf(uniformName, material.getFloat(uniformName));
				if (renderingEngine.getUniforms().getValuesOfType(uniformType).contains(uniformName))
					setUniformf(uniformName, (float) renderingEngine.getUniforms().getValuesOfType(uniformType).getValue(uniformName));
			} else if (uniformType.equals("DirectionalLight"))
				setUniformDirectionalLight(uniformName, (DirectionalLight) renderingEngine.getActiveLight());
			else if (uniformType.equals("PointLight"))
				setUniformPointLight(uniformName, (PointLight) renderingEngine.getActiveLight());
			else if (uniformType.equals("SpotLight"))
				setUniformSpotLight(uniformName, (SpotLight) renderingEngine.getActiveLight());
		}
	}

	public void addAllAttributes(String shaderText) {
		final String ATTRIBUTE_KEYWORD = "attribute ";
		int attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD);
		int i = 0;
		while (attributeStartLocation != -1) {
			if (attributeStartLocation != 0)
				if (Character.isLetterOrDigit(shaderText.charAt(attributeStartLocation - 1))) {
					attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length() - 1);
					continue;
				}
			String[] sa = shaderText.substring(attributeStartLocation, shaderText.indexOf(";", attributeStartLocation)).split("\\s+");
			String attributeName = sa[2];

			setAttribLocation(attributeName, i);

			attributeStartLocation = shaderText.indexOf(ATTRIBUTE_KEYWORD, attributeStartLocation + ATTRIBUTE_KEYWORD.length() - 1);
			i++;
		}
	}

	public void addAllUniforms(String shaderText) {
		final String UNIFORM_KEYWORD = "uniform ";
		int uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD);
		while (uniformStartLocation != -1) {
			if (uniformStartLocation != 0)
				if (Character.isLetterOrDigit(shaderText.charAt(uniformStartLocation - 1))) {
					uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length() - 1);
					continue;
				}
			String[] sa = shaderText.substring(uniformStartLocation, shaderText.indexOf(";", uniformStartLocation)).split("\\s+");
			String uniformType = sa[1].trim(), uniformName = sa[2].trim();
			Struct struct = Struct.getStruct(uniformType);
			if (struct == null)
				struct = new Struct(uniformType, false);
			if (!struct.isStruct())
				addUniform(uniformName);
			else
				addUniformStruct(uniformName + ".", struct);
			resource.getUniformTypes().put(uniformName, uniformType);
			uniformStartLocation = shaderText.indexOf(UNIFORM_KEYWORD, uniformStartLocation + UNIFORM_KEYWORD.length() - 1);
		}
	}

	private void addUniformStruct(String parent, Struct structI) {
		for (String name : structI.getKeySet()) {
			String type = structI.getType(name);
			Struct struct = Struct.getStruct(type);
			if (struct == null)
				struct = new Struct(type, false);
			if (!struct.isStruct())
				addUniform(parent + name);
			else
				addUniformStruct(parent + name + ".", struct);
		}
	}

	public void parseStructs(String shaderText) {
		final String STRUCT_KEYWORD = "struct ";
		int structStartLocation = shaderText.indexOf(STRUCT_KEYWORD);
		while (structStartLocation != -1) {
			if (structStartLocation != 0)
				if (Character.isLetterOrDigit(shaderText.charAt(structStartLocation - 1))) {
					structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length() - 1);
					continue;
				}
			String structData = shaderText.substring(structStartLocation + STRUCT_KEYWORD.length(), shaderText.indexOf('}', structStartLocation) + 1);
			parseStruct(structData);
			structStartLocation = shaderText.indexOf(STRUCT_KEYWORD, structStartLocation + STRUCT_KEYWORD.length() - 1);
		}
	}

	private void parseStruct(String structText) {
		int startLoc = structText.indexOf('{');
		String structName = structText.substring(0, startLoc).trim();
		String[] fields = structText.substring(startLoc + 1, structText.indexOf('}')).trim().split(";");
		Struct struct = Struct.getStruct(structName);
		if (struct == null) {
			struct = new Struct(structName, true);
			for (String s : fields) {
				s = s.trim();
				String[] sa = s.split("\\s+");
				if (sa.length != 2)
					continue;
				String type = sa[0], name = sa[1];
				struct.addField(name, type);
			}
		}
	}

	public static void addUniformUpdater(String name, UniformUpdater updater) {
		updaters.put(name, updater);
	}

	public void addUniform(String uniform) {
		int uniformLocation = glGetUniformLocation(resource.getProgram(), uniform);

		if (uniformLocation == 0xFFFFFFFF) {
			System.err.println("Error: Could not find uniform: " + uniform);
			new Exception().printStackTrace();
			System.exit(1);
		}

		resource.getUniforms().put(uniform, uniformLocation);
	}

	public void setUniformPointLight(String uniformName, PointLight pointLight) {
		setUniformBaseLight(uniformName + ".base", pointLight);
		setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent());
		setUniform(uniformName + ".position", pointLight.getTransform().getPos().add(pointLight.getOffset()));
		setUniformf(uniformName + ".range", pointLight.getRange());
	}

	public void addVertexShaderFromFile(String text) {
		addProgram(loadShader(text), GL_VERTEX_SHADER);
	}

	public void addGeometryShaderFromFile(String text) {
		addProgram(loadShader(text), GL_GEOMETRY_SHADER);
	}

	public void addFragmentShaderFromFile(String text) {
		addProgram(loadShader(text), GL_FRAGMENT_SHADER);
	}

	public void addVertexShader(String text) {
		addProgram(text, GL_VERTEX_SHADER);
	}

	public void addGeometryShader(String text) {
		addProgram(text, GL_GEOMETRY_SHADER);
	}

	public void addFragmentShader(String text) {
		addProgram(text, GL_FRAGMENT_SHADER);
	}

	public void setUniformBaseLight(String uniformName, BaseLight baseLight) {
		setUniform(uniformName + ".color", baseLight.getColor());
		setUniformf(uniformName + ".intensity", baseLight.getIntensity());
	}

	public void compileShader() {
		glLinkProgram(resource.getProgram());
		if (glGetProgrami(resource.getProgram(), GL_LINK_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}

		glValidateProgram(resource.getProgram());
		if (glGetProgrami(resource.getProgram(), GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(resource.getProgram(), 1024));
			System.exit(1);
		}
	}

	private void addProgram(String text, int type) {
		int shader = glCreateShader(type);
		if (shader == 0) {
			System.err.println("Shader creation failed: Could not find valid memory location when adding shader");
			System.exit(1);
		}

		glShaderSource(shader, text);
		glCompileShader(shader);

		if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
			System.err.println(glGetShaderInfoLog(shader, 1024));
			System.exit(1);
		}

		glAttachShader(resource.getProgram(), shader);
	}

	protected static String loadShader(String fileName) {
		StringBuilder shaderSource = new StringBuilder();
		BufferedReader shaderReader = null;
		final String INCLUDE_DIRECTIVE = "#include";
		final String SKIP = "//";

		try {
			shaderReader = new BufferedReader(new FileReader("./res/shaders/" + fileName));
			String line;
			while ((line = shaderReader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith(SKIP))
					continue;
				else if (line.startsWith(INCLUDE_DIRECTIVE)) {
					line = line.substring(INCLUDE_DIRECTIVE.length() + 2, line.length() - 1);
					if (line.charAt(0) == ' ')
						line = line.trim().substring(1);
					shaderSource.append(loadShader(line));
				} else
					shaderSource.append(line).append("\n");
			}
			shaderReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return shaderSource.toString();
	}

	public void setUniformi(String uniformName, int value) {
		glUniform1i(resource.getUniforms().get(uniformName), value);
	}

	public void setUniformf(String uniformName, float value) {
		glUniform1f(resource.getUniforms().get(uniformName), value);
	}

	public void setUniform(String uniformName, Vector3f value) {
		glUniform3f(resource.getUniforms().get(uniformName), value.getX(), value.getY(), value.getZ());
	}

	public void setUniform(String uniformName, Matrix4f value) {
		glUniformMatrix4fv(resource.getUniforms().get(uniformName), true, Util.createFlippedBuffer(value));
	}

	public void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight) {
		setUniformBaseLight(uniformName + ".base", directionalLight);
		setUniform(uniformName + ".direction", directionalLight.getDirection());
	}

	public void setUniformSpotLight(String uniformName, SpotLight spotLight) {
		setUniformPointLight(uniformName + ".pointLight", spotLight);
		setUniform(uniformName + ".direction", spotLight.getDirection());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
}

class Struct {
	private String name;
	private boolean isStruct;
	private static HashMap<String, Struct> structMap = new HashMap<>();
	private HashMap<String, String> fieldMap = new HashMap<>();// FieldName:FieldType

	public Struct(String name, boolean isStruct) {
		this.name = name;
		if (!structMap.containsKey(name))
			structMap.put(name, this);
		this.isStruct = isStruct;
	}

	public boolean isStruct() {
		return isStruct;
	}

	public String getName() {
		return name;
	}

	public static Struct getStruct(String type) {
		return structMap.get(type);
	}

	public Set<String> getFields() {
		return fieldMap.keySet();
	}

	public String getType(String name) {
		return fieldMap.get(name);
	}

	public void addField(String name, String type) {
		fieldMap.put(name, type);
	}

	public Set<String> getKeySet() {
		return fieldMap.keySet();
	}
}
