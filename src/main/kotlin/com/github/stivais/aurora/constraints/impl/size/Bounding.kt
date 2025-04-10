package com.github.stivais.aurora.constraints.impl.size

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component
import com.github.stivais.aurora.utils.loop

/**
 * # Bounding
 *
 * This size essentially calculates a Bounding box around its element's children.
 *
 * Note: it will ignore any constraint that relies on its parent for the constraint, as it causes flaws in calculating
 */
object Bounding : Constraint.Size {

    override fun calculateSize(element: Component, horizontal: Boolean): Float {
        val children = element.children ?: return 0f

        var value = 0f
        children.loop {
            if (!it.enabled) return@loop
            if (!it.constraints.getSize(horizontal).reliesOnParent()) {
                val new = it.getPosition(horizontal) + it.getSize(horizontal)
                if (new > value) value = new
            }
        }
        return value
    }

    override fun reliesOnChildren(): Boolean = true

    override fun toString() = "Bounding()"
}