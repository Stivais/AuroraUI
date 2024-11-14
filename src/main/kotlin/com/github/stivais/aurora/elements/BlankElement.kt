package com.github.stivais.aurora.elements

import com.github.stivais.aurora.constraints.Constraints

/**
 * # BlankElement
 *
 * Subclass of [Element], which doesn't render anything.
 */
abstract class BlankElement(constraints: Constraints) : Element(constraints) {
    final override fun draw() {
        // doesn't render
    }
}