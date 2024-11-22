@file:OptIn(ExperimentalTypeInference::class)

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.events.Lifetime
import com.github.stivais.aurora.events.Mouse
import kotlin.experimental.ExperimentalTypeInference

//
// This file contains functions to make
// registering existing events much simpler
//

//--------------//
// Mouse Events //
//--------------//

/**
 * Registers [Mouse.Clicked] event with a specified button.
 *
 * Has an optional return value.
 */
@OverloadResolutionByLambdaReturnType
fun ElementScope<*>.onClick(button: Int = 0, block: (Mouse.Clicked) -> Boolean) {
    element.registerEvent(Mouse.Clicked(button), block)
}

/**
 * Registers [Mouse.Clicked] event with a specified button.
 *
 * Returns false by default.
 */
@JvmName("onClickUnit")
@OverloadResolutionByLambdaReturnType
inline fun ElementScope<*>.onClick(button: Int = 0, crossinline block: (Mouse.Clicked) -> Unit) {
    element.registerEvent(Mouse.Clicked(button)) { block(it); false }
}


/**
 * Registers [Mouse.Released] event with a specified button.
 *
 * Always returns false.
 */
inline fun ElementScope<*>.onRelease(button: Int = 0, crossinline block: (Mouse.Released) -> Unit) {
    element.registerEvent(Mouse.Released(button)) { block(it); false }
}

/**
 * Registers [Mouse.Scrolled] event.
 *
 * Has an optional return value.
 */
@OverloadResolutionByLambdaReturnType
fun ElementScope<*>.onScroll(block: (Mouse.Scrolled) -> Boolean) {
    element.registerEvent(Mouse.Scrolled(0f), block)
}

/**
 * Registers [Mouse.Scrolled] event.
 *
 * Note: [amount][Mouse.Scrolled.amount] is always between -1f to 1f, you need to handle strength manually.
 *
 * Returns false by default.
 */
@JvmName("onScrollUnit")
@OverloadResolutionByLambdaReturnType
inline fun ElementScope<*>.onScroll(crossinline block: (Mouse.Scrolled) -> Unit) {
    element.registerEvent(Mouse.Scrolled(0f)) { block(it); false }
}

/**
 * Registers [Mouse.Moved] event.
 *
 * Has an optional return value.
 */
@OverloadResolutionByLambdaReturnType
fun ElementScope<*>.onMouseMove(block: (Mouse.Moved) -> Boolean) {
    element.registerEvent(Mouse.Moved, block)
}

/**
 * Registers [Mouse.Moved] event.
 *
 * Returns false by default.
 */
@JvmName("_onMouseMove")
@OverloadResolutionByLambdaReturnType
inline fun ElementScope<*>.onMouseMove(crossinline block: (Mouse.Moved) -> Unit) {
    element.registerEvent(Mouse.Moved) { block(it); false }
}

/**
 * Registers [Mouse.Entered] event.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onMouseEnter(crossinline block: () -> Unit) {
    element.registerEvent(Mouse.Entered) { block(); false }
}

/**
 * Registers [Mouse.Exited] event.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onMouseExit(crossinline block: () -> Unit) {
    element.registerEvent(Mouse.Exited) { block(); false }
}

/**
 * Registers both [Mouse.Entered] and [Mouse.Exited] events, using the same function.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onMouseEnterExit(crossinline block: () -> Unit) {
    element.registerEvent(Mouse.Entered) { block(); false }
    element.registerEvent(Mouse.Exited) { block(); false }
}

//-----------------//
// Lifetime events //
//-----------------//
/**
 * Registers [Lifetime.Initialized] event.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onAdd(crossinline block: (Lifetime.Initialized) -> Unit) {
    element.registerEvent(Lifetime.Initialized) { block(it); false }
}

/**
 * Registers [Lifetime.Uninitialized] event.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onRemove(crossinline block: (Lifetime.Uninitialized) -> Unit) {
    element.registerEvent(Lifetime.Uninitialized) { block(it); false }
}