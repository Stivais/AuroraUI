package com.github.stivais.aurora.element.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.element.Component
import com.github.stivais.aurora.utils.loop

class Column(
    aurora: Aurora,
    position: Constraints<Constraint.Position>,
    size: Constraints<Constraint.Size>,
) : Component(aurora, position, size) {

    override fun size() {
//        val height: Measurement<Constraint.Size> = size.second
//        if (height is Undefined && size.first is Undefined) {
//            var width = 0f
//            var height = 0f
//            children?.loop {
//                it.size()
//                height += it.height
//                if (it.width > width) width = it.width
//            }
//            this.width = width
//            this.height = height
//        } else {
//            super.size()
//        }
    }

    override fun layout() {
        var offset = 0f
        children?.loop {
            it.position(x, y + offset)
            it.layout()
            offset += it.height
        }
        redraw = false
    }
}