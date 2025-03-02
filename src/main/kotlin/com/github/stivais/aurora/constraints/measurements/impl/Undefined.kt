package com.github.stivais.aurora.constraints.measurements.impl

import com.github.stivais.aurora.component.Component
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.measurements.Measurement

object Undefined : Measurement<Constraint.Any> {
    override fun calculate(component: Component, type: Int): Float {
        return 0f
    }
}