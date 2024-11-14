package com.github.stivais.aurora.elements.impl.layout

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.constraints.impl.size.Copying
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.elements.BlankElement
import com.github.stivais.aurora.elements.DSL
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.elements.impl.Group
import com.github.stivais.aurora.utils.loop

class Column(
    constraints: Constraints,
    private val padding: Constraint.Size? = null,
) : BlankElement(constraints) {

    override fun prePosition() {
        val paddingY = padding?.calculateSize(this, horizontal = false) ?: 0f
        var increment = 0f
        children?.loop {
            if (it.constraints.y is Undefined) {
                it.internalY = increment
                increment += it.height + if (it is Divider) 0f else paddingY
            }
        }
    }

    override fun getDefaultPositions() = Pair(Undefined, Undefined)

    companion object {
        @DSL
        fun ElementScope<Column>.divider(size: Constraint.Size) {
            element.addElement(Divider(height = size))
        }

        @DSL
        fun ElementScope<Column>.section(
            size: Constraint.Size,
            block: ElementScope<Group>.() -> Unit
        ) = Group(size(w = Copying, h = size)).scope(block)


        @DSL
        fun ElementScope<Column>.sectionRow(
            size: Constraint.Size = Bounding,
            padding: Constraint.Size? = null,
            block: ElementScope<Row>.() -> Unit
        ) = Row(size(w = Copying, h = size), padding).scope(block)
    }
}