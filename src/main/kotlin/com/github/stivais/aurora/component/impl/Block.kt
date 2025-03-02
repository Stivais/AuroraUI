package com.github.stivais.aurora.component.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.component.Drawable
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.engine.renderer.tessellator.Mode
import com.github.stivais.aurora.engine.renderer.tessellator.Tessellator

class Block(
    aurora: Aurora,
    position: Constraints<Constraint.Position>,
    size: Constraints<Constraint.Size>,
    color: Color
) : Drawable(aurora, position, size, color) {

    override fun generate(tessellator: Tessellator) {
        val rgba = color.rgba
        val x1 = x + width
        val y1 = y + height

//        println("$x $y $x1 $y1")

        tessellator {
            begin(Mode.TRIANGLE_FAN)
            vertex(x, y, rgba)
            vertex(x1, y, rgba)
            vertex(x1, y1, rgba)
            vertex(x, y1, rgba)
        }
    }
}