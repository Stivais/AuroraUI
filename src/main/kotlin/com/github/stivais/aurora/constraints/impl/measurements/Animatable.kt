@file:Suppress("unused")

package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.elements.Element

/**
 * # Animatable
 *
 * This constraint helps implement animations that go between 2 different points back and forth.
 */
class Animatable(
    private var from: Constraint,
    private var to: Constraint,
) : Constraint.Measurement {

    constructor(from: Constraint, to: Constraint, swapIf: Boolean) : this(from, to,) {
        if (swapIf) {
            swap()
        }
    }

    /**
     * Current animation for this [Animatable]
     *
     * If this is null, that means it isn't animating
     */
    var animation: Animation? = null
        private set

    private var current: Float = 0f

    // acts as from, if it started animating during an existing one, to prevent snapping
    private var before: Float? = null

    override fun calculate(element: Element, type: Int): Float {
        if (animation != null) {
            element.redraw()
            val progress = animation!!.get()
            val from = before ?: from.calculate(element, type)
            current = from + (to.calculate(element, type) - from) * progress

            if (animation!!.finished) {
                animation = null
                before = null
                swap()
            }
            return current
        }
        return from.calculate(element, type)
    }

    override fun reliesOnChildren() = from.reliesOnChildren() || to.reliesOnChildren()

    fun animate(duration: Float, style: Animation.Style): Animation? {
        if (duration == 0f) {
            swap()
            return null
        }

        animation = if (animation != null) {
            before = current
            swap()
            Animation(duration * animation!!.get(), style)
        } else {
            Animation(duration, style)
        }
        return animation
    }

    fun swap() {
        val temp = to
        to = from
        from = temp
    }

    override fun toString() = "Animatable(from=\"$from\", to=\"$to\")"
}