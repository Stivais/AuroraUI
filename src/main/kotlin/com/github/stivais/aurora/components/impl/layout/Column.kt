package com.github.stivais.aurora.components.impl.layout

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.constraints.impl.size.Copying
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.components.scope.AuroraDSL
import com.github.stivais.aurora.components.scope.ComponentScope
import com.github.stivais.aurora.components.Layout
import com.github.stivais.aurora.utils.loop

/**
 * # Column
 *
 * This [element][com.github.stivais.aurora.components.Component], which implements [Layout],
 * is used to place elements, with undefined positions, vertically on the screen.
 *
 * If you want a layout to place elements horizontally use [Row].
 *
 * @see Layout
 * @see Row
 */
class Column(
    constraints: Constraints,
    padding: Constraint.Size? = null,
) : Layout(constraints, padding) {

    override fun prePosition() {
        val paddingY = padding?.calculateSize(this, horizontal = false) ?: 0f
        var increment = 0f

        children?.loop {
//            if (!it.constraints.y.undefined()) {
//                throw Exception()
//            }
            if (it.constraints.y.undefined() && it.enabled) {
                it.internalY = increment
                increment += it.height + if (it is Divider) 0f else paddingY
            }
        }
    }

    companion object {
        /**
         * Creates a row, with a width of [Copying] and height being specified.
         *
         * Acts as a section, to place elements in.
         */
        @AuroraDSL
        fun ComponentScope<Column>.sectionRow(
            size: Constraint.Size = Bounding,
            padding: Constraint.Size? = null,
            block: ComponentScope<Row>.() -> Unit
        ) = Row(size(w = Copying, h = size), padding).scope(block)
    }
}