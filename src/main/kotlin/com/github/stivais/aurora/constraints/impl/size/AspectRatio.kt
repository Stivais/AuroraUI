package com.github.stivais.aurora.constraints.impl.size

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component

/**
 * # AspectRatio
 *
 * This [Constraint.Size] calculates based on the other size with a ratio between them
 */
class AspectRatio(
    private var ratio: Float
) : Constraint.Size {

    override fun calculateSize(element: Component, horizontal: Boolean): Float {
        var size = element.getSize(!horizontal)
        size = if (horizontal) size * ratio else size / ratio
        return size
    }

    override fun toString() = "AspectRatio(ratio=\"$ratio\")"
}