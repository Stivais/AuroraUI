package com.github.stivais.aurora.renderer.shader.impl

import com.github.stivais.aurora.renderer.shader.BaseShader
import org.lwjgl.opengl.GL20.*

class Shader(vertexSource: String, fragmentSource: String) : BaseShader() {

    override val programID: Int

    init {
        val vertexShader = compileShader(vertexSource, GL_VERTEX_SHADER)
        val fragmentShader = compileShader(fragmentSource, GL_FRAGMENT_SHADER)

        programID = glCreateProgram()
        glAttachShader(programID, vertexShader)
        glAttachShader(programID, fragmentShader)
        glLinkProgram(programID)
        checkCompilation()

        glDeleteShader(vertexShader)
        glDeleteShader(fragmentShader)
    }

    companion object {
        // in here since BaseShader looks ugly imo

        fun create(
            vertexPath: String,
            fragmentPath: String
        ): Shader {
            return Shader(
                loadShaderCode(vertexPath),
                loadShaderCode(fragmentPath)
            )
        }

        fun create(
            computePath: String
        ): ComputeShader {
            return ComputeShader(
                loadShaderCode(computePath)
            )
        }

        private fun loadShaderCode(filePath: String): String {
            val inputStream = Thread.currentThread().contextClassLoader.getResourceAsStream(filePath) ?: throw IllegalArgumentException("Shader file not found: $filePath")
            return inputStream.bufferedReader().use { it.readText() }
        }
    }
}