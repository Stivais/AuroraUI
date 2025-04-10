package com.github.stivais.aurora.constraints.impl.operational

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component

/**
 * # Additive
 *
 * Operational constraint, that takes 2 constraints and combines their result.
 */
class Additive(
    private val first: Constraint,
    private val second: Constraint
) : Constraint.Measurement {

    override fun calculate(element: Component, type: Int): Float {
        return first.calculate(element, type) + second.calculate(element, type)
    }

    override fun reliesOnChildren() = first.reliesOnChildren() || second.reliesOnChildren()

    override fun reliesOnParent() = first.reliesOnParent() || second.reliesOnParent()

    override fun toString() = "Additive(first=\"$first\", second=\"$second\")"
}