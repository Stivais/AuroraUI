package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component

/**
 * # Undefined
 *
 * Acts as placeholder constraint and doesn't calculate anything.
 *
 * All it does is simply return the corresponding position/size,
 * allowing you to mutate it without the constraint setting it to something else.
 */
object Undefined : Constraint.Measurement {
    override fun calculate(element: Component, type: Int): Float {
        return when (type) {
            0 -> element.internalX
            1 -> element.internalY
            2 -> element.width
            else -> element.height
        }
    }

    override fun toString() = "Undefined()"
}