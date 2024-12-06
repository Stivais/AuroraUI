package com.github.stivais.aurora.elements.impl.layout

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.elements.Layout
import com.github.stivais.aurora.utils.loop

/**
 * # Grid
 *
 * This [Layout], positions children in a grid pattern,
 * where it places them horizontally until the elements exceed the width,
 * when it does it will start placing them on a new line.
 */
class Grid(
    constraints: Constraints,
    padding: Constraint.Size? = null
) : Layout(constraints, padding) {

    override fun prePosition() {
        val padding = padding?.calculateSize(this, horizontal = true) ?: 0f

        var currX = 0f
        var currY = 0f
        children?.loop {
            if (it.constraints.x.undefined() && it.constraints.y.undefined() && it.enabled) {
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