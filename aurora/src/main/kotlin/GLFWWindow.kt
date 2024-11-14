package com.github.stivais

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.utils.log
import org.lwjgl.glfw.Callbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL.setCapabilities
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil

class GLFWWindow(
    title: String,
    width: Int,
    height: Int,
) {

    private var width = width
    private var height = height
        set(value) {
            val h = IntArray(1)
            glfwGetFramebufferSize(handle, null, h)
            offset = h[0] - value
            field = value
        }

    private val handle: Long

    private var offset = 0

    init {
        glfwSetErrorCallback { code, desc ->
            val stack = Thread.currentThread().stackTrace.drop(4).joinToString("\n\t at ")
            println("($code): ${GLFWErrorCallback.getDescription(desc)}\nStack: $stack")
        }
        if (!glfwInit()) throw RuntimeException("Failed init glfw")
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        handle = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL)
        if (handle == MemoryUtil.NULL) {
            glfwTerminate()
            throw RuntimeException("Failed to create the GLFW wndow")
        }

        glfwMakeContextCurrent(handle)
        createCapabilities()
    }

    private fun callbacks(ui: AuroraUI) {
        glfwSetFramebufferSizeCallback(handle) { _, width, height ->
            this.width = width
            this.height = height
            ui.resize(width, height)
        }
        glfwSetCursorPosCallback(handle) { _, x, y ->
            ui.eventManager.onMouseMove(x.toFloat(), y.toFloat())
        }
        glfwSetMouseButtonCallback(handle) { _, button, action, _ ->
            if (action == GLFW_PRESS) {
                ui.eventManager.onMouseClick(button)
            } else if (action == GLFW_RELEASE) {
                ui.eventManager.onMouseRelease(button)
            }
        }
        glfwSetScrollCallback(handle) { _, x, y ->
            ui.eventManager.onMouseScroll(y.toFloat())
        }

        glfwSetCharCallback(handle) { _, char ->
            ui.eventManager.onKeyTyped(char.toChar())
//            println("char ${char.toChar()} char code $char")
        }

        glfwSetKeyCallback(handle) { _, key, _, action, mods ->
//            println(key)
//            GLFW_KEY_F10
        }
    }

    fun open(ui: AuroraUI) {
        callbacks(ui)
        ui.initialize(width, height)
        while (!glfwWindowShouldClose(handle)) {
            glViewport(0, 0, width, height)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glClearColor(0f, 0f, 0f, 0f)

            ui.render()

            glfwSwapBuffers(handle)
            glfwPollEvents()
        }
        setCapabilities(null)
        glfwSetErrorCallback(null)?.free()
        Callbacks.glfwFreeCallbacks(handle)
        glfwTerminate()
        glfwDestroyWindow(handle)
    }
}