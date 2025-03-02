#version 330

in vec4 v_color;
in vec2 texCoords;

out vec4 FragColor;

void main() {
    FragColor = vec4(v_color);
}