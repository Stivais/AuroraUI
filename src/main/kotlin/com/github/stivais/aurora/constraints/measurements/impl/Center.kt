package com.github.stivais.aurora.constraints.measurements.impl

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.measurements.Measurement
import com.github.stivais.aurora.element.Component

object Center : Measurement<Constraint.Position> {
    override fun calculate(component: Component, type: Int): Float {
        val sizeType = 4 - (type and 1)
        val size = component.measurement(sizeType)
        val parentSize = component.parent?.measurement(sizeType) ?: 0f
        return parentSize / 2f - size / 2f
    }
}