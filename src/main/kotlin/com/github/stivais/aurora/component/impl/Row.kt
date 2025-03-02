package com.github.stivais.aurora.component.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.component.Component
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.measurements.impl.Undefined
import com.github.stivais.aurora.utils.loop

class Row(
    aurora: Aurora,
    position: Constraints<Constraint.Position>,
    size: Constraints<Constraint.Size>,
) : Component(aurora, position, size) {

    override fun size() {
        redraw = false
        if (size.first is Undefined && size.second is Undefined) {
            var width = 0f
            var height = 0f
            children?.loop {
                it.size()
                width += it.width
                if (it.height > height) height = it.height
            }
            this.width = width
            this.height = height
        } else {
            super.size()
        }
    }

    override fun layout() {
        var offset = 0f
        children?.loop {
            it.position(x + offset, y)
            it.layout()
            offset += it.width
        }
    }
}
