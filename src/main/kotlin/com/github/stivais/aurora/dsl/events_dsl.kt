@file:OptIn(ExperimentalTypeInference::class)
@file:Suppress("unused", "unused_parameter")

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.components.Component
import com.github.stivais.aurora.components.scope.ComponentScope
import com.github.stivais.aurora.events.*
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
fun ComponentScope<*>.onClick(button: Int = 0, block: (Mouse.Clicked) -> Boolean) {
    component.registerEvent(Mouse.Clicked(button), block)
}

/**
 * Registers [Mouse.Clicked] event with a specified button.
 *
 * Returns false by default.
 */
@JvmName("onClickUnit")
@OverloadResolutionByLambdaReturnType
inline fun ComponentScope<*>.onClick(button: Int = 0, crossinline block: (Mouse.Clicked) -> Unit) {
    component.registerEventUnit(Mouse.Clicked(button), block)
}


/**
 * Registers [Mouse.Clicked.NonSpecific] event with a specified button.
 *
 * Has an optional return value.
 */
@Suppress
@OverloadResolutionByLambdaReturnType
fun ComponentScope<*>.onClick(nonSpecific: Boolean, block: (Mouse.Clicked.NonSpecific) -> Boolean) {
    component.registerEvent(Mouse.Clicked.NonSpecific(0 /* doesn't matter */), block)
}


/**
 * Registers [Mouse.Clicked.NonSpecific] event with a specified button.
 *
 * Returns false by default.
 */
@JvmName("onClickNSUnit")
@OverloadResolutionByLambdaReturnType
inline fun ComponentScope<*>.onClick(nonSpecific: Boolean, crossinline block: (Mouse.Clicked.NonSpecific) -> Unit) {
    component.registerEventUnit(Mouse.Clicked.NonSpecific(0), block)
}

/**
 * Registers [Mouse.Released] event with a specified button.
 *
 * Always returns false.
 */
inline fun ComponentScope<*>.onRelease(button: Int = 0, crossinline block: (Mouse.Released) -> Unit) {
    component.registerEventUnit(Mouse.Released(button), block)
}

/**
 * Registers [Mouse.Scrolled] event.
 *
 * Has an optional return value.
 */
@OverloadResolutionByLambdaReturnType
fun ComponentScope<*>.onScroll(block: (Mouse.Scrolled) -> Boolean) {
    component.registerEvent(Mouse.Scrolled(0f), block)
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
inline fun ComponentScope<*>.onScroll(crossinline block: (Mouse.Scrolled) -> Unit) {
    component.registerEventUnit(Mouse.Scrolled(0f), block)
}

/**
 * Registers [Mouse.Moved] event.
 *
 * Has an optional return value.
 */
@OverloadResolutionByLambdaReturnType
fun ComponentScope<*>.onMouseMove(block: (Mouse.Moved) -> Boolean) {
    component.registerEvent(Mouse.Moved, block)
}

/**
 * Registers [Mouse.Moved] event.
 *
 * Returns false by default.
 */
@JvmName("_onMouseMove")
@OverloadResolutionByLambdaReturnType
inline fun ComponentScope<*>.onMouseMove(crossinline block: (Mouse.Moved) -> Unit) {
    component.registerEventUnit(Mouse.Moved, block)
}

/**
 * Registers [Mouse.Entered] event.
 *
 * Returns false by default.
 */
inline fun ComponentScope<*>.onMouseEnter(crossinline block: () -> Unit) {
    component.registerEvent(Mouse.Entered) { block(); false }
}

/**
 * Registers [Mouse.Exited] event.
 *
 * Returns false by default.
 */
inline fun ComponentScope<*>.onMouseExit(crossinline block: () -> Unit) {
    component.registerEvent(Mouse.Exited) { block(); false }
}

/**
 * Registers both [Mouse.Entered] and [Mouse.Exited] events, using the same function.
 *
 * Returns false by default.
 */
inline fun ComponentScope<*>.onMouseEnterExit(crossinline block: () -> Unit) {
    component.registerEvent(Mouse.Entered) { block(); false }
    component.registerEvent(Mouse.Exited) { block(); false }
}

//-----------------//
// Keyboard events //
//-----------------//

/**
 * Registers [Keyboard.CodeTyped] event.
 *
 * Has an optional return value.
 */
@OverloadResolutionByLambdaReturnType
fun ComponentScope<*>.onKeycodePressed(block: (Keyboard.CodeTyped) -> Boolean) {
    component.registerEvent(Keyboard.CodeTyped(), block)
}

//-----------------//
// Lifetime events //
//-----------------//

/**
 * Registers [Lifetime.Initialized] event.
 *
 * Returns false by default.
 */
inline fun ComponentScope<*>.onAdd(crossinline block: (Lifetime.Initialized) -> Unit) {
    component.registerEventUnit(Lifetime.Initialized, block)
}

/**
 * Registers [Lifetime.Uninitialized] event.
 *
 * Returns false by default.
 */
inline fun ComponentScope<*>.onRemove(crossinline block: (Lifetime.Uninitialized) -> Unit) {
    component.registerEventUnit(Lifetime.Uninitialized, block)
}

//-----------------//
// Lifetime events //
//-----------------//

/**
 * Registers [Focused.Gained] event.
 *
 * Returns false by default.
 */
inline fun ComponentScope<*>.onFocus(crossinline block: (Focused.Gained) -> Unit) {
    component.registerEventUnit(Focused.Gained, block)
}

/**
 * Registers [Focused.Gained] event.
 *
 * Returns false by default.
 */
inline fun ComponentScope<*>.onFocusLost(crossinline block: (Focused.Lost) -> Unit) {
    component.registerEventUnit(Focused.Lost, block)
}

/**
 * Registers both [Focused.Gained] and [Focused.Lost] events, using the same function.
 *
 * Returns false by default.
 */
inline fun ComponentScope<*>.onFocusChanged(crossinline block: () -> Unit) {
    component.registerEvent(Focused.Gained) { block(); false }
    component.registerEvent(Focused.Lost) { block(); false }
}

/**
 * Registers an event listener, which always returns false.
 *
 * If the event inherits [AuroraEvent.NonSpecific], it's [Class] will be added to [events][Component.events].
 *
 * If the event isn't a lifetime event, it will mark [acceptsInput][Component.acceptsInput] as true.
 *
 * @see Component.registerEvent
 */
inline fun <E : AuroraEvent> Component.registerEventUnit(event: E, crossinline block: (E) -> Unit) {
    registerEvent(event) {
        block(it)
        false
    }
}