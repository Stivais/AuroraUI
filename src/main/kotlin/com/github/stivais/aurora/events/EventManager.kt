package com.github.stivais.aurora.events

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.utils.loop
import com.github.stivais.aurora.utils.reverseLoop
import kotlin.math.sign

class EventManager(private val ui: AuroraUI) {

    var mouseX: Float = 0f
    var mouseY: Float = 0f
    var mouseDown: Boolean = false

    private var hoveredElement: Element? = null

    var focused: Element? = null
        set(value) {
            if (field == value) return
            field?.acceptFocused(Focused.Lost)
            value?.acceptFocused(Focused.Gained)
            field = value
        }

    fun onMouseMove(x: Float, y: Float) {
        mouseX = x
        mouseY = y
        hoveredElement = getHovered(x, y)
        postToAll(Mouse.Moved)
    }

    fun onMouseClick(button: Int): Boolean {
        mouseDown = true
        val event = Mouse.Clicked(button)

        if (focused != null) {
            if (focused!!.isInside(mouseX, mouseY)) {
                return focused!!.accept(event)
            } else {
                focused = null
            }
        }
        return post(event)
    }

    fun onMouseRelease(button: Int) {
        mouseDown = false
        postToAll(Mouse.Released(button))
    }

    fun onMouseScroll(amount: Float): Boolean {
        return post(Mouse.Scrolled(amount.sign))
    }


    fun onKeyTyped(char: Char): Boolean {
        if (focused != null) {
            focused!!.acceptFocused(Keyboard.CharTyped(char))
        }
        return false
    }


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

    fun postToAll(event: AuroraEvent, element: Element = ui.main) {
        element.accept(event)
        element.children?.loop {
            postToAll(event, it)
        }
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