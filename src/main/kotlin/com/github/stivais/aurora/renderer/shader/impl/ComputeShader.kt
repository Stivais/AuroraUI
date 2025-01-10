package com.github.stivais.aurora.renderer.shader.impl

import com.github.stivais.aurora.renderer.shader.BaseShader
import org.lwjgl.opengl.GL43.*

class ComputeShader(source: String) : BaseShader() {

    override val programID: Int

    init {
        val shader = compileShader(source, GL_COMPUTE_SHADER)
        programID = glCreateProgram()
        glAttachShader(programID, shader)
        glLinkProgram(programID)
        checkCompilation()

        glDeleteShader(shader)
    }

    fun dispatch(x: Int, y: Int, z: Int) {
        glDispatchCompute(x, y, z)
        glMemoryBarrier(GL_SHADER_STORAGE_BARRIER_BIT)
    }
}