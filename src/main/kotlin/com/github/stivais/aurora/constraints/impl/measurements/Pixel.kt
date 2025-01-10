package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.element.Component

@JvmInline
value class Pixel(val value: Float) : Constraint.Measurement {

    override fun calculate(component: Component, type: Int): Float {
        return value
    }

    override fun mutates(component: Component) = false

    class Mutable(var value: Float) : Constraint.Measurement {

        override fun calculate(component: Component, type: Int): Float {
            return value
        }

        override fun mutates(component: Component) = true
    }
}