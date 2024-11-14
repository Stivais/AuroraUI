package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.renderer.data.Image
import com.github.stivais.aurora.renderer.data.Radii

class ImageElement(
    private var image: Image,
    constraints: Constraints,
    radius: Radii?,
) : Element(constraints) {

    private val radius: Radii = radius ?: Block.EMPTY_RADIUS

    // temp
    private var init = false

    override fun draw() {
        if (!init) {
            init = true
            renderer.createImage(image)
        }
        renderer.image(image, x, y, width, height, radius)
    }
}