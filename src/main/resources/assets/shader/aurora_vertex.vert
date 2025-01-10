#version 430 core

struct Vertex {
    vec2 position;
    uint color;
};

layout(std430, binding = 0) readonly buffer VertexData {
    Vertex vertices[];
};

uniform mat4 projection;

out vec4 v_color;

void main() {
    Vertex vertex = vertices[gl_VertexID];
    gl_Position = projection * vec4(vertex.position, 1.0, 1.0);

    uint packed_color = vertex.color;

    float alpha = float((packed_color >> 24u) & 0xFFu) / 255.0;
    float red   = float((packed_color >> 16u) & 0xFFu) / 255.0;
    float green = float((packed_color >> 8u) & 0xFFu) / 255.0;
    float blue  = float(packed_color & 0xFFu) / 255.0;

    v_color = vec4(red, green, blue, alpha);
}