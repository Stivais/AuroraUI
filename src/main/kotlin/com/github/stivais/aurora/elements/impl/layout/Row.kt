package com.github.stivais.aurora.elements.impl.layout

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.constraints.impl.size.Copying
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.elements.AuroraDSL
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.elements.Layout
import com.github.stivais.aurora.utils.loop

/**
 * # Row
 *
 * This [element][com.github.stivais.aurora.elements.Element], which implements [Layout],
 * is used to place elements, with undefined positions, horizontally on the screen.
 *
 * If you want a layout to place elements vertically use [Column].
 *
 * @see Layout
 * @see Column
 */
class Row(
    constraints: Constraints,
    padding: Constraint.Size? = null
) : Layout(constraints, padding) {

    override fun prePosition() {
        val padding = padding?.calculateSize(this, horizontal = true) ?: 0f
        var increment = 0f
        children?.loop {
            if (it.constraints.x.undefined() && it.enabled) {
                it.internalX = increment
                increment += it.width + if (it is Divider) 0f else padding
            }
        }
    }

    companion object {
        /**
         * Creates a column, with a width being specified and height of [Copying].
         *
         * Acts as a section, to place elements in.
         */
        @AuroraDSL
        fun ElementScope<Column>.sectionColumn(
            size: Constraint.Size = Bounding,
            padding: Constraint.Size? = null,
            block: ElementScope<Column>.() -> Unit
        ) = Column(size(w = size, h = Copying), padding).scope(block)
    }
}