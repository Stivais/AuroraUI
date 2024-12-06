@file:Suppress("UNUSED", "FunctionName")

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.elements.impl.Group
import com.github.stivais.aurora.events.AuroraEvent
import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.renderer.data.Radii
import com.github.stivais.aurora.transforms.impl.Alpha
import com.github.stivais.aurora.transforms.impl.Rotation
import com.github.stivais.aurora.transforms.impl.Scale

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

/**
 * Creates a [Scale] transform and adds it to the element
 */
fun ElementScope<*>.scale(amount: Float): Scale {
    val scale = Scale(amount)
    transform(scale)
    return scale
}

/**
 * Creates a [Scale.Animated] transform and adds it to the element
 */
fun ElementScope<*>.scale(from: Float, to: Float): Scale.Animated {
    val scale = Scale.Animated(from, to)
    transform(scale)
    return scale
}

/**
 * Creates a [Rotation] transform and adds it to the element
 */
fun ElementScope<*>.rotation(amount: Float): Rotation {
    val rotation = Rotation(amount)
    transform(rotation)
    return rotation
}

/**
 * Creates a [Rotation.Animated] transform and adds it to the element
 */
fun ElementScope<*>.rotation(from: Float, to: Float): Rotation.Animated {
    val rotation = Rotation.Animated(from, to)
    transform(rotation)
    return rotation
}

/**
 * Creates a [Alpha] transform and adds it to the element
 */
fun ElementScope<*>.alpha(amount: Float): Alpha {
    val alpha = Alpha(amount)
    transform(alpha)
    return alpha
}

/**
 * Creates a [Alpha.Animated] transform and adds it to the element
 */
fun ElementScope<*>.alpha(from: Float, to: Float): Alpha.Animated {
    val alpha = Alpha.Animated(from, to)
    transform(alpha)
    return alpha
}

/**
 * Moves the element to the top inside of it's parent
 */
fun Element.moveToTop() {
    val it = this
    this.parent!!.children!!.apply {
        remove(it)
        add(it)
    }
}

/**
 * Passes an event to a different scope.
 *
 * Note: Shouldn't be used with any input related events (like Mouse click events).
 */
fun ElementScope<*>.passEvent(event: AuroraEvent, to: ElementScope<*>) {
    ui.eventManager.postToAll(event, to.element)
}

/**
 * Toggles this element's enabled value.
 */
fun <E : Element> ElementScope<E>.toggle(): ElementScope<E> {
    element.enabled = !element.enabled
    return this
}

/**
 * Returns a boolean based on if this element is focused in the [EventManager][com.github.stivais.aurora.events.EventManager]
 */
fun ElementScope<*>.focused(): Boolean {
    return ui.eventManager.focused == this.element
}