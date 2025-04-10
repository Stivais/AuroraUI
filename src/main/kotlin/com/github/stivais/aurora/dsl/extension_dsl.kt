@file:Suppress("unused")

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.impl.measurements.Pixel
import com.github.stivais.aurora.components.Component
import com.github.stivais.aurora.components.scope.ComponentScope
import com.github.stivais.aurora.components.impl.Block
import com.github.stivais.aurora.utils.color
import com.github.stivais.aurora.utils.multiply

/**
 * Gets ran when dragging down on an element.
 *
 * If you need percents based on mouse position in element, use [onMouseDrag].
 *
 * Note: For it to be considered dragging, the element must be hovered when clicked.
 *
 * @param block Function to get ran.
 */
inline fun ComponentScope<*>.onDrag(button: Int = 0, crossinline block: () -> Boolean) {
    var pressed = false
    onClick(button) {
        pressed = true
        block.invoke()
    }
    onRelease(button) {
        pressed = false
    }
    onMouseMove {
        if (pressed) block.invoke() else false
    }
}

/**
 * Gets ran when dragging down on an element.
 *
 * If you don't need percents based on mouse position in element, use [onDrag].
 *
 * @param block Function to get ran. Parameters represent percentage-based position of the mouse when dragged.
 */
inline fun ComponentScope<*>.onMouseDrag(crossinline block: (x: Float, y: Float) -> Boolean) {
    onDrag {
        block(
            ((ui.mx - component.x) / component.width).coerceIn(0f, 1f),
            ((ui.my - component.y) / component.height).coerceIn(0f, 1f),
        )
    }
}

/**
 * Extension, which allows for an element to be able to be dragged around.
 *
 * It will never be able to be dragged outside the main element.
 *
 * This changes the element's position constraints to be [Pixel] after initialization,
 * this is so it can be easily mutable.
 *
 * It also brings the element to the top when dragged.
 */
fun ComponentScope<*>.draggable(
    button: Int = 0,
    moves: Component = component,
    coerce: Boolean = true,
) {
    var initialized = false
    val px: Pixel = 0.px
    val py: Pixel = 0.px

    var clickedX = 0f
    var clickedY = 0f

    onClick(button) {
        if (!initialized) {
            initialized = true
            px.pixels = moves.internalX
            py.pixels = moves.internalY
            moves.constraints.x = px
            moves.constraints.y = py
        }
        clickedX = ui.mx - moves.internalX
        clickedY = ui.my - moves.internalY
        moves.moveToTop()
    }
    onDrag(button) {
        var newX = ui.mx - clickedX
        var newY = ui.my - clickedY
        if (coerce) {
            newX = newX.coerceIn(0f, (ui.main.width - moves.width))
            newY = newY.coerceIn(0f, (ui.main.height - moves.height))
        }
        px.pixels = newX
        py.pixels = newY
        redraw()
        false
    }
}

/**
 * Extension for [Block],
 * where the color gets darkened whenever the mouse is hovering over the element.
 *
 * It will mutate the color to a [Color.Animated],
 * where first color is the original color, and the second is a darker version.
 */
fun ComponentScope<Block>.hoverEffect(
    factor: Float,
    duration: Float = 0.2.seconds,
    style: Animation.Style = Animation.Style.Linear
) {
    val before = component.color!!
    val hover = Color.Animated(from = before, to = color { before.rgba.multiply(factor = factor) })
    component.color = hover
    onMouseEnterExit { hover.animate(duration, style) }
}