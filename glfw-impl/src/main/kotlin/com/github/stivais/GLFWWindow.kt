package com.github.stivais.com.github.stivais

import com.github.stivais.aurora.Aurora
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL.setCapabilities
import org.lwjgl.opengl.GL20.*
import org.lwjgl.system.MemoryUtil

class GLFWWindow(
    var width: Int,
    var height: Int,
) {

    val handle: Long

    init {
        if (!glfwInit()) throw RuntimeException("failed to initialize GLFW")
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)

        handle = glfwCreateWindow(width, height, "aurora", MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) {
            glfwTerminate()
            throw RuntimeException("failed to create the GLFW window")
        }

        glfwMakeContextCurrent(handle)
        glfwSwapInterval(0)
        createCapabilities()
    }

    inline fun openAndRun(aurora: Aurora? = null, crossinline block: () -> Unit) {
        glViewport(0, 0, width, height)
        glfwSetFramebufferSizeCallback(handle) { _, width, height ->
            glViewport(0, 0, width, height)
            this.width = width
            this.height = height
            aurora?.resize(width, height)
        }
        while (!glfwWindowShouldClose(handle)) {
            glClearColor(0.2f, 0.2f, 0.2f, 1.0f)
            glClear(GL_COLOR_BUFFER_BIT)

            glEnable(GL_ALPHA)
            glEnable(GL_BLEND)
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

            block()
            glfwSwapBuffers(handle)
            glfwPollEvents()
        }
    }

    inline fun onClick(crossinline block: () -> Unit) {
        glfwSetMouseButtonCallback(handle) { _, button, action, _ ->
            if (action == GLFW_PRESS && button == 0) {
                block()
            }
        }
    }

    fun cleanup() {
        setCapabilities(null)
        Callbacks.glfwFreeCallbacks(handle)
        glfwTerminate()
        glfwDestroyWindow(handle)
    }

    fun setTitle(string: String) {
        glfwSetWindowTitle(handle, string)
    }
}