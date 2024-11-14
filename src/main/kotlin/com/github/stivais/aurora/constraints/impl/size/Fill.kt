package com.github.stivais.aurora.constraints.impl.size

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.elements.Element

/**
 * # Fill
 *
 * This size gets the size between its element's position, and it's parent's edge
 */
object Fill : Constraint.Size {

    override fun calculateSize(element: Element, horizontal: Boolean): Float {
        var p = element.parent ?: return 0f
        if (p.constraints.getSize(horizontal).reliesOnChildren()) p = p.parent ?: return 0f
        return p.getSize(horizontal) - element.getPosition(horizontal)
    }

    override fun toString() = "Fill()"
}