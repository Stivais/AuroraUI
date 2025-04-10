package com.github.stivais.aurora.constraints.impl.positions

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component

/**
 * # Alignment
 *
 * Lets you define how the element is placed based on its position.
 */
sealed interface Alignment : Constraint.Position {

    val position: Constraint.Position

    /**
     * # Alignment.Center
     *
     * Makes the position place the element aligning on its center (by default it is left to right).
     *
     * Do not confuse this with [Center][com.github.stivais.aurora.constraints.impl.positions.Center],
     * which places the element in the center of its parent.
     */
    class Center(override val position: Constraint.Position) : Alignment {
        override fun calculatePos(element: Component, horizontal: Boolean): Float {
            return position.calculatePos(element, horizontal) - element.getSize(horizontal) / 2f
        }
        override fun toString() = "Alignment.Center(position=\"$position)\n"
    }

    /**
     * # Alignment.Right
     *
     * Makes the position place the element aligning right to left (by default it is left to right).
     */
    class Right(override val position: Constraint.Position) : Alignment {
        override fun calculatePos(element: Component, horizontal: Boolean): Float {
            return position.calculatePos(element, horizontal) - element.getSize(horizontal)
        }
        override fun toString() = "Alignment.Right(position=\"$position)\n"
    }

    /**
     * # Alignment.Opposite
     *
     * When placing the element, the position will place from the other side of an element
     */
    class Opposite(override val position: Constraint.Position) : Alignment {
        override fun calculatePos(element: Component, horizontal: Boolean): Float {
            val width = (element.parent?.getSize(horizontal) ?: 0f)
            return (width - element.getSize(horizontal) - position.calculatePos(element, horizontal))
        }
        override fun toString() = "Alignment.Opposite(position=\"$position)\n"
    }
}