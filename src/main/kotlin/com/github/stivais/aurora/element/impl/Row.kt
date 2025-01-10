package com.github.stivais.aurora.element.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.Sizes
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.element.Component
import com.github.stivais.aurora.utils.loop

class Row(
    ui: Aurora,
    positions: Positions,
    sizes: Sizes,
) : Component(ui, positions, sizes) {

    override fun size() {
        redraw = false
        if (size.height is Undefined && size.width is Undefined) {
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