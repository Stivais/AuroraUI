package com.github.stivais.aurora.element.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.Sizes
import com.github.stivais.aurora.element.Drawable
import com.github.stivais.aurora.renderer.Renderer

class Block(
    ui: Aurora,
    positions: Positions,
    size: Sizes,
    color: Color
) : Drawable(ui, positions, size, color) {

    override fun generate(renderer: Renderer) {
//        renderer.rect(x, y, width, height, color.rgba)
        val tl = minOf(width / 2f, height / 2f, 50f)
        renderer.roundedRect(x, y, width, height, color.rgba, tl, 0f, 0f, 0f)
//        renderer.circle(x + width / 2f, y + width / 2f, width / 2f, color.rgba)
    }
}