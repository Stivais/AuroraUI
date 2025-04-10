package com.github.stivais.aurora.renderer.data

/**
 * Used in *mainly* the [Block][com.github.stivais.aurora.components.impl.Block] element
 * to represent rounded corners
 */
data class Radii(
    var topLeft: Float,
    var bottomLeft: Float,
    var bottomRight: Float,
    var topRight: Float
)