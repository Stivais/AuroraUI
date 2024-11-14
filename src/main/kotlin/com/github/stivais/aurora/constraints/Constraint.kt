package com.github.stivais.aurora.constraints

import com.github.stivais.aurora.elements.Element

/**
 * # Constraint
 *
 * Base interface for [Position], [Size] and [Measurement].
 *
 * It allows for calculating position/size based on any implementation.
 */
sealed interface Constraint {

    /**
     * Calculates position/size based on [type]
     *
     * @param type type of position or size. (x: 0, y: 1, width: 2, height: 3)
     */
    fun calculate(element: Element, type: Int = 0): Float

    /**
     * If this constraint uses its children's value in the calculation,
     * this is to ensure they're positioned/sized correctly in the [positioning pipeline][com.github.stivais.aurora.elements.Element.size]
     */
    fun reliesOnChildren(): Boolean = false


    /**
     * If this constraint uses its parent's value in the calculation,
     * this is to ensure they're positioned/sized correctly
     */
    fun reliesOnParent(): Boolean = false

    /**
     * # Position
     *
     * Interface for constraints,
     * which exclusively work for position and cannot be used to calculate the size of an element.
     *
     * Positions provided by Aurora include:
     * [Center][com.github.stivais.aurora.constraints.impl.positions.Center],
     * [Alignment.Center][com.github.stivais.aurora.constraints.impl.positions.Alignment.Center],
     * [Alignment.Right][com.github.stivais.aurora.constraints.impl.positions.Alignment.Right],
     * [Alignment.Opposite][com.github.stivais.aurora.constraints.impl.positions.Alignment.Opposite]
     */
    interface Position : Constraint {

        /**
         * Calculates position, with [horizontal] defining if x or y is being positioned
         *
         * @param horizontal If this is true x is being positioned else y is being positioned
         */
        fun calculatePos(element: Element, horizontal: Boolean): Float

        override fun calculate(element: Element, type: Int): Float = calculatePos(element, type == 0)
    }

    /**
     * # Size
     *
     * Interface for constraints,
     * which exclusively work for size and cannot be used to calculate the position of an element.
     *
     * Sizes provided by Aurora include:
     * [Bounding][com.github.stivais.aurora.constraints.impl.size.Bounding],
     * [Copying][com.github.stivais.aurora.constraints.impl.size.Copying],
     * [Fill][com.github.stivais.aurora.constraints.impl.size.Fill],
     * [AspectRatio][com.github.stivais.aurora.constraints.impl.size.AspectRatio],
     */
    interface Size : Constraint {

        /**
         * Calculates size, with [horizontal] defining if width or height is being sized
         *
         * @param horizontal If this is true width is being sized else height is being sized
         */
        fun calculateSize(element: Element, horizontal: Boolean): Float

        override fun calculate(element: Element, type: Int): Float = calculateSize(element, type == 2)
    }

    /**
     * # Measurement
     *
     * Interface for constraints, which works for both size and positions.
     *
     * Measurements provided by Aurora include:
     * [Pixel][com.github.stivais.aurora.constraints.impl.measurements.Pixel],
     * [Percent][com.github.stivais.aurora.constraints.impl.measurements.Percent],
     * [Animatable][com.github.stivais.aurora.constraints.impl.measurements.Animatable],
     * [Undefined][com.github.stivais.aurora.constraints.impl.measurements.Undefined],
     * [Additive][com.github.stivais.aurora.constraints.impl.operational.Additive],
     * [Multiplicative][com.github.stivais.aurora.constraints.impl.operational.Multiplicative],
     * [Subtractive][com.github.stivais.aurora.constraints.impl.operational.Subtractive],
     */
    interface Measurement : Position, Size {

        override fun calculatePos(element: Element, horizontal: Boolean): Float {
            return calculate(element, if (horizontal) 0 else 1)
        }

        override fun calculateSize(element: Element, horizontal: Boolean): Float {
            return calculate(element, if (horizontal) 2 else 3)
        }

        override fun calculate(element: Element, type: Int): Float
    }
}