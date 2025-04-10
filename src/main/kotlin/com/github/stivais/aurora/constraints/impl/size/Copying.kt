package com.github.stivais.aurora.constraints.impl.size

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component

/**
 * # Copying
 *
 * This constraint copies its parent's size.
 */
object Copying : Constraint.Size {

    override fun calculateSize(element: Component, horizontal: Boolean): Float {
        return (element.parent?.getSize(horizontal) ?: 0f)
    }

    override fun reliesOnParent() = true

    override fun toString() = "Copying()"
}