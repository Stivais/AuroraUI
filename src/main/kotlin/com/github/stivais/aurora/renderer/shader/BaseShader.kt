package com.github.stivais.aurora.renderer.shader

import org.joml.Matrix4f
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER

abstract class BaseShader {

    abstract val programID: Int

    // idk if i should use this name or "use"
    fun start() {
        glUseProgram(programID)
    }

    fun stop() {
        glUseProgram(0)
    }

    fun cleanup() {
        glDeleteProgram(programID)
    }

    fun setMat4f(location: String, matrix: Matrix4f) {
        glUniformMatrix4fv(
            glGetUniformLocation(programID, location), false, matrix.get(MAT4_ARRAY)
        )
    }

    fun checkCompilation() {
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            println(glGetProgramInfoLog(programID, 10000))
            throw Exception("Failed to link shader program.")
        }
    }

    fun compileShader(source: String, type: Int): Int {
        val shader = glCreateShader(type)
        glShaderSource(shader, source)
        glCompileShader(shader)

        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            println(glGetShaderInfoLog(shader, 10000))
            throw Exception("Failed to compile shader ${getShaderType(type)}.")
        }
        return shader
    }

    companion object {

        private val MAT4_ARRAY = FloatArray(16)

        private fun getShaderType(type: Int) = when (type) {
            GL_VERTEX_SHADER -> "VERTEX"
            GL_FRAGMENT_SHADER -> "FRAGMENT"
            GL_COMPUTE_SHADER -> "COMPUTE"
            else -> "UNKNOWN"
        }
    }
}