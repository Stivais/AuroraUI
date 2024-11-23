@file:Suppress("unused")

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.impl.measurements.Pixel
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.elements.impl.Block
import com.github.stivais.aurora.utils.color
import com.github.stivais.aurora.utils.multiply

/**
 * Gets ran when dragging down on an element.
 *
 * Note: For it to be considered dragging, the element must be hovered when clicked.
 *
 * @param block Function to get ran. Parameters represent percentage-based position of the mouse when dragged
 */
inline fun ElementScope<*>.onDrag(crossinline block: () -> Unit) {
    var pressed = false
    onClick {
        pressed = true
        block.invoke()
    }
    onRelease {
        pressed = false
    }
    onMouseMove {
        if (pressed) {
            block.invoke()
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
    moves: Element = element,
    coerce: Boolean = true,
) {
    val px: Pixel = 0.px
    val py: Pixel = 0.px

    onAdd {
        moves.size()
        moves.positionChildren()
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
    onDrag {
        var newX = ui.mx - clickedX
        var newY = ui.my - clickedY
        if (coerce) {
            newX = newX.coerceIn(0f, (ui.main.width - moves.width))
            newY = newY.coerceIn(0f, (ui.main.height - moves.height))
        }
        px.pixels = newX
        py.pixels = newY
        element.redraw()
    }
}

/**
 * Extension for [Block],
 * where the color gets darkened whenever the mouse is hovering over the element.
 *
 * It will mutate the color to a [Color.Animated],
 * where first color is the original color, and the second is a darker version.
 */
fun ElementScope<Block>.hoverEffect(
    factor: Float,
    duration: Float = 0.2.seconds,
    style: Animation.Style = Animation.Style.EaseOutQuad
) {
    val before = element.color!!
    val hover = Color.Animated(from = before, to = color { before.rgba.multiply(factor = factor) })
    element.color = hover
    onMouseEnterExit { hover.animate(duration, style) }
}