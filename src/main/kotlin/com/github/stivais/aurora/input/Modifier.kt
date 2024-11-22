@file:Suppress("UNUSED")

package com.github.stivais.aurora.input

@JvmInline
value class Modifier(val value: Byte) {
    val hasLeftShift: Boolean
        get() = value.toInt() and 0b00000001 != 0

    val hasRightShift: Boolean
        get() = value.toInt() and 0b00000010 != 0

    val hasLeftControl: Boolean
        get() = value.toInt() and 0b00000100 != 0

    val hasRightControl: Boolean
        get() = value.toInt() and 0b00001000 != 0

    val hasLeftAlt: Boolean
        get() = value.toInt() and 0b00010000 != 0

    val hasRightAlt: Boolean
        get() = value.toInt() and 0b00100000 != 0

    val hasShift: Boolean
        get() = value.toInt() and 0b00000011 != 0

    val hasControl: Boolean
        get() = value.toInt() and 0b00001100 != 0

    val hasAlt: Boolean
        get() = value.toInt() and 0b00110000 != 0

    companion object {
        @JvmStatic
        val LSHIFT: Modifier = Modifier(value = 0b00000001)

        @JvmStatic
        val RSHIFT: Modifier = Modifier(value = 0b00000010)

        @JvmStatic
        val LCTRL: Modifier = Modifier(value = 0b00000100)

        @JvmStatic
        val RCTRL: Modifier = Modifier(value = 0b00001000)

        @JvmStatic
        val LALT: Modifier = Modifier(value = 0b00010000)

        @JvmStatic
        val RALT: Modifier = Modifier(value = 0b00100000)
    }
}