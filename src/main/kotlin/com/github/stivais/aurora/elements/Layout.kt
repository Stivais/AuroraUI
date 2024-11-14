package com.github.stivais.aurora.elements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.constraints.impl.size.Copying
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.elements.impl.Group
import com.github.stivais.aurora.elements.impl.layout.Column
import com.github.stivais.aurora.elements.impl.layout.Grid
import com.github.stivais.aurora.elements.impl.layout.Row

/**
 * # Layout
 *
 * This element is mainly used to provide functionality for elements,
 * which place other elements inside of it.
 *
 * If width and or height constraints are [Undefined], it will replace them with [Bounding]
 *
 * Base layouts include:
 * [Column],
 * [Row],
 * [Grid]
 */
abstract class Layout(
    constraints: Constraints,
    val padding: Constraint.Size?
) : BlankElement(constraints) {

    init {
        if (constraints.width is Undefined) constraints.width = Bounding
        if (constraints.height is Undefined) constraints.height = Bounding
    }

    final override fun getDefaultPositions() = Pair(Undefined, Undefined)

    /**
     * Divider for [Layout] elements.
     *
     * @see divider
     */
    internal class Divider(
        width: Constraint.Size = Copying,
        height: Constraint.Size = Copying,
    ) : BlankElement(size(width, height)) {

        init {
            require(width != height) {
                "When using divider, it's width and height must not be equal"
            }
        }
    }

    companion object {

        /**
         * Creates a divider, with a specified size, inside a layout.
         *
         * It isn't supported for [Grid].
         */
        @DSL
        fun <E : Layout> ElementScope<E>.divider(size: Constraint.Size) {
            val divider = when (element) {
                is Column -> Divider(height = size)
                is Row -> Divider(width = size)
                else -> return
            }
            element.addElement(divider)
        }

        /**
         * Creates a section, with a specified size, inside a layout.
         *
         * This is intended to hold elements or contain functionality unlike [divider].
         *
         * It isn't supported for [Grid].
         */
        @DSL
        fun <E : Layout> ElementScope<E>.section(
            size: Constraint.Size,
            block: ElementScope<Group>.() -> Unit = {}
        ) {
            val group = when (element) {
                is Column -> Group(size(w = Copying, h = size))
                is Row -> Group(size(w = size, h = Copying))
                else -> return
            }
            group.scope(block)
        }
    }
}