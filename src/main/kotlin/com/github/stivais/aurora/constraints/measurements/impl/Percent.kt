package com.github.stivais.aurora.constraints.measurements.impl

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.measurements.Measurement
import com.github.stivais.aurora.element.Component

class Percent(val amount: Float) : Measurement<Constraint.Any> {
    override fun calculate(component: Component, type: Int): Float {
        val p = component.parent ?: return 0f
        return amount * p.measurement(4 - (type and 1))
    }
}