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
@OverloadResolutionByLambdaReturnType
fun ElementScope<*>.onClick(button: Int = 0, block: (Mouse.Clicked) -> Boolean) {
    element.registerEvent(Mouse.Clicked(button), block)
}

@JvmName("_onClick") @OverloadResolutionByLambdaReturnType
inline fun ElementScope<*>.onClick(button: Int = 0, crossinline block: (Mouse.Clicked) -> Unit) {
    element.registerEvent(Mouse.Clicked(button)) { block(it); false } // im not sure if I want to return false or true more
}

inline fun ElementScope<*>.onRelease(button: Int = 0, crossinline block: (Mouse.Released) -> Unit) {
    element.registerEvent(Mouse.Released(button)) { block(it); false }
}

@OverloadResolutionByLambdaReturnType
fun ElementScope<*>.onScroll(amount: Float = 0f, block: (Mouse.Scrolled) -> Boolean) {
    element.registerEvent(Mouse.Scrolled(amount), block)
}

@JvmName("_onClick")
@OverloadResolutionByLambdaReturnType
inline fun ElementScope<*>.onScroll(amount: Float = 0f, crossinline block: (Mouse.Scrolled) -> Unit) {
    element.registerEvent(Mouse.Scrolled(amount)) { block(it); false } // im not sure if I want to return false or true more
}

@OverloadResolutionByLambdaReturnType
fun ElementScope<*>.onMouseMove(block: (Mouse.Moved) -> Boolean) {
    element.registerEvent(Mouse.Moved, block)
}

@JvmName("_onMouseMove")
@OverloadResolutionByLambdaReturnType
inline fun ElementScope<*>.onMouseMove(crossinline block: (Mouse.Moved) -> Unit) {
    element.registerEvent(Mouse.Moved) { block(it); false }
}

//-----------------//
// Lifetime events //
//-----------------//
/** DSl for [Lifetime.Initialized] */
inline fun ElementScope<*>.onAdd(crossinline block: (Lifetime.Initialized) -> Unit) {
    element.registerEvent(Lifetime.Initialized) { block(it); false }
}

/** DSl for [Lifetime.Uninitialized] */
inline fun ElementScope<*>.onRemove(crossinline block: (Lifetime.Uninitialized) -> Unit) {
    element.registerEvent(Lifetime.Uninitialized) { block(it); false }
}