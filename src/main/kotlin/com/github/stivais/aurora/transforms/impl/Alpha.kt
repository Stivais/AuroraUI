package com.github.stivais.aurora.transforms.impl

import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.transforms.Transform

/**
 * # Alpha
 *
 * This transformation changes the alpha of an element by a provided amount.
 *
 * This class can be delegated to a float because of [Transform.Mutable].
 */
class Alpha(override var amount: Float) : Transform.Mutable {
    override fun apply(element: Element, renderer: Renderer) {
        renderer.globalAlpha(amount)
    }

    /**
     * # Alpha.Animated
     *
     * This transformation changes the alpha of an element by an amount, which can be animated.
     */
    class Animated(
        from: Float,
        to: Float,
    ) : Transform.Animated(from, to) {
        override fun apply(element: Element, renderer: Renderer) {
            renderer.globalAlpha(get())
        }
    }
}