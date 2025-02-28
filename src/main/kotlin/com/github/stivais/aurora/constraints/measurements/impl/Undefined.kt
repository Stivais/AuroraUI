package com.github.stivais.aurora.constraints.measurements.impl

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.measurements.Measurement
import com.github.stivais.aurora.element.Component

object Undefined : Measurement<Constraint.Any> {
    override fun calculate(component: Component, type: Int): Float {
        return 0f
    }
}