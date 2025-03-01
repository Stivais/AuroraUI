package com.github.stivais.aurora.element.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.element.Drawable

class Block(
    aurora: Aurora,
    position: Constraints<Constraint.Position>,
    size: Constraints<Constraint.Size>,
    color: Color
) : Drawable(aurora, position, size, color) {

    override fun generate() {
//        val tl = minOf(width / 2f, height / 2f, 50f)
//        renderer.roundedRect(x, y, width, height, color.rgba, tl, 0f, 0f, 0f)
    }
}