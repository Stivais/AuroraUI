package com.github.stivais.aurora.renderer.data

import com.github.stivais.aurora.utils.setupConnection
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.file.Files

/**
 * # Image
 *
 * This class stores data for an image.
 *
 * The image type can be either raster (e.g. PNG) or vector (e.g. SVG).
 *
 * The image can be created either from:
 * - A resource path (file path, classpath resource, or HTTP URL)
 * - Directly from an InputStream
 *
 * @param resourcePath can be an url to a file, or to retrieve data on the internet (http)
 */
class Image {
    val resourcePath: String
    val type: Type

    // currently unused
    var width: Float = 0f
    var height: Float = 0f

    /**
     * Input stream for this image
     */
    val stream: InputStream

    constructor(
        resourcePath: String,
        type: Type = getType(resourcePath)
    ) {
        this.resourcePath = resourcePath
        this.type = type

        val trimmedPath = resourcePath.trim()
        stream = if (trimmedPath.startsWith("http")) {
            setupConnection(trimmedPath)
        } else {
            val file = File(trimmedPath)
            if (file.exists() && file.isFile) {
                Files.newInputStream(file.toPath())
            } else {
                this::class.java.getResourceAsStream(trimmedPath) ?: throw FileNotFoundException(trimmedPath)
            }
        }
    }

    constructor(inputStream: InputStream, type: Type = Type.RASTER) {
        this.resourcePath = "direct-stream"
        this.type = type
        this.stream = inputStream
    }

    /**
     * [Image] type.
     *
     * Raster represents formats like PNG.
     * Vector represents the SVG format.
     */
    enum class Type {
        RASTER,
        VECTOR
    }

    override fun hashCode() = resourcePath.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Image) return false
        return resourcePath == other.resourcePath
    }

    /**
     * Gets a bytebuffer, based on the [input stream][stream]
     */
    fun buffer(): ByteBuffer {
        val bytes: ByteArray = stream.readBytes()
        stream.close()
        val buffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder()).put(bytes).also { it.flip() }
        return buffer
    }

    companion object {
        private fun getType(path: String): Type {
            return if (path.substringAfterLast('.') == "svg") Type.VECTOR else Type.RASTER
        }
    }
}