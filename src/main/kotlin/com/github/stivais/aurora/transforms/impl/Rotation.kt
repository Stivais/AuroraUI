package com.github.stivais.aurora.transforms.impl

import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.transforms.Transform

/**
 * # Scale
 *
 * This transformation rotates an element by a provided amount.
 *
 * This class can be delegated to a float because of [Transform.Mutable].
 */
class Rotation(override var amount: Float) : Transform.Mutable {

    override fun apply(element: Element, renderer: Renderer) {
        rotate(element, renderer, amount)
    }

    /**
     * # Scale.Animated
     *
     * This transformation rotates an element by an amount, which can be animated.
     */
    class Animated(
        from: Float,
        to: Float,
    ) : Transform.Animated(from, to) {
        override fun apply(element: Element, renderer: Renderer) {
            rotate(element, renderer, get())
        }
    }
}

private fun rotate(element: Element, renderer: Renderer, amount: Float) {
    val x = element.x + element.width / 2f
    val y = element.y + element.height / 2f
    renderer.translate(x, y)
    renderer.rotate(Math.toRadians(amount.toDouble()).toFloat())
    renderer.translate(-x, -y)
}