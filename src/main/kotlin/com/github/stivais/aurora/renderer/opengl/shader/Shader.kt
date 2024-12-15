package com.github.stivais.aurora.renderer.opengl.shader

import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL45

// todo: allow to take from file, and also reload support
class Shader(
    vertexSource: String,
    fragmentSource: String
) {

    private val programID: Int

    init {
        val vertexShader = compileShader(vertexSource, GL_VERTEX_SHADER)
        val fragmentShader = compileShader(fragmentSource, GL_FRAGMENT_SHADER)
        programID = glCreateProgram()
        glAttachShader(programID, vertexShader)
        glAttachShader(programID, fragmentShader)
        glLinkProgram(programID)

        if (GL45.glGetProgrami(programID, GL45.GL_LINK_STATUS) == GL45.GL_FALSE) {
            println(glGetProgramInfoLog(programID, 10000))
            throw Exception("Failed to link shader program.")
        }
    }

    fun use() {
        glUseProgram(programID)
    }

    private fun compileShader(source: String, type: Int): Int {
        val shader = glCreateShader(type)
        glShaderSource(shader, source)
        glCompileShader(shader)

        if (GL45.glGetShaderi(shader, GL45.GL_COMPILE_STATUS) == GL45.GL_FALSE) {
            println(glGetShaderInfoLog(shader, 10000))
            throw Exception("Failed to compile shader ${getShaderType(type)}.")
        }
        return shader
    }

    private fun getShaderType(type: Int) = when (type) {
        GL_VERTEX_SHADER -> "VERTEX"
        GL_FRAGMENT_SHADER -> "FRAGMENT"
        else -> "UNKNOWN"
    }
}