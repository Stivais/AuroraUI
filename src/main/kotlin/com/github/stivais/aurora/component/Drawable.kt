package com.github.stivais.aurora.component

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.engine.renderer.tessellator.Tessellator

// handle all rendering stuff etc
abstract class Drawable(
    aurora: Aurora,
    position: Constraints<Constraint.Position>,
    size: Constraints<Constraint.Size>,
    val color: Color,
) : Component(aurora, position, size) {

    var bufferSlotIndex: Long = 0L
    var bufferSlotSize: Long = 0L

    abstract fun generate(tessellator: Tessellator)


    fun generate() {
        bufferSlotIndex = Tessellator.getCurrentIndex().toLong()
        generate(Tessellator)
        bufferSlotSize = Tessellator.getCurrentIndex() - bufferSlotIndex
//        println("a $bufferSlotIndex $bufferSlotSize")
    }
}