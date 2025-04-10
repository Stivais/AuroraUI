package com.github.stivais.aurora.components.impl

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.components.scope.AuroraDSL
import com.github.stivais.aurora.components.Component
import com.github.stivais.aurora.components.scope.ComponentScope
import com.github.stivais.aurora.renderer.data.Radii
import com.github.stivais.aurora.renderer.data.Gradient as GradientType

/**
 * # Block
 *
 * The most basic element (which renders).
 *
 * It is a rectangle that can optionally be rounded
 * and also have an outline with a specified thickness.
 */
open class Block(
    constraints: Constraints,
    color: Color,
    radii: Radii?
): Component(constraints, color) {

    protected val radii = radii ?: EMPTY_RADIUS

    private var outline: Color? = null
    private var thickness: Constraint.Measurement? = null

    override fun draw() {
        renderer.rect(x, y, width, height, color!!.rgba, radii)
        if (thickness != null && outline != null) {
            val thickness = thickness!!.calculate(this)
            renderer.hollowRect(x, y, width, height, thickness, outline!!.rgba, radii)
        }
    }

    companion object {

        /**
         * Constant value for usage between [Blocks][Block] without a radius.
         */
        @JvmField
        val EMPTY_RADIUS = Radii(0f, 0f, 0f, 0f)

        /**
         * Adds an outline to a [Block] with a specified thickness.
         */
        @AuroraDSL
        fun ComponentScope<Block>.outline(color: Color, thickness: Constraint.Measurement): ComponentScope<Block> {
            component.outline = color
            component.thickness = thickness
            return this
        }
    }

    /**
     * # Block.Gradient
     *
     * Copy of [Block], with a gradient between 2 colors based on a direction.
     */
    class Gradient(
        constraints: Constraints,
        color1: Color,
        private var color2: Color,
        private val gradient: GradientType,
        radius: Radii?
    ): Block(constraints, color1, radius) {
        override fun draw() {
            renderer.gradientRect(
                x, y, width, height, color!!.rgba, color2.rgba, gradient, radii
            )
        }
    }
}