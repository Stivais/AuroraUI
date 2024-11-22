@file:Suppress("unused")

package com.github.stivais.aurora.input

/**
 * List of keys which can't be represented as a [Char].
 */
enum class Keys {
    // placeholder
    UNKNOWN,

    ESCAPE,

    F1,
    F2,
    F3,
    F4,
    F5,
    F6,
    F7,
    F8,
    F9,
    F10,
    F11,
    F12,

    ENTER,
    BACKSPACE,
    TAB,

    INSERT,
    DELETE,
    HOME,
    END,
    PAGE_UP,
    PAGE_DOWN,

    RIGHT,
    LEFT,
    DOWN,
    UP;
}