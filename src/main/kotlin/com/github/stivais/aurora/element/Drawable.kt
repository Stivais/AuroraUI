package com.github.stivais.aurora.element

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.renderer.Renderer

// handle all rendering stuff etc
abstract class Drawable(
    aurora: Aurora,
    position: Constraints<Constraint.Position>,
    size: Constraints<Constraint.Size>,
    val color: Color,
) : Component(aurora, position, size) {

    abstract fun generate(renderer: Renderer)

}