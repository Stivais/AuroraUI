package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.elements.DSL
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.renderer.data.Radii
import com.github.stivais.aurora.renderer.data.Gradient as GradientType

open class Block(
    constraints: Constraints,
    color: Color,
    radii: Radii?
): Element(constraints, color) {

    val radii = radii ?: EMPTY_RADIUS

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

        @JvmField
        val EMPTY_RADIUS = Radii(0f, 0f, 0f, 0f)

        @DSL
        fun ElementScope<Block>.outline(color: Color, thickness: Constraint.Measurement): ElementScope<Block> {
            element.outline = color
            element.thickness = thickness
            return this
        }
    }

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