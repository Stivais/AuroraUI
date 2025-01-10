package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.element.Component

object Center : Constraint.Position {

    override fun calculatePos(component: Component, horizontal: Boolean): Float {
        val size = component.getSize(horizontal)
        val parentSize = component.parent?.getSize(horizontal) ?: 0f
        if (size > parentSize) return 0f
        return parentSize / 2f - size / 2f
    }

    override fun mutates(component: Component): Boolean {
        return true
    }
}