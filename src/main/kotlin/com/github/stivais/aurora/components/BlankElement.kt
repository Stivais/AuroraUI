package com.github.stivais.aurora.components

import com.github.stivais.aurora.constraints.Constraints

/**
 * # BlankElement
 *
 * Subclass of [Component], which doesn't render anything.
 */
abstract class BlankElement(constraints: Constraints) : Component(constraints) {
    final override fun draw() {
        // doesn't render
    }
}