package com.github.stivais.aurora.renderer.opengl.shader

import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

// currently attributes per vertex is
// - x, y (2 floats),
// - color (1 int)

// in the future, use:
// - x, y (2 floats (maybe even pack to 1 float/int, if precision is not noticeable))
// - color "channel" index
// - texture data (uv, index or whatever)

//todo: use ibo
class VAOBuilder(capacity: Int) {

    // temp value (2 MB)
    constructor() : this(2_097_152)

    private var vertexBuffer: ByteBuffer = MemoryUtil.memAlloc(capacity)

    private val vao: Int = glGenVertexArrays()

    private val vbo: Int = glGenBuffers()

    // not sure if this is needed
    private var vertexAmount = 0

    // color thing rn is temp
    fun vertex(x: Float, y: Float, r: Byte, g: Byte, b: Byte) {
        vertexBuffer
            .putFloat(x)
            .putFloat(y)
            .put(r)
            .put(g)
            .put(b)
            .put(255.toByte())
        vertexAmount++
    }

    fun upload() {
        vertexBuffer.position(0)
        vertexBuffer.limit(vertexAmount * 12 /* vertex amount * vertex size in bytes (temp solution) */)

        glBindVertexArray(vao)

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW)

        val stride = java.lang.Float.BYTES * 2 + java.lang.Byte.BYTES * 4

        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0L)

        glEnableVertexAttribArray(1)
        glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, stride, 8L)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun render() {
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLES, 0, vertexAmount)
    }

    fun cleanup() {
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbo)
        MemoryUtil.memFree(vertexBuffer)
    }
}