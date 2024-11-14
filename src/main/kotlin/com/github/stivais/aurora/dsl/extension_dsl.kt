package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.constraints.impl.measurements.Pixel
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.utils.log

/**
 * Gets ran when dragging down on an element.
 *
 * Note: For it to be considered dragging, the element must be hovered when clicked.
 *
 * @param block Function to get ran. Parameters represent percentage-based position of the mouse when dragged
 */
inline fun ElementScope<*>.onDrag(crossinline block: (x: Float, y: Float) -> Unit) {
    var pressed = false
    onClick {
        pressed = true
        block(
            ((ui.mx - element.x) / element.width).coerceIn(0f, 1f),
            ((ui.my - element.y) / element.height).coerceIn(0f, 1f),
        )
    }
    onRelease {
        pressed = false
    }
    onMouseMove {
        if (pressed) {
            block(
                ((ui.mx - element.x) / element.width).coerceIn(0f, 1f),
                ((ui.my - element.y) / element.height).coerceIn(0f, 1f),
            )
        }
    }
}

/**
 * Extension, which allows for an element to be able to be dragged around.
 *
 * It will never be able to be dragged outside the main element.
 *
 * This changes the element's position constraints to be [Pixel] after initialization,
 * this is so it can be easily mutable
 */
fun ElementScope<*>.draggable(
    button: Int = 0,
    moves: Element = element
) {
    val px: Pixel = 0.px
    val py: Pixel = 0.px

    moves.log()

    onAdd {
        px.pixels = moves.internalX
        py.pixels = moves.internalY
        moves.constraints.x = px
        moves.constraints.y = py
    }

    var clickedX = 0f
    var clickedY = 0f

    onClick(button) {
        clickedX = ui.mx - moves.internalX
        clickedY = ui.my - moves.internalY
    }
    onDrag { _, _ ->
        px.pixels = (ui.mx - clickedX).coerceIn(0f, ui.main.width - moves.width).log()
        py.pixels = (ui.my - clickedY).coerceIn(0f, ui.main.height - moves.height).log()
        element.redraw()
    }
}