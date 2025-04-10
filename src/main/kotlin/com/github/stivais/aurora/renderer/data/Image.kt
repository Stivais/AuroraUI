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
 * This class stores data for loading images to textures.
 * Image type can be a PNG or an [SVG](https://en.wikipedia.org/wiki/SVG) image.
 */
class Image private constructor(
    val identifier: String,
    val type: Type,
    val stream: InputStream,
) {

    /**
     * Loads an image from a resource path.
     *
     * @param resourcePath Path to image.
     * @param type Type of image. it will attempt to assume the type, based on the resource path
     */
    constructor(
        resourcePath: String,
        type: Type = getType(resourcePath)
    ) : this(resourcePath, type, getStream(resourcePath))

    // currently unused
    var width: Float = 0f
    var height: Float = 0f

    enum class Type {
        PNG,
        SVG,
    }

    fun buffer(): ByteBuffer {
//        when (type) {
//            Type.PNG -> {
//                val bytes
//            }
//            Type.SVG -> {}
//        }
//
        val bytes: ByteArray = stream.readBytes()
        stream.close()
        val buffer = ByteBuffer.allocateDirect(bytes.size).order(ByteOrder.nativeOrder()).put(bytes).also { it.flip() }
        return buffer
    }

    override fun hashCode() = identifier.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Image) return false
        return identifier == other.identifier
    }

    companion object {

        /**
         * Loads an [Image] from an input stream.
         *
         * Must have a unique identifier to not override other images.
         */
        fun fromInputStream(
            identifier: String,
            stream: InputStream,
            type: Type,
        ) = Image(identifier, type, stream)

        private fun getStream(path: String): InputStream {
            val trimmedPath = path.trim()
            return if (trimmedPath.startsWith("http")) {
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

        private fun getType(path: String): Type {
            return if (path.substringAfterLast('.') == "svg") Type.SVG else Type.PNG
        }
    }
}