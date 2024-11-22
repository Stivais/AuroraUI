package com.github.stivais.aurora

import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.elements.impl.Group
import com.github.stivais.aurora.events.EventManager
import com.github.stivais.aurora.events.Lifetime
import com.github.stivais.aurora.operations.Operation
import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.renderer.data.Font
import com.github.stivais.aurora.utils.loopRemoveIf

class AuroraUI(val renderer: Renderer) {

    /**
     * Marker for if this ui is initialized,
     * to prevent [initialize] and [close] being run multiple times in a row.
     */
    private var initialized = false

    /**
     * The root element, all rendering and events start from here.
     */
    val main = Group(Constraints(0.px, 0.px, 0.px, 0.px)).also { it.ui = this }

    /**
     * Handles [events][com.github.stivais.aurora.events.AuroraEvent] and Input.
     */
    val eventManager = EventManager(this)

    /**
     * Gets [EventManager.mouseX]. Used to make code nicer.
     */
    inline val mx: Float
        get() = eventManager.mouseX

    /**
     * Gets [EventManager.mouseY]. Used to make code nicer.
     */
    inline val my: Float
        get() = eventManager.mouseY

    /**
     * List of [Operations][Operation], that get ran in [render].
     *
     * @see Operation
     */
    private var operations = arrayListOf<Operation>()

    /**
     * Sets up this UIs width and height, and initialize all the initial elements.
     *
     * If [initialized] is true it will not do anything.
     */
    fun initialize(width: Int, height: Int) {
        if (!initialized) {
            initialized = true
            main.constraints.width = width.px
            main.constraints.height = height.px

            main.initialize()
            main.size()
            main.positionChildren()
            main.clip()
        }
    }

    /**
     * Main function for the [UI][AuroraUI].
     *
     * Renders all the elements to the screen.
     */
    fun render() {
        operations.loopRemoveIf {
            it.run()
        }
        renderer.beginFrame(main.width, main.height)
        renderer.push()
        main.render()
        renderer.pop()
        renderer.endFrame()
    }

    /**
     * Posts [Lifetime.Uninitialized] to all elements.
     *
     * Use this whenever your UI is closed.
     *
     * If [initialized] is true it will do nothing.
     */
    fun close() {
        if (initialized) {
            initialized = false
            eventManager.postToAll(Lifetime.Uninitialized, main)
        }
    }

    /**
     * Resizes the UI, so it can match window's width and height.
     */
    fun resize(width: Int, height: Int) {
        main.constraints.width = width.px
        main.constraints.height = height.px
        main.redraw()
    }

    fun addOperation(operation: Operation) {
        operations.add(operation)
    }

    companion object {
        /**
         * Default font provided by Aurora.
         */
        @JvmField
        val defaultFont = Font("Aurora", "/assets/aurora/fonts/AuroraDefault.otf")
    }
}