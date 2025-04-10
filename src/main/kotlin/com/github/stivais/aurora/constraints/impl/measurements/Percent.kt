package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component

/**
 * # Percent
 *
 * Calculates a position and or size based on an element's parent's size
 */
class Percent(private var amount: Float) : Constraint.Measurement {

    override fun calculate(element: Component, type: Int): Float {
        val size = element.parent?.getSize(type % 2 == 0) ?: return 0f
        return size * amount
    }

    override fun toString() = "Percent(amount=\"$amount\")"
}