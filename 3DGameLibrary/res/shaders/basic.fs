#version 330

//in vec4 color;

in vec2 texCoord0;

uniform vec3 color;
uniform sampler2D diffuse;

out vec4 fragColor;

void main(){
	vec4 textureColor = texture2D(diffuse, texCoord0.xy);
	if(textureColor == 0)
		fragColor = vec4(color, 1);
	else
		fragColor = textureColor * vec4(color, 1);
	//fragColor = color;//vec4(0.0, 1.0, 1.0, 1.0);
}