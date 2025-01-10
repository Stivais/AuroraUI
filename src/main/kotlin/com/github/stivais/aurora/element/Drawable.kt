package com.github.stivais.aurora.element

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.Sizes
import com.github.stivais.aurora.renderer.Renderer

// handle all rendering stuff etc
abstract class Drawable(
    ui: Aurora,
    positions: Positions,
    size: Sizes,
    val color: Color,
) : Component(ui, positions, size) {

    abstract fun generate(renderer: Renderer)

}