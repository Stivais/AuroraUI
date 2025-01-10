#version 460 core
layout (local_size_x = 1) in;

layout(std430, binding = 0) buffer VertexData {
    vec3 vertices[];
};

// components data, should be access with pointers.
// common data:
// 1st int - type
// 2nd int - x
// 3rd int - y
// 4th int - width
// 5th int - height

layout(std430, binding = 1) readonly buffer Component {
    int component_data[];
};

layout(std430, binding = 2) readonly buffer ComponentPointers {
    uint component_pointers[];
};

layout(std430, binding = 3) buffer DataOffset {
    int offset;
};

float component_data_float(uint index) {
    return intBitsToFloat(component_data[index]);
}

// maybe do some kind of occlusion culling, per vertex point
void uploadTriangle(uint offset, float x1, float y1, float x2, float y2, float x3, float y3) {
    vertices[offset] = vec3(x1, y1, 0);
    vertices[offset + 1] = vec3(x2, y2, 0);
    vertices[offset + 2] = vec3(x3, y3, 0);
}

void main() {
    uint id = gl_GlobalInvocationID.x;

    if (id == 0) {
        offset = 0;
    }

    uint ptr = component_pointers[id];
    uint type = component_data[ptr];

    switch (type) {
        case 0: // Triangle // 1: x1, 2: y1, 3: x1, 4: y2, 5: x3, 6: y3
            {
                int current = atomicAdd(offset, 3);//int(id * 3);

                uploadTriangle(
                    current,
                    component_data_float(ptr + 1), component_data_float(ptr + 2),
                    component_data_float(ptr + 3), component_data_float(ptr + 4),
                    component_data_float(ptr + 5), component_data_float(ptr + 6)
                );
            }
            break;
        case 1: // rect
            {
                vec4 shape = vec4(
                    component_data_float(ptr + 1),
                    component_data_float(ptr + 2),
                    component_data_float(ptr + 3),
                    component_data_float(ptr + 4)
                );

                int current = atomicAdd(offset, 6);
                barrier();

                vertices[current] = vec3(shape.x, shape.y, 0);
                vertices[current + 1] = vec3(shape.x + shape.z, shape.y, 0);
                vertices[current + 2] = vec3(shape.x + shape.z, shape.y + shape.w, 0);

                vertices[current + 3] = vec3(shape.x, shape.y, 0);
                vertices[current + 4] = vec3(shape.x, shape.y + shape.w, 0);
                vertices[current + 5] = vec3(shape.x + shape.z, shape.y + shape.w, 0);
            }
            break;

        case 2: // circle
        {
            vec3 shape = vec3(
                intBitsToFloat(component_data[ptr + 1]),
                intBitsToFloat(component_data[ptr + 2]),
                intBitsToFloat(component_data[ptr + 3])
            );

            float radius = shape.z;

            int segments = 32;

            int current = atomicAdd(offset, 3 * segments);

            float angleStep = 360f / float(segments);

            vec3 center = vec3(shape.xy, 0);

            for (int i = 0; i < segments; i++) {
                float angle1 = angleStep * i;
                float angle2 = angleStep * (i + 1);

                vec3 pos1 = vec3(
                    radius * cos(angle1 * 3.1415927f / 180f),
                    radius * sin(angle1 * 3.1415927f / 180f),
                    0
                ) + center;

                vec3 pos2 = vec3(
                    radius * cos(angle2 * 3.1415927f / 180f),
                    radius * sin(angle2 * 3.1415927f / 180f),
                    0
                ) + center;

                vertices[current + i * 3] = center;
                vertices[current + i * 3 + 1] = pos1;
                vertices[current + i * 3 + 2] = pos2;
            };
        }
        break;
    }
}