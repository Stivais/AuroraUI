package com.github.stivais

import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL.setCapabilities
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryUtil

class GLFWWindow(
    width: Int,
    height: Int,
) {

    val handle: Long

    init {
        if (!glfwInit()) throw RuntimeException("failed to initialize GLFW")
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)

        handle = glfwCreateWindow(width, height, "aurora", MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) {
            glfwTerminate()
            throw RuntimeException("failed to create the GLFW window")
        }

        glfwMakeContextCurrent(handle)
        createCapabilities()
    }

    inline fun openAndRun(crossinline block: () -> Unit) {
        while (!glfwWindowShouldClose(handle)) {
            glClearColor(1f, 1f, 1f, 1.0f)
            glClear(GL_COLOR_BUFFER_BIT)
            block()
            glfwSwapBuffers(handle)
            glfwPollEvents()
        }
    }

    fun cleanup() {
        setCapabilities(null)
        Callbacks.glfwFreeCallbacks(handle)
        glfwTerminate()
        glfwDestroyWindow(handle)
    }
}