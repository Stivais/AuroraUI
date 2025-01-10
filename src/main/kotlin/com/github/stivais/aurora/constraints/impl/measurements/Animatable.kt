package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.animation.Animation
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.element.Component

class Animatable(
    private var from: Constraint,
    private var to: Constraint
) : Constraint.Measurement {

    var animation: Animation? = null
        private set

    private var current: Float = 0f

    private var before: Float? = null

    override fun calculate(component: Component, type: Int): Float {
        if (animation != null) {
            // todo: something better
            component.parent?.redraw = true

            val progress = animation!!.get()
            val from = before ?: from.calculate(component, type)
            current = from + (to.calculate(component, type) - from) * progress

            if (animation!!.finished) {
                animation = null
                before = null
                swap()
            }
            return current
        }
        return from.calculate(component, type)
    }

    fun animate(duration: Float, style: Animation.Style): Animation? {
        if (duration <= 0f) {
            swap()
            animation = null
            return null
        }
        if (animation != null) {
            swap()
            before = current
            animation!!.restart(duration, style)
        } else {
            animation = Animation(duration, style)
        }
        return animation
    }

    fun swap() {
        val temp = to
        to = from
        from = temp
    }

    override fun mutates(component: Component) = true
    
}