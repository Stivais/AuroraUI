package com.github.stivais.aurora.renderer.impl

import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.renderer.shader.impl.Shader
import com.github.stivais.aurora.utils.getIntFromSSBO
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL43.GL_SHADER_STORAGE_BUFFER
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.ShortBuffer
import org.lwjgl.opengl.ARBShaderStorageBufferObject.GL_SHADER_STORAGE_BUFFER as GL_SSB

//
// Basic Renderer concept
//
// Utilizes compute shaders to generate vertices extremely fast.
//
// Needs a version which supports older opengl versions, however won't be as fast due to cpu needing to generate vertices

// abstract enough methods to be able to create any shape, or commonly used shapes
// for example:
// triangle (including xyz),
// line,
// rect (quad),
// rounded rect
// circle

// needs to support rotation, scaling on the vertices
// needs to support a color palette system (either by texture, or buffer)

// add some kind of enum for common amount of component amounts to interpret recommended capacity
// or make renderer render multiple instances of UIs at once
class RendererImpl : Renderer {

    private val shader = Shader.create(
        "assets/shader/aurora_vertex.vert",
        "assets/shader/aurora_fragment.frag",
    )

    private val computeShader = Shader.create(
        "assets/shader/aurora_component_compute.glsl",
    )

    private val vao = glGenVertexArrays()

    // todo: find reasonable buffer values (max component limit is either 32k or 64k)
    private val componentBuffer: ByteBuffer = MemoryUtil.memAlloc(CAPACITY_LOWER.toInt())

    private val pointerBuffer: ShortBuffer = MemoryUtil.memAllocShort(CAPACITY_LOWER.toInt() shr 2)

    private val vertexSSBO = glGenBuffers()
    private val componentSSBO = glGenBuffers()
    private val pointerSSBO = glGenBuffers()
    private val dataSSBO = glGenBuffers()

    private var componentAmount = 0
    // not sure if this name is fine or not, or if it makes sense
    private var currentPointerOffset = 0

    private var triangleCount = 0

    private val projectionMatrix = org.joml.Matrix4f()

    init {
        // load the ssbo
        glBindBufferBase(GL_SSB, 0, vertexSSBO)
        glBufferData(GL_SSB, CAPACITY, GL_DYNAMIC_DRAW)

        glBindBufferBase(GL_SSB, 1, componentSSBO)
        glBufferData(GL_SSB, CAPACITY_LOWER, GL_DYNAMIC_DRAW)

        glBindBufferBase(GL_SSB, 2, pointerSSBO)
        glBufferData(GL_SSB, CAPACITY_LOWER, GL_DYNAMIC_DRAW)

        // only contains an integer so size is only enough to fit that
        glBindBufferBase(GL_SSB, 3, dataSSBO)
        glBufferData(GL_SSB, 4, GL_DYNAMIC_DRAW)
    }

    override fun resize(width: Float, height: Float) {
        shader.start()
        projectionMatrix.setOrtho(0f, width, height, 0f, 1000f, -1000f)
        shader.setMat4f("projection", projectionMatrix)
    }

    override fun cleanup() {
        shader.cleanup()
        computeShader.cleanup()

        glDeleteVertexArrays(vao)
        glDeleteBuffers(vertexSSBO)
        glDeleteBuffers(componentSSBO)
        glDeleteBuffers(pointerSSBO)
        glDeleteBuffers(dataSSBO)
    }

    override fun render() {
        glClear(GL_COLOR_BUFFER_BIT)
//        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_ALPHA)

        shader.start()
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, vertexSSBO)
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, componentSSBO)
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, pointerSSBO)
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 3, dataSSBO)

        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLES, 0, triangleCount)
    }

    override fun upload() {
        componentBuffer.position(0)
        componentBuffer.limit(currentPointerOffset * 4)
        pointerBuffer.position(0)
        pointerBuffer.limit(componentAmount)

        // bind all ssbo for the shader. only component and pointer ssbo need updating
        glBindBufferBase(GL_SSB, 0, vertexSSBO)
        glBindBufferBase(GL_SSB, 1, componentSSBO)
        glBufferSubData(GL_SSB, 0, componentBuffer)
        glBindBufferBase(GL_SSB, 2, pointerSSBO)
        glBufferSubData(GL_SSB, 0, pointerBuffer)
        glBindBufferBase(GL_SSB, 3, dataSSBO)

        computeShader.start()
        computeShader.dispatch(componentAmount, 1, 1)

        triangleCount = getIntFromSSBO(dataSSBO, 0)

        // reset for next upload
        componentAmount = 0
        currentPointerOffset = 0
        componentBuffer.clear()
        pointerBuffer.clear()
    }

    override fun rect(x: Float, y: Float, width: Float, height: Float, color: Int) {
        begin()
        put(1)
        put(x)
        put(y)
        put(width)
        put(height)
        put(color)
        finish()
    }

    override fun roundedRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int,
        tl: Float,
        tr: Float,
        bl: Float,
        br: Float
    ) {
        begin()
        put(2)
        put(x, y, width, height)
        put(color)
        put(tl, tr, bl, br)
        finish()
    }

    override fun circle(x: Float, y: Float, radius: Float, color: Int) {
        begin()
        put(3)
        put(x, y)
        put(radius)
        put(color)
        finish()
    }

    private var offset: Int = 0

    private fun begin() {
        offset = 0
        pointerBuffer.put(currentPointerOffset.toShort())
    }

    private fun finish() {
        if (offset != 0) {
            currentPointerOffset += offset
            componentAmount++
        }
    }

    private fun put(value: Int) {
        componentBuffer.putInt(value)
        offset++
    }

    private fun put(value: Float) {
        componentBuffer.putFloat(value)
        offset++
    }

    private fun put(first: Float, second: Float) {
        put(first)
        put(second)
    }

    private fun put(first: Float, second: Float, third: Float, fourth: Float) {
        put(first)
        put(second)
        put(third)
        put(fourth)
    }

    companion object {
         // 2MB
        const val CAPACITY = 2_097_152L * 32

        // 0.5MB
        const val CAPACITY_LOWER = 524_288L * 32
    }
}