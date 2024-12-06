@file:Suppress("UNUSED")

package com.github.stivais.aurora.input

/**
 * # Modifier
 *
 * Represents whenever the user is holding down certain keys (Shift, Ctrl, Alt).
 */
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
        /** Static modifier representing LSHIFT. */
        @JvmStatic
        val LSHIFT: Modifier = Modifier(value = 0b00000001)

        /** Static modifier representing RSHIFT. */
        @JvmStatic
        val RSHIFT: Modifier = Modifier(value = 0b00000010)

        /** Static modifier representing LCTRL. */
        @JvmStatic
        val LCTRL: Modifier = Modifier(value = 0b00000100)

        /** Static modifier representing RCTRL. */
        @JvmStatic
        val RCTRL: Modifier = Modifier(value = 0b00001000)

        /** Static modifier representing LALT. */
        @JvmStatic
        val LALT: Modifier = Modifier(value = 0b00010000)

        /** Static modifier representing RALT. */
        @JvmStatic
        val RALT: Modifier = Modifier(value = 0b00100000)
    }
}