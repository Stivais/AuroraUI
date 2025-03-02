package com.github.stivais.aurora.constraints.measurements.impl

import com.github.stivais.aurora.animation.Animation
import com.github.stivais.aurora.component.Component
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.measurements.Measurement
import com.github.stivais.aurora.utils.Timing

class Animatable<E : Constraint>(
    private var from: Measurement<E>,
    private var to: Measurement<E>,
) : Measurement<E> {

    var animation: Animation? = null
        private set

    private var current: Float = 0f

    private var before: Float? = null

    override fun calculate(component: Component, type: Int): Float {
        val anim = animation
        if (anim != null) {
            component.parent?.redraw = true

            val progress = anim.get()
            val from = before ?: from.calculate(component, type)
            current = from + (to.calculate(component, type) - from) * progress

            if (anim.finished) {
                animation = null
                before = null
                swap()
            }
            return current
        }
        return from.calculate(component, type)
    }

    fun animate(duration: Timing, style: Animation.Style): Animation? {
        val d = duration.value
        if (d <= 0f) {
            swap()
            animation = null
            return null
        }

        val anim = animation
        if (anim != null) {
            swap()
            before = current
            anim.restart(d, style)
        } else {
            animation = Animation(d, style)
        }
        return animation
    }

    fun swap() {
        val temp = to
        to = from
        from = temp
    }
}