package com.github.stivais.aurora.transforms.impl

import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.transforms.Transform

/**
 * # Scale
 *
 * This transformation scales an element by a provided amount.
 *
 * This class can be delegated to a float because of [Transform.Mutable].
 *
 * @param centered If scaling should scale from center or from top-left corner
 */
class Scale(
    override var amount: Float,
    private val centered: Boolean = true
) : Transform.Mutable {

    override fun apply(element: Element, renderer: Renderer) {
        scale(element, renderer, amount, amount, centered)
    }

    /**
     * # Scale.Animated
     *
     * This transformation scales an element by an amount, which can be animated.
     *
     * @param centered If scaling should scale from center or from top-left corner
     */
    class Animated(
        from: Float,
        to: Float,
        private val centered: Boolean = true
    ) : Transform.Animated(from, to) {
        override fun apply(element: Element, renderer: Renderer) {
            val amount = get()
            scale(element, renderer, amount, amount, centered)
        }
    }
}

// utility fun
private fun scale(element: Element, renderer: Renderer, amountX: Float, amountY: Float, centered: Boolean) {
    var x = element.x
    var y = element.y
    if (centered) {
        x += element.width / 2f
        y += element.height / 2f
    }
    renderer.translate(x, y)
    renderer.scale(amountX, amountY)
    renderer.translate(-x, -y)
    element.scaleX = amountX
    element.scaleY = amountY
}