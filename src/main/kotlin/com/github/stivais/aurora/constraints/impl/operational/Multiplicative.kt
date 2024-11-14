package com.github.stivais.aurora.constraints.impl.operational

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.elements.Element

/**
 * # Multiplicative
 *
 * Operational constraint, that takes 2 constraints and multiplies their result.
 */
class Multiplicative(
    private val first: Constraint,
    private val second: Constraint
) : Constraint.Measurement {

    override fun calculate(element: Element, type: Int): Float {
        return first.calculate(element, type) * second.calculate(element, type)
    }
    override fun reliesOnChildren() = first.reliesOnChildren() || second.reliesOnChildren()

    override fun reliesOnParent() = first.reliesOnParent() || second.reliesOnParent()

    override fun toString() = "Multiplicative(first=\"$first\", second=\"$second\")"
}