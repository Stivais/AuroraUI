package com.github.stivais.aurora.renderer.data

import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * # Font
 *
 * This class stores data about a font file, for example a ttf file.
 *
 * Its usage is heavily dependent on the [Renderer Implementation][com.github.stivais.aurora.renderer.Renderer]
 *
 * @param name Used to identify the font.
 * @param resourcePath Path to the file.
 */
class Font(val name: String, val resourcePath: String) {

    // this must stay allocated
    val buffer: ByteBuffer

    init {
        val stream = this::class.java.getResourceAsStream(resourcePath) ?: throw FileNotFoundException(resourcePath)
        val bytes = stream.readBytes()
        stream.close()
        buffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder()).put(bytes)
        buffer.flip()
    }

    override fun hashCode(): Int {
        return resourcePath.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Font) return false
        return other.resourcePath == resourcePath
    }
}