package com.github.stivais.aurora.engine.renderer.tessellator

import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.IntBuffer

object Tessellator {

    const val STRIDE = 20

    // To prevent insufficient space
    // Either: use a really high value, or use a dynamic system
    // which raises the buffer amount if limit is reached
    const val CAPACITY = 2_097_152

    var vertexBuffer: ByteBuffer = MemoryUtil.memAlloc(CAPACITY)

    // find a reasonable size, relative to CAPACITY
    var indexBuffer: IntBuffer = MemoryUtil.memAllocInt(CAPACITY)

    private var currentMode: Mode = Mode.TRIANGLE

    private var vertexBeginOffset: Int = 0

    fun begin(mode: Mode) {
        currentMode = mode
        vertexBeginOffset = vertexBuffer.position() / STRIDE
    }

    fun vertex(
        x: Float,
        y: Float,
        color: Int,
        uv0: Float = 0f,
        uv1: Float = 0f,
    ) {
        vertexBuffer
            .putFloat(x)
            .putFloat(y)
            .putInt(color)
            .putFloat(uv0)
            .putFloat(uv1)

        val currVertex = (vertexBuffer.position() / STRIDE) - 1
        currentMode.put(indexBuffer, vertexBeginOffset, currVertex)
    }

    fun getCurrentIndex() = indexBuffer.position()

    fun beginUpload() {
        vertexBuffer.limit(vertexBuffer.position())
        vertexBuffer.position(0)
        indexBuffer.limit(indexBuffer.position())
        indexBuffer.position(0)
    }

    fun finishUpload() {
        vertexBuffer.limit(vertexBuffer.capacity())
        indexBuffer.limit(indexBuffer.capacity())
    }

    inline operator fun invoke(block: Tessellator.() -> Unit): Unit = block()
}