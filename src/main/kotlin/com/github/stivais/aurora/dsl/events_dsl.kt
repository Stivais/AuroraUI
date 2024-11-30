@file:OptIn(ExperimentalTypeInference::class)
@file:Suppress("unused")

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
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
    element.registerEventUnit(Mouse.Clicked(button), block)
}


/**
 * Registers [Mouse.Released] event with a specified button.
 *
 * Always returns false.
 */
inline fun ElementScope<*>.onRelease(button: Int = 0, crossinline block: (Mouse.Released) -> Unit) {
    element.registerEventUnit(Mouse.Released(button), block)
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
    element.registerEventUnit(Mouse.Scrolled(0f), block)
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
    element.registerEventUnit(Mouse.Moved, block)
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
// Keyboard events //
//-----------------//

/**
 * Registers [Keyboard.CodeTyped] event.
 *
 * Has an optional return value.
 */
@OverloadResolutionByLambdaReturnType
fun ElementScope<*>.onKeycodePressed(block: (Keyboard.CodeTyped) -> Boolean) {
    element.registerEvent(Keyboard.CodeTyped(), block)
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
    element.registerEventUnit(Lifetime.Initialized, block)
}

/**
 * Registers [Lifetime.Uninitialized] event.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onRemove(crossinline block: (Lifetime.Uninitialized) -> Unit) {
    element.registerEventUnit(Lifetime.Uninitialized, block)
}

//-----------------//
// Lifetime events //
//-----------------//

/**
 * Registers [Focused.Gained] event.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onFocus(crossinline block: (Focused.Gained) -> Unit) {
    element.registerEventUnit(Focused.Gained, block)
}

/**
 * Registers [Focused.Gained] event.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onFocusLost(crossinline block: (Focused.Lost) -> Unit) {
    element.registerEventUnit(Focused.Lost, block)
}

/**
 * Registers both [Focused.Gained] and [Focused.Lost] events, using the same function.
 *
 * Returns false by default.
 */
inline fun ElementScope<*>.onFocusChanged(crossinline block: () -> Unit) {
    element.registerEvent(Focused.Gained) { block(); false }
    element.registerEvent(Focused.Lost) { block(); false }
}

/**
 * Registers an event listener, which always returns false.
 *
 * If the event inherits [AuroraEvent.NonSpecific], it's [Class] will be added to [events][Element.events].
 *
 * If the event isn't a lifetime event, it will mark [acceptsInput][Element.acceptsInput] as true.
 *
 * @see Element.registerEvent
 */
inline fun <E : AuroraEvent> Element.registerEventUnit(event: E, crossinline block: (E) -> Unit) {
    registerEvent(event) {
        block(it)
        false
    }
}