package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.elements.BlankElement

/**
 * # Group
 *
 * [BlankElement], where if width and or height constraints are [Undefined], it gets replaced by [Bounding]
 */
class Group(constraints: Constraints) : BlankElement(constraints) {
    init {
        if (constraints.width is Undefined) constraints.width = Bounding
        if (constraints.height is Undefined) constraints.height = Bounding
    }
}