package com.github.stivais.aurora.constraints.impl.measurements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.components.Component

/**
 * # Pixel
 *
 * The most basic [measurement][Constraint.Measurement] in Aurora. Name is self-explanatory.
 */
class Pixel(var pixels: Float) : Constraint.Measurement {

    override fun calculate(element: Component, type: Int): Float {
        return pixels
    }

    override fun toString() = "Pixel(amount=$pixels)"

    companion object {
        /**
         * Constant for [Pixel]
         *
         * Note: Its value shouldn't be changed
         */
        @JvmField
        val ZERO = Pixel(0f)
    }
}