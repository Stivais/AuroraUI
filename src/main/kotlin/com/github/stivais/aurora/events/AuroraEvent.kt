package com.github.stivais.aurora.events

import com.github.stivais.aurora.input.Keys
import com.github.stivais.aurora.input.Modifiers

/**
 * # AuroraEvent
 *
 * This represents an event that can be posted to an event, mainly to handle inputs.
 *
 * @see Mouse
 */
interface AuroraEvent {
    interface NonSpecific : AuroraEvent
}

/**
 * All events for mouse inputs
 *
 * These events include:
 * [Mouse.Clicked],
 * [Mouse.Released],
 * [Mouse.Scrolled],
 * [Mouse.Moved],
 * [Mouse.Entered],
 * [Mouse.Exited],
 */
sealed interface Mouse : AuroraEvent {

    /**
     * Gets posted when the mouse is clicked
     */
    data class Clicked(val button: Int): Mouse

    /**
     * Gets posted when the mouse is released
     */
    data class Released(val button: Int): Mouse

    /**
     * Gets posted when the mouse is scrolled
     */
    class Scrolled(val amount: Float): Mouse {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            return other is Scrolled
        }

        override fun hashCode(): Int = 29791 // 31^3

        operator fun component1() = amount
    }

    /**
     * Gets posted when the mouse moves
     */
    data object Moved : Mouse

    /**
     * Gets posted when the mouse enters the element
     *
     * @see com.github.stivais.aurora.elements.Element.hovered
     */
    data object Entered : Mouse

    /**
     * Gets posted when the mouse enters the element
     *
     * @see com.github.stivais.aurora.elements.Element.hovered
     */
    data object Exited : Mouse
}

/**
 * # Lifetime
 *
 * Events, which get posted based on the lifetime of an element
 */
sealed interface Lifetime : AuroraEvent {

    /**
     * Gets posted when element is initialized
     *
     * @see com.github.stivais.aurora.elements.Element.initialize
     */
    data object Initialized : Lifetime

    /**
     * Gets posted when element is uninitalized
     */
    data object Uninitialized : Lifetime
}

sealed interface Keyboard : AuroraEvent.NonSpecific {

    class CharTyped(private val key: Char = ' ', val mods: Modifiers = Modifiers(0)) : Keyboard {
        operator fun component1() = key
    }

    class KeyTyped(private val key: Keys = Keys.UNKNOWN, val mods: Modifiers = Modifiers(0)) : Keyboard {
        operator fun component1() = key
    }
}

sealed interface Focused : AuroraEvent.NonSpecific {

    data object Gained : Focused

    data object Lost : Focused
}