package com.github.stivais.aurora.engine.renderer.shader

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20.*
import java.io.FileNotFoundException

// maybe custom handling of uniforms,
// however I do want to keep it as low abstractions on the render side
// as possible
class Shader(
    vertexSource: String,
    fragmentSource: String
) {

    private val programID: Int

    init {
        val vertex = compileShader(vertexSource, GL_VERTEX_SHADER)
        val fragment = compileShader(fragmentSource, GL_FRAGMENT_SHADER)

        programID = glCreateProgram()
        glAttachShader(programID, vertex)
        glAttachShader(programID, fragment)
        glLinkProgram(programID)

        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            println(glGetProgramInfoLog(programID, 10_000))
            throw RuntimeException("Failed to link shader program.")
        }

        glDeleteShader(vertex)
        glDeleteShader(fragment)
    }

    fun bind() {
        glUseProgram(programID)
    }

    fun unbind() {
        glUseProgram(0)
    }

    fun delete() {
        glDeleteProgram(programID)
    }

    fun setMat4f(location: String, mat4f: Matrix4f) {
        glUniformMatrix4fv(glGetUniformLocation(programID, location), false, mat4f.get(MAT4_ARRAY))
    }

    private fun compileShader(source: String, type: Int): Int {
        val shader = glCreateShader(type)
        glShaderSource(shader, source)
        glCompileShader(shader)

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            println(glGetShaderInfoLog(shader, 10_000))
            throw RuntimeException("Failed to compile shader ${when (type) {
                GL_VERTEX_SHADER -> "VERTEX"
                GL_FRAGMENT_SHADER -> "FRAGMENT"
                else -> "INVALID"
            }}")
        }
        return shader
    }

    companion object {

        private val MAT4_ARRAY = FloatArray(16)

        fun fromFile(
            vertexPath: String,
            fragmentPath: String,
        ): Shader {
            return Shader(
                readFile(vertexPath),
                readFile(fragmentPath),
            )
        }

        private fun readFile(path: String): String {
            return this::class.java.getResourceAsStream(path)?.reader()?.readText() ?: throw FileNotFoundException("$path doesn't exist.")
        }
    }
}