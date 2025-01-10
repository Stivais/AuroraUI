#version 460 core
layout (local_size_x = 1) in;

/**
 *  Aurora compute shader.
 *  Compute shader, which generates vertices for components.
 *  Each thread corresponds to component.
 *  It gets the component data based on an index retrieved from the thread's number.
 *
 *  Using the index, it retrieves a integer representing a component type.
 *
 *  The component type gets the necessary data it needs,
 *  Allocates enough space for vertex buffer,
 *  And generates the vertices.
**/

/**
 * Struct representing a vertex.
**/
struct Vertex {
    vec2 position;
    uint color;
};

layout(std430, binding = 0) buffer Vertices {
    Vertex vertices[];
};

layout(std430, binding = 1) readonly buffer Components {
    float component_data[];
};

layout(std430, binding = 2) readonly buffer ComponentIndices {
    uint component_indices[];
};

layout(std430, binding = 3) buffer Data {
    // also is used to get triangle count for cpu.
    int offset;
};

/**
 * PI Constant
**/
const float PI = 3.1415927f;

/**
 * Constant for how precise a triangle should be (amount of triangles to approximate it).
 *
 * The lower the number, the more precise.
**/
const float circle_precision = 0.35f;

/**
 * Current index for taking data from component_data.
 * It is recommended to retrieve data using the next or next_uint functions.
 *
 * Do not directly edit!
**/
uint current_index = 0;

/**
 * The offset in the vertex buffer to place vertices so it doesn't override other components.
 *
 * Use [allocate_vertex_space] to update this value.
 * Shouldn't be directly accessed unless necessary.
**/
uint vertex_offset = 0;

/**
 * Updates [vertex_offset], ensuring the component has enough space to upload vertices.
**/
void allocate_vertex_space(int vertex_count) {
    vertex_offset = atomicAdd(offset, vertex_count);
}

float next() {
    current_index += 1;
    return component_data[current_index];
}

vec2 next_2() {
    return vec2(next(), next());
}

vec4 next_4() {
    return vec4(next(), next(), next(), next());
}

uint next_uint() {
    current_index += 1;
    return floatBitsToUint(component_data[current_index]);
}

void upload_triangle(
    float x1, float y1,
    float x2, float y2,
    float x3, float y3,
    uint color
) {
    vertices[vertex_offset] = Vertex(vec2(x1, y1), color);
    vertices[vertex_offset + 1] = Vertex(vec2(x2, y2), color);
    vertices[vertex_offset + 2] = Vertex(vec2(x3, y3), color);
    vertex_offset += 3;
}

void upload_triangle(vec2 point1, vec2 point2, vec2 point3, uint color) {
    upload_triangle(point1.x, point1.y, point2.x, point2.y, point3.x, point3.y, color);
}

int calculate_segments(float radius) {
    float segments = 2.21 * sqrt(radius / circle_precision);
    return int(floor(segments + 0.5));
}

