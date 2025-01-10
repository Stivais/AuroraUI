#version 460 core

layout(std430, binding = 0) buffer VertexData {
    vec3 vertices[];
};

uniform mat4 projection;

out vec3 vColor;

void main() {
    gl_Position = projection * vec4(vertices[gl_VertexID], 1.0); // Use SSBO data for vertex positions
    vColor = vec3(1, 0, 0);
}
