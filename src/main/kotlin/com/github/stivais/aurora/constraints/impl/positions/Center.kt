package com.github.stivais.aurora.constraints.impl.positions

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component

/**
 * # Center
 *
 * This constraint places the element in the center of the parent element.
 *
 * Do not confuse this with [Alignment.Center]
 */
object Center : Constraint.Position {

    override fun calculatePos(element: Component, horizontal: Boolean): Float {
        val parentSize = element.parent?.getSize(horizontal) ?: 0f
        if (parentSize == 0f) return 0f
        return parentSize / 2f - element.getSize(horizontal) / 2f
    }

    override fun toString() = "Center()"
}