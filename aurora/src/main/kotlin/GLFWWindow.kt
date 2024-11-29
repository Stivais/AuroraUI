package com.github.stivais

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.input.Keys
import com.github.stivais.aurora.input.Modifier
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
        }

        glfwSetKeyCallback(handle) { _, keycode, _, action, glfwMods ->
            if (keycode < 255 && glfwMods > 1 && action == GLFW_PRESS) {
                ui.eventManager.onKeyTyped((keycode + 32).toChar())
            }

            val key = keyMap[keycode]
            if (key != null && action == GLFW_PRESS && ui.eventManager.onKeyTyped(key)) {
                return@glfwSetKeyCallback
            }
            val mods = modifierMap[keycode]
            if (mods != null) {
                if (action == GLFW_PRESS) {
                    ui.eventManager.addModifier(mods.value)
                } else if (action == GLFW_RELEASE) {
                    ui.eventManager.removeModifier(mods.value)
                }
                return@glfwSetKeyCallback
            }
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

    private val keyMap = hashMapOf(
        GLFW_KEY_ESCAPE to Keys.ESCAPE,

        GLFW_KEY_F1 to Keys.F1,
        GLFW_KEY_F2 to Keys.F2,
        GLFW_KEY_F3 to Keys.F3,
        GLFW_KEY_F4 to Keys.F4,
        GLFW_KEY_F5 to Keys.F5,
        GLFW_KEY_F6 to Keys.F6,
        GLFW_KEY_F7 to Keys.F7,
        GLFW_KEY_F8 to Keys.F8,
        GLFW_KEY_F9 to Keys.F9,
        GLFW_KEY_F10 to Keys.F10,
        GLFW_KEY_F11 to Keys.F11,
        GLFW_KEY_F12 to Keys.F12,

        GLFW_KEY_ENTER to Keys.ENTER,
        GLFW_KEY_BACKSPACE to Keys.BACKSPACE,
        GLFW_KEY_TAB to Keys.TAB,

        GLFW_KEY_INSERT to Keys.INSERT,
        GLFW_KEY_DELETE to Keys.DELETE,
        GLFW_KEY_HOME to Keys.HOME,
        GLFW_KEY_END to Keys.END,
        GLFW_KEY_PAGE_UP to Keys.PAGE_UP,
        GLFW_KEY_PAGE_DOWN to Keys.PAGE_DOWN,

        GLFW_KEY_UP to Keys.UP,
        GLFW_KEY_DOWN to Keys.DOWN,
        GLFW_KEY_LEFT to Keys.LEFT,
        GLFW_KEY_RIGHT to Keys.RIGHT,
    )

    private val modifierMap = hashMapOf(
        GLFW_KEY_LEFT_SHIFT to Modifier.LSHIFT,
        GLFW_KEY_RIGHT_SHIFT to Modifier.RSHIFT,
        GLFW_KEY_LEFT_CONTROL to Modifier.LCTRL,
        GLFW_KEY_RIGHT_CONTROL to Modifier.RCTRL,
        GLFW_KEY_LEFT_ALT to Modifier.LALT,
        GLFW_KEY_RIGHT_ALT to Modifier.RALT,
    )
}