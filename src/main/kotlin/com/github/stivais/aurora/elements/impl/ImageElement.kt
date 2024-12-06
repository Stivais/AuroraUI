package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.events.Lifetime
import com.github.stivais.aurora.renderer.data.Image
import com.github.stivais.aurora.renderer.data.Radii

/**
 * # ImageElement
 *
 * This element renders an image, with an optional radius.
 */
class ImageElement(
    private var image: Image,
    constraints: Constraints,
    radius: Radii?,
) : Element(constraints) {

    private val radius: Radii = radius ?: Block.EMPTY_RADIUS

    init {
        registerEvent(Lifetime.Initialized) {
            renderer.createImage(image)
            false
        }
        registerEvent(Lifetime.Uninitialized) {
            renderer.deleteImage(image)
            false
        }
    }

    override fun draw() {
        renderer.image(image, x, y, width, height, radius)
    }
}