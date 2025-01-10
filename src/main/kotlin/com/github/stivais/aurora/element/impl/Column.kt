package com.github.stivais.aurora.element.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.Sizes
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.element.Component
import com.github.stivais.aurora.utils.loop

class Column(
    ui: Aurora,
    positions: Positions,
    size: Sizes
) : Component(ui, positions, size) {

    override fun size() {
        if (size.height is Undefined && size.width is Undefined) {
            var width = 0f
            var height = 0f
            children?.loop {
                it.size()
                height += it.height
                if (it.width > width) width = it.width
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
            it.position(x, y + offset)
            it.layout()
            offset += it.height
        }
        redraw = false
    }
}