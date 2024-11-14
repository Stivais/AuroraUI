package com.github.stivais.aurora.elements.impl.layout

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.elements.Layout
import com.github.stivais.aurora.utils.loop

class Grid(
    constraints: Constraints,
    padding: Constraint.Size? = null
) : Layout(constraints, padding) {

    override fun prePosition() {
        val padding = padding?.calculateSize(this, horizontal = true) ?: 0f

        var currX = 0f
        var currY = 0f
        children?.loop {
            if (it.constraints.x is Undefined && it.constraints.y is Undefined) {
                if (currX + it.width + padding > width) {
                    currX = 0f
                    currY += it.height + padding
                }
                it.internalX = currX
                it.internalY = currY
                currX += it.width + padding
            }
        }
    }
}