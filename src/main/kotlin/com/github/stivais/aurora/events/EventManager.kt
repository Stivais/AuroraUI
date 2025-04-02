@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.stivais.aurora.events

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.input.Keys
import com.github.stivais.aurora.input.Modifier
import com.github.stivais.aurora.utils.loop
import com.github.stivais.aurora.utils.reverseLoop
import kotlin.math.sign

/**
 * # EventManager
 *
 * The EventManager handles dispatching events to elements within the ui.
 * It mainly handles input-related events, such as [Mouse] and [Keyboard].
 *
 */
class EventManager(private val ui: AuroraUI) {

    var mouseX: Float = 0f
    /** Mouse's current y position. */

    var mouseY: Float = 0f
    /** Flag if mouse is currently down.*/
    var mouseDown: Boolean = false

    /**
     * Current modifiers active.
     * (CTRL, SHIFT, ALT)
     */
    var modifier = Modifier(0)

    /**
     * Current top-most hovered element, which can accept input.
     */
    private var hoveredElement: Element? = null

    /**
     * Current focused element inside a UI.
     */
    var focused: Element? = null
        set(value) {
            if (field == value) return
            field?.acceptFocused(Focused.Lost)
            value?.acceptFocused(Focused.Gained)
            field = value
        }

    /**
     * Used when mouse is moved.
     */
    fun onMouseMove(x: Float, y: Float) {
        if (mouseX == x && mouseY == y) return
        mouseX = x
        mouseY = y
        hoveredElement = getHovered(x, y)
        postToAll(Mouse.Moved)
    }

    /**
     * Used when mouse is clicked.
     *
     * It first dispatches [Mouse.Clicked.NonSpecific], then [Mouse.Clicked].
     *
     * When there isn't a focused element,
     * it will dispatch it as a bubbling event from [hoveredElement].
     *
     * When there is a focused element,
     * if the mouse is outside it, it will unfocus it, otherwise it will dispatch the event to it.
     */
    fun onMouseClick(button: Int): Boolean {
        mouseDown = true
        val eventNS = Mouse.Clicked.NonSpecific(button)
        val event = Mouse.Clicked(button)

        if (focused != null) {
            if (focused!!.isInside(mouseX, mouseY)) {
                if (focused!!.accept(eventNS)) {
                    return true
                }
                return focused?.accept(event) ?: false
            } else {
                focused = null
            }
        }
        return post(eventNS) || post(event)
    }

    /**
     * Used when mouse is released.
     *
     * Dispatches [Mouse.Released] to all elements.
     */
    fun onMouseRelease(button: Int) {
        mouseDown = false
        postToAll(Mouse.Released(button))
    }

    /**
     * Used when mouse is scrolled.
     *
     * Note: Amount is always -1, 0, or 1.
     *
     * Dispatches [Mouse.Scrolled] (as a bubbling event) to the [hoveredElement].
     */
    fun onMouseScroll(amount: Float): Boolean {
        return post(Mouse.Scrolled(amount.sign))
    }

    /**
     * Used when a key, representable by a char, gets pressed.
     *
     * Dispatches [Keyboard.CharTyped] to the focused element.
     */
    fun onKeyTyped(char: Char): Boolean {
        if (focused != null) {
            return focused!!.acceptFocused(Keyboard.CharTyped(char, modifier))
        }
        return false
    }

    /**
     * Used when a key, not representable by a char, gets pressed.
     *
     * Dispatches [Keyboard.KeyTyped] to the focused element.
     */
    fun onKeyTyped(key: Keys): Boolean {
        if (focused != null) {
            return focused!!.acceptFocused(Keyboard.KeyTyped(key, modifier))
        }
        return false
    }

    /**
     * Used when a key gets pressed, representing a keycode.
     *
     * Dispatches [Keyboard.CodeTyped] to the focused element.
     */
    fun onKeycodePressed(code: Int): Boolean {
        if (focused != null) {
            return  focused!!.acceptFocused(Keyboard.CodeTyped(code, modifier))
        }
        return false
    }

    /**
     * Used when any [keyboard modifier][Modifier] is pressed. (CTRL, SHIFT, ALT)
     */
    fun addModifier(mods: Byte) {
        modifier = Modifier((modifier.value.toInt() or mods.toInt()).toByte())
    }

    /**
     * Used when any [keyboard modifier][Modifier] is released. (CTRL, SHIFT, ALT)
     */
    fun removeModifier(mods: Byte) {
        modifier = Modifier((modifier.value.toInt() and mods.toInt().inv()).toByte())
    }

    /**
     * Dispatches a bubbling event,
     * starting from the specified element (by default it is [hoveredElement]).
     *
     * A [bubbling event](https://en.wikipedia.org/wiki/Event_bubbling) starts from an element,
     * and climbs up it's hierarchy until it is consumed.
     */
    fun post(event: AuroraEvent, element: Element? = hoveredElement): Boolean {
        var current = element
        while (current != null) {
            if (current.accept(event)) {
                return true
            }
            current = current.parent
        }
        return false
    }

    /**
     * Dispatches an event to every single element inside a UI.
     */
    fun postToAll(event: AuroraEvent, element: Element = ui.main) {
        element.accept(event)
        element.children?.loop {
            postToAll(event, it)
        }
    }

    /**
     * Forces a recalculation of [hoveredElement], in case it no longer is hovered.
     */
    fun recalculate() {
        hoveredElement = getHovered(mouseX, mouseY)
    }

    private fun getHovered(x: Float, y: Float, element: Element = ui.main): Element? {
        var result: Element? = null
        if (element.renders && element.isInside(x, y)) {
            element.children?.reverseLoop { it ->
                if (result == null) {
                    getHovered(x, y, it)?.let {
                        result = it
                        return@reverseLoop // prevent discarding hovered
                    }
                }
                unmarkHovered(it)
            }
            if (element.acceptsInput) {
                element.hovered = true
                if (result == null) result = element
            }
        }
        return result
    }

    private fun unmarkHovered(element: Element) {
        // early exit, since if it acceptsInput but isn't hovered,
        // means that any element under this one will never be hovered
        if (!element.hovered && element.acceptsInput) return
        element.hovered = false
        element.children?.loop { unmarkHovered(it) }
    }
}