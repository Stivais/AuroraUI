@file:Suppress("UNUSED")

package com.github.stivais.aurora.input

@JvmInline
value class Modifier(val value: Byte) {
    val hasShift: Boolean
        get() = value.toInt() and 0b00000011 != 0

    val hasControl: Boolean
        get() = value.toInt() and 0b00001100 != 0

    val hasAlt: Boolean
        get() = value.toInt() and 0b00110000 != 0
}