#version 430 core

in vec4 v_color;

out vec4 FragColor;

void main() {
    FragColor = vec4(v_color);
}