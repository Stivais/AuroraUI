package com.github.stivais.aurora.input

// needs implementation
// figure out bit layout for it
// in event manager handle creating a modifier
@JvmInline
value class Modifiers(val value: Byte) {
    val hasShift: Boolean
        get() = false

    val hasControl: Boolean
        get() = false

    val hasAlt: Boolean
        get() = false
}