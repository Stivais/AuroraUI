package com.github.stivais.aurora.engine.renderer.context

import com.github.stivais.aurora.engine.renderer.tessellator.Tessellator
import org.lwjgl.opengl.GL30.*


// this class holds the vao/vbo for a UI
class AuroraContext {

    private val vao: Int = glGenVertexArrays()

    private val vbo: Int = glGenBuffers()

    private val ibo: Int = glGenBuffers()

    init {
        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, Tessellator.CAPACITY.toLong(), GL_DYNAMIC_DRAW)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, Tessellator.CAPACITY / 4L, GL_DYNAMIC_DRAW)
    }

    fun upload(tessellator: Tessellator) {
        tessellator.beginUpload()

        glBindVertexArray(vao)
        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferSubData(GL_ARRAY_BUFFER, 0, tessellator.vertexBuffer)

        glEnableVertexAttribArray(0)
        glVertexAttribPointer(0, 2, GL_FLOAT, false, tessellator.STRIDE, 0L)
        glEnableVertexAttribArray(1)
        glVertexAttribIPointer(1, 1, GL_UNSIGNED_INT, tessellator.STRIDE, 8L)
        glEnableVertexAttribArray(2)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, tessellator.STRIDE, 12L)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
        glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, tessellator.indexBuffer)

        tessellator.finishUpload()
    }

    fun bind() {
        glBindVertexArray(vao)
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo)
    }

    fun unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        glBindVertexArray(0)
    }

    fun cleanup() {
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbo)
        glDeleteBuffers(ibo)
    }
}