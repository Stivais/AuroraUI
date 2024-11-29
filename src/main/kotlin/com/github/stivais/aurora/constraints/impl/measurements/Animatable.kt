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
     * Current animation for this [Animatable].
     *
     * If this is null, that means it isn't animating.
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

    /**
     * Starts an animation for this [Animatable]
     *
     * If the duration is 0f, it will directly swap [from] and [to].
     *
     * @return [Animation] if duration isn't 0f
     */
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

    /**
     * Swaps [from] and [to].
     */
    fun swap() {
        val temp = to
        to = from
        from = temp
    }

    override fun toString() = "Animatable(from=\"$from\", to=\"$to\")"

    /**
     * # Animatable.Raw
     *
     * This constraint allows you to smoothly animate to any value.
     */
    class Raw(start: Float) : Constraint.Measurement {

        /**
         * Current animation for this [Animatable.Raw]
         *
         * If duration is 0 and an animation exists, it will not overwrite it.
         *
         * If this is null, that means it isn't animating.
         */
        private var animation: Animation? = null

        private var from: Float = start
        private var to: Float = 0f

        private var current: Float = start

        fun animate(to: Float, duration: Float, style: Animation.Style) {
            if (animation == null) {
                if (duration == 0f) {
                    current = to
                    from = to
                } else {
                    this.from = current
                    this.to = to
                    animation = Animation(duration, style)
                }
            } else {
                if (duration != 0f) {
                    this.from = current
                    animation!!.restart(duration, style)
                }
                this.to = to
            }
        }

        fun to(to: Float) = if (animation != null) this.to = to else current = to

        override fun calculate(element: Element, type: Int): Float {
            if (animation != null) {
                element.redraw()
                current = from + (to - from) * animation!!.get()
                if (animation!!.finished) animation = null
            }
            return current
        }

        override fun toString() = "Animatable.Raw(current=\"$current\")"
    }
}