void main() {
    uint id = gl_GlobalInvocationID.x;
    // only time u should set this value
    current_index = (component_indices[id / 2] >> (16 * (id & 1u))) & 0xFFFFu;
    uint type = floatBitsToUint(component_data[current_index]);

    if (id == 0) {
        offset = 0;
    };

    switch (type) {


        /**
         * Triangle Component
         *
         * Vertex count: 3
         * Input data: x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, color: Int
        **/
        case 0:
        {
            // todo: maybe support 3d triangles, that get translated to 2d coordinates?
            allocate_vertex_space(3);
            upload_triangle(next_2(), next_2(), next_2(), next_uint());
        }
        break;


        /**
         * Rectangle component.
         *
         * Vertex count: 6
         * Input data: x: Float, y: Float, width: Float, height: Float, color: Int
        **/
        case 1:
        {
            float x = next(), y = next(), width = x + next(), height = y + next();
            uint color = next_uint();

            allocate_vertex_space(6);

            upload_triangle(x, y, width, y, width, height, color);
            upload_triangle(x, y, x, height, width, height, color);
        }
        break;

        /**
         * Rounded rectangle component.
         *
         * Vertex count: 12 + sum of corner segments.
         * Input:
         * x: Float, y: Float, width: Float, height: Float, color: Int,
         * top left: Float, top right: Float, bottom left: Float, bottom right: Float
        **/
        case 2:
        {
            // idk if this is bad or not but oh well
            const float angle_lookup[4] = float[](180, 270, 90, 0);

            float x = next(), y = next(), width = next(), height = next();

            uint color = next_uint();
            vec4 radii = next_4();

            ivec4 segments = ivec4(
                ceil(calculate_segments(radii[0]) / 4f),
                ceil(calculate_segments(radii[1]) / 4f),
                ceil(calculate_segments(radii[2]) / 4f),
                ceil(calculate_segments(radii[3]) / 4f)
            );

            vec2 center = vec2(
                x + width / 2f, y + height / 2f
            );

            allocate_vertex_space(12 + (segments.x + segments.y + segments.z + segments.w) * 3);

            // corners
            for (int corner = 0; corner < 4; corner++) {

                float radius = radii[corner];
                uint corner_segments = segments[corner];
                float angle_offset = angle_lookup[corner];
                float step = 90f / float(corner_segments);

                // if bit is 0 (first bit for horizontal, 2nd bit for vertical), shape.x/y + radius, else shape.x/y + shape.z/w - radius
                vec2 position_offset = vec2(
                    x + radius + ((corner & 1) * (width - radius * 2)),
                    y + radius + (((corner >> 1) & 1) * (height - radius * 2))
                );

                for (int i = 0; i < corner_segments; i++) {
                    float angle1 = step * i + angle_offset;
                    float angle2 = step * (i + 1) + angle_offset;

                    vec2 point1 = vec2(
                        radius * cos(angle1 * PI / 180f),
                        radius * sin(angle1 * PI / 180f)
                   ) + position_offset;

                    vec2 point2 = vec2(
                        radius * cos(angle2 * PI / 180f),
                        radius * sin(angle2 * PI / 180f)
                    ) + position_offset;

                    upload_triangle(
                        center,
                        point1,
                        point2,
                        color
                    );
                }
            }

            // filling gaps
            // i should probably figure out how to make this make the rect like how a triangle fan would
            float tl = radii[0], tr = radii[1], bl = radii[2], br = radii[3];
            upload_triangle(
                center.x, center.y,
                x + tl,         y,
                x + width - tr, y,
                color
            );
            upload_triangle(
                center.x, center.y,
                x + bl,         y + height,
                x + width - br, y + height,
                color
            );
            upload_triangle(
                center.x, center.y,
                x, y + tl,
                x, y + height - bl,
                color
            );
            upload_triangle(
                center.x, center.y,
                x + width, y + tr,
                x + width, y + height - br,
                color
            );
        }
        break;

//        case 3: // circle
//        {
//            vec3 shape = vec3(
//                component_data[index + 1],
//                component_data[index + 2],
//                component_data[index + 3]
//            );
//
//            uint color = component_data_uint(index + 4);
//
//            float radius = shape.z;
//            int segments = calculateSegments(radius, 0.35);
//
////            int current;
//            int current = atomicAdd(offset, 3 * segments);
//
//            float angleStep = (2 * 3.1415927f) / float(segments);
//
//            vec2 center = vec2(shape.xy);
//
//            float cosStep = cos(angleStep);
//            float sinStep = sin(angleStep);
//
//            // Initialize first angle
//            float cosCurrent = 1.0;
//            float sinCurrent = 0.0;
//
//            for (int i = 0; i < segments; i++) {
//                float cosNext = cosCurrent * cosStep - sinCurrent * sinStep;
//                float sinNext = sinCurrent * cosStep + cosCurrent * sinStep;
//
//                vec2 pos1 = vec2(
//                    radius * cosCurrent,
//                    radius * sinCurrent
//                ) + center;
//
//                vec2 pos2 = vec2(
//                    radius * cosNext,
//                    radius * sinNext
//                ) + center;
//
////                uploadTriangle(
////                    current + i * 3,
////                    center.x, center.y, pos1.x, pos1.y, pos2.x, pos2.y,
////                    color
////                );
//
//                cosCurrent = cosNext;
//                sinCurrent = sinNext;
//            }
//        }
//        break;
    }
}