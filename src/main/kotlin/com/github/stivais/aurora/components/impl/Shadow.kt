package com.github.stivais.aurora.components.impl

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.dsl.copies
import com.github.stivais.aurora.components.Component
import com.github.stivais.aurora.renderer.data.Radii

class Shadow(
    constraints: Constraints = copies(),
    color: Color = Color.BLACK,
    private val blur: Float,
    private val spread: Float,
    radii: Radii?,
) : Component(constraints, color) {

    private val radii = radii ?: Block.EMPTY_RADIUS

    override fun draw() {
        renderer.dropShadow(x, y, width, height, color!!.rgba, blur, spread, radii.topLeft, radii.topLeft, radii.bottomLeft, radii.bottomRight)
    }
}