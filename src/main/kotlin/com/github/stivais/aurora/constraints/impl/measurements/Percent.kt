package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.element.Component

class Percent(private val value: Float) : Constraint.Measurement {

    override fun calculate(component: Component, type: Int): Float {
        val p = component.parent ?: return 0f
        return value * p.getSize(type % 2 == 0)
    }

    override fun mutates(component: Component): Boolean {
        val p = component.parent ?: return false
        return p.size.width.mutates(component)
    }
}