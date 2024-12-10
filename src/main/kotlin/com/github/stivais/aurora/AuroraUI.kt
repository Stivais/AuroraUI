package com.github.stivais.aurora

import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.impl.Group
import com.github.stivais.aurora.events.EventManager
import com.github.stivais.aurora.events.Lifetime
import com.github.stivais.aurora.operations.Operation
import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.renderer.data.Font
import com.github.stivais.aurora.utils.loopRemoveIf

/**
 * # AuroraUI
 *
 * AuroraUI is a declarative UI framework, utilizing [Kotlin's DSL features](https://kotlinlang.org/docs/type-safe-builders.html)
 * to create intuitive user interfaces, while simplifying the development process.
 *
 * ## Elements
 *
 * AuroraUI is built around **[Elements][Element]**, they are hierarchical components, which render to the screen.
 *
 * They are positioned and sized by **[Constraints][com.github.stivais.aurora.constraints.Constraint]**,
 * they define the x, y, width and height of an element.
 *
 * Elements can also be positioned by **[Layouts][com.github.stivais.aurora.elements.Layout]**.
 * They're a type of element, such as [Column][com.github.stivais.aurora.elements.impl.layout.Column],
 * or [Row][com.github.stivais.aurora.elements.impl.layout.Row], which position its elements.
 *
 * ## Events
 *
 * **[Events][com.github.stivais.aurora.events.AuroraEvent]** are interaction or occurrences
 * that are processed by AuroraUIs [EventManager].
 *
 * Most common type of events are inputs, such as [Mouse Events][com.github.stivais.aurora.events.Mouse]
 * or [Keyboard Events][com.github.stivais.aurora.events.Keyboard].
 *
 * ## Implementation
 *
 * You need to implement everything on [Window].
 */
class AuroraUI(val renderer: Renderer) {

    /**
     * Reference to the window rendering this UI.
     *
     * Can also be used to see if a UI is initialized.
     */
    var window: Window? = null

    /**
     * The root element, all rendering and events start from here.
     */
    val main = Group(Constraints(0.px, 0.px, 0.px, 0.px)).also { it.ui = this }

    /**
     * Handles [events][com.github.stivais.aurora.events.AuroraEvent] and Input.
     */
    val eventManager = EventManager(this)

    /**
     * Flag for when [eventManager] needs to recalculate hovered elements.
     */
    var recalculateMouse = false

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
     * Backing property for [Window.getClipboard] and [Window.setClipboard].
     */
    inline var clipboard: String?
        get() = window?.getClipboard()
        set(value) {
            window?.setClipboard(value)
        }

    /**
     * List of [Operations][Operation], that get ran in [render].
     *
     * @see Operation
     */
    private var operations = arrayListOf<Operation>()

    /**
     * Sets up this UIs width and height, and initialize all the initial elements.
     */
    fun initialize(width: Int, height: Int, window: Window) {
        this.window = window

        main.constraints.width = width.px
        main.constraints.height = height.px

        main.size()
        main.positionChildren()
        main.clip()
        main.initialize()
    }

    /**
     * Main function for the [UI][AuroraUI].
     *
     * Renders all the elements to the screen.
     */
    fun render() {
        if (recalculateMouse) {
            eventManager.recalculate()
            recalculateMouse = false
        }
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
     */
    fun close() {
        if (window != null) {
            window = null
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

    fun focus(element: Element) {
        eventManager.focused = element
    }

    fun unfocus() {
        eventManager.focused = null
    }

    companion object {
        /**
         * Default font provided by Aurora.
         */
        @JvmField
        val defaultFont = Font("Aurora", "/assets/aurora/fonts/AuroraDefault.otf")
    }
}