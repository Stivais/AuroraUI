package com.github.stivais.aurora.renderer.gl20

// mainly targeting old opengl versions

// vertex data

//class RendererImploLD(capacity: Int = 2_097_152) : RendererOld() {
//
//    private val shader = ShaderOld(
//        """
//        #version 330 core
//        layout (location = 0) in vec2 pos;
//        layout (location = 1) in uint paletteIndex;
//
//        uniform mat4 projection;
//        uniform usampler2D palette;
//
//        out vec4 vColor;
//
//        void main() {
//            gl_Position = projection * vec4(pos.x, pos.y, 0.0, 1.0);
//
//            uvec4 color = texelFetch(palette, ivec2(paletteIndex, 0), 0);
//
//            // needs to be reordered like this for whatever reason
//            vColor = vec4(color.zyxw);
//        }
//        """.trimIndent(),
//        """
//        #version 330 core
//
//        in vec4 vColor;
//
//        out vec4 FragColor;
//
//        void main() {
//            FragColor = vColor;
//        }
//        """.trimIndent(),
//    )
//
//    private var vertexBuffer: ByteBuffer = MemoryUtil.memAlloc(capacity)
//
//    private var indexBuffer: IntBuffer = MemoryUtil.memAllocInt(capacity shr 2)
//
//    private val vao: Int = glGenVertexArrays()
//
//    private val vbo: Int = glGenBuffers()
//
//    private val ibo: Int = glGenBuffers()
//
//    // used for rendering
//    private var vertexAmount = 0
//    private var indexAmount = 0
//
//    private var vertexIndex = 0
//    private var currentIndex = 0
//
//    private var currentColor: Int = 0
//
//    private val projectionMatrix = org.joml.Matrix4f()
//    private val projectionBuffer = MemoryUtil.memAllocFloat(16)
//
//    override fun render(width: Float, height: Float) {
//        if (vertexAmount != 0) {
//            projectionMatrix.setOrtho(0f, width, height, 0f, 1000f, -1000f)
//            shader.setMat4f("projection", projectionMatrix, projectionBuffer)
//
//            shader.use()
//
//            glUseProgram(shader.programID)
//            glActiveTexture(GL_TEXTURE0)
//            glBindTexture(GL_TEXTURE_2D, paletteTexture)
//
//            glBindVertexArray(vao)
//            glDrawElements(GL_TRIANGLES, indexAmount, GL_UNSIGNED_INT, 0)
//            glBindVertexArray(0)
//
//            glBindTexture(GL_TEXTURE_2D, 0)
//        }
//    }
//
//    private fun vertexNoIndices(x: Float, y: Float, z: Float = 0f) {
//        vertexBuffer
//            .putFloat(x)
//            .putFloat(y)
//            .putInt(currentColor)
//
//        vertexAmount++
//    }
//
//    private fun index(index: Int) {
//        indexBuffer.put(index)
//        currentIndex++
//    }
//
//    override fun uploadShape(shape: ShapeOld) {
//        val start = vertexIndex
//        for ((x, y) in shape.vertices) {
//            vertexNoIndices(x, y)
//            vertexIndex++
//        }
//        for (index in shape.indices) {
//            index(index + start)
//        }
//    }
//
//    override fun vertex(x: Float, y: Float, z: Float) {
//        vertexNoIndices(x, y, z)
//        index(vertexIndex)
//        vertexIndex++
//    }
//
//    // todo: outside of renderer, and inside a different place innit
//    override fun quad(x: Float, y: Float, w: Float, h: Float) {
//        val start = vertexIndex
//        val x2 = x + w
//        val y2 = y + h
//        vertexNoIndices(x, y)
//        vertexNoIndices(x2, y)
//        vertexNoIndices(x2, y2)
//        vertexNoIndices(x, y2)
//
//        index(start)
//        index(start + 1)
//        index(start + 2)
//
//        index(start)
//        index(start + 2)
//        index(start + 3)
//
//        vertexIndex += 4
//    }
//
//    override fun color(color: Color, palette: ColorPalette) {
//        currentColor = palette.getPaletteIndex(color)
//    }
//
//    private val paletteTexture = glGenTextures()
//
//
//    override fun uploadPalette(palette: ColorPalette) {
//
//        glBindTexture(GL_TEXTURE_2D, paletteTexture)
//
//        val data = palette.staticColors.toIntArray()
//
//        // Upload data as RGBA32UI
//        glTexImage2D(
//            GL_TEXTURE_2D,
//            0, // Mipmap level
//            GL_RGBA8,
//            palette.staticColors.size,
//            1, // 1D data in a 2D texture
//            0, // Border
//            GL_RGBA,
//            GL_UNSIGNED_BYTE,
//            data
//        )
//
//        // Texture parameters
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
//        glBindTexture(GL_TEXTURE_2D, 0)
//    }
//
//    override fun upload() {
//        val stride = 12
//        vertexBuffer.position(0)
//        vertexBuffer.limit(vertexIndex * stride)
//        indexBuffer.position(0)
//        indexBuffer.limit(currentIndex)
//
//        glBindVertexArray(vao)
//
//        glBindBuffer(GL_ARRAY_BUFFER, vbo)
//        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)
//
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW)
//
//        glEnableVertexAttribArray(0)
//        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0L)
//
//        // color
//        glEnableVertexAttribArray(1)
//        glVertexAttribIPointer(1, 1, GL_UNSIGNED_INT, stride, 8L)
//
//        glBindBuffer(GL_ARRAY_BUFFER, 0)
//        glBindVertexArray(0)
//
//        vertexAmount = vertexIndex
//        indexAmount = currentIndex
//
//        // reset for next upload
//        vertexIndex = 0
//        currentIndex = 0
//        vertexBuffer.position(0)
//        vertexBuffer.limit(vertexBuffer.capacity())
//        indexBuffer.position(0)
//        indexBuffer.limit(indexBuffer.capacity())
//    }
//
//    override fun cleanup() {
//        glDeleteVertexArrays(vao)
//        glDeleteBuffers(vbo)
//        MemoryUtil.memFree(vertexBuffer)
//        shader.delete()
//    }
//}