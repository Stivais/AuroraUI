package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.element.Component

object Undefined : Constraint.Measurement {

    override fun calculate(component: Component, type: Int): Float {
        return 0f
    }

    override fun mutates(component: Component): Boolean {
        return false
    }
}