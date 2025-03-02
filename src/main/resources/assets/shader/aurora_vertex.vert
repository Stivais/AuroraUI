#version 330

layout(location = 0) in vec2 pos;
layout(location = 1) in uint color;
layout(location = 2) in vec2 uvs;

uniform mat4 projection;

out vec4 v_color;
out vec2 texCoords;

void main() {
    gl_Position = projection * vec4(pos, 1.0, 1.0);

    float alpha = float((color >> 24u) & 0xFFu) / 255.0;
    float red   = float((color >> 16u) & 0xFFu) / 255.0;
    float green = float((color >> 8u) & 0xFFu) / 255.0;
    float blue  = float(color & 0xFFu) / 255.0;

    v_color = vec4(red, green, blue, alpha);
    texCoords = uvs;
}