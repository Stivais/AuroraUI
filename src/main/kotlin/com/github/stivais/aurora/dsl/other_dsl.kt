@file:Suppress("UNUSED", "FunctionName")

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.elements.impl.Group
import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.renderer.data.Radii

/**
 * Function that creates an instance of [AuroraUI],
 * and automatically creates an [ElementScope] starting from the root element
 */
fun Aurora(renderer: Renderer, block: ElementScope<Group>.() -> Unit): AuroraUI {
    val ui = AuroraUI(renderer)
    ElementScope(ui.main).block()
    return ui
}

/**
 * Converts seconds into nanoseconds,
 * which is the main timing in Aurora.
 */
val Number.seconds: Float
    get() = this.toFloat() * 1_000_000_000

/**
 * Converts milliseconds into nanoseconds,
 * which is the main timing in Aurora.
 */
val Number.ms: Float
    get() = this.toFloat() * 1_000_000

/**
 * Creates a [Radii], where all numbers are the inputted value.
 */
fun Number.radius() = Radii(this.toFloat(), this.toFloat(), this.toFloat(), this.toFloat())

/**
 * Creates a [Radii] from 4 Numbers converted to floats
 */
fun radius(tl: Number = 0f, bl: Number = 0f, br: Number = 0f, tr: Number = 0f) =
    Radii(tl.toFloat(), bl.toFloat(), br.toFloat(), tr.toFloat())