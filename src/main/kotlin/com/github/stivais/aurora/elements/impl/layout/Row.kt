package com.github.stivais.aurora.elements.impl.layout

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.constraints.impl.size.Copying
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.elements.BlankElement
import com.github.stivais.aurora.elements.DSL
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.elements.impl.Group
import com.github.stivais.aurora.utils.loop

class Row(
    constraints: Constraints,
    private val padding: Constraint.Size? = null
) : BlankElement(constraints) {

    override fun prePosition() {
        val padding = padding?.calculateSize(this, horizontal = true) ?: 0f
        var increment = 0f
        children?.loop {
            if (it.constraints.x is Undefined) {
                it.internalX = increment
                increment += it.width + if (it is Divider) 0f else padding
            }
        }
    }

    override fun getDefaultPositions() = Pair(Undefined, Undefined)

    companion object {
        @DSL
        fun ElementScope<Row>.divider(size: Constraint.Size) {
            element.addElement(Divider(width = size))
        }

        @DSL
        fun ElementScope<Row>.section(size: Constraint.Size, block: ElementScope<Group>.() -> Unit) {
            Group(size(w = Copying, h = size)).scope(block)
        }
    }
}