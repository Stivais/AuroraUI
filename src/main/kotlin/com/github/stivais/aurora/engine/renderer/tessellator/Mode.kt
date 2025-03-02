package com.github.stivais.aurora.engine.renderer.tessellator

import java.nio.IntBuffer

enum class Mode : Test {
    TRIANGLE {
        override fun put(indexBuffer: IntBuffer, vertexBeginOffset: Int, currentVertex: Int) {
            indexBuffer.put(currentVertex)
        }
    },
    TRIANGLE_STRIP {
        override fun put(indexBuffer: IntBuffer, vertexBeginOffset: Int, currentVertex: Int) {
            if (currentVertex - vertexBeginOffset > 1) {
                indexBuffer
                    .put(currentVertex)
                    .put(currentVertex - 1)
                    .put(currentVertex)
            }
        }
    },
    TRIANGLE_FAN {
        override fun put(indexBuffer: IntBuffer, vertexBeginOffset: Int, currentVertex: Int) {
            if (currentVertex - vertexBeginOffset > 1) {
                indexBuffer
                    .put(vertexBeginOffset)
                    .put(currentVertex - 1)
                    .put(currentVertex)
            }
        }
    },
}

private interface Test {
    fun put(indexBuffer: IntBuffer, vertexBeginOffset: Int, currentVertex: Int)
}