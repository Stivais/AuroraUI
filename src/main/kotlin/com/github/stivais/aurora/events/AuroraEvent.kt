package com.github.stivais.aurora.events

import com.github.stivais.aurora.input.Keys
import com.github.stivais.aurora.input.Modifier

/**
 * # AuroraEvent
 *
 * This represents an event that can be posted to an event, mainly to handle inputs.
 *
 * @see Mouse
 */
interface AuroraEvent {

    /**
     * # AuroraEvent.NonSpecific
     *
     * This is an extension for [AuroraEvent],
     * which indicates that only the type of event needs to match when being posted.
     *
     * @see com.github.stivais.aurora.elements.Element.registerEvent
     */
    interface NonSpecific : AuroraEvent
}

/**
 * # Mouse *Events*
 *
 * All events for mouse inputs.
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
     * Gets posted when the mouse is clicked with the specified button.
     */
    data class Clicked(val button: Int): Mouse {

        /**
         * Alternative for [Clicked], which accepts any button.
         */
        data class NonSpecific(val button: Int) : Mouse, AuroraEvent.NonSpecific
    }

    /**
     * Gets posted when the mouse is released.
     */
    data class Released(val button: Int): Mouse

    /**
     * Gets posted when the mouse is scrolled.
     *
     * Note: [amount] should always be between -1f to 1f
     */
    data class Scrolled(val amount: Float): Mouse, AuroraEvent.NonSpecific

    /**
     * Gets posted when the mouse moves.
     */
    data object Moved : Mouse

    /**
     * Gets posted when the mouse enters the element.
     *
     * @see com.github.stivais.aurora.elements.Element.hovered
     */
    data object Entered : Mouse

    /**
     * Gets posted when the mouse enters the element.
     *
     * @see com.github.stivais.aurora.elements.Element.hovered
     */
    data object Exited : Mouse
}

/**
 * # Lifetime *Events*
 *
 * Events, which get posted based on the lifetime of an element.
 *
 * Implements [AuroraEvent.NonSpecific].
 */
sealed interface Lifetime : AuroraEvent.NonSpecific {

    /**
     * Gets posted when element is initialized.
     *
     * @see com.github.stivais.aurora.elements.Element.initialize
     */
    data object Initialized : Lifetime

    /**
     * Gets posted when element is uninitialized.
     */
    data object Uninitialized : Lifetime
}

/**
 * # Keyboard *Events*
 *
 * Events for keyboard inputs.
 *
 * Implements [AuroraEvent.NonSpecific].
 *
 * These events include:
 * [Keyboard.CharTyped],
 * [Keyboard.KeyTyped],
 */
sealed interface Keyboard : AuroraEvent.NonSpecific {

    data class CharTyped(val key: Char = ' ', val mods: Modifier = Modifier(0)) : Keyboard

    data class KeyTyped(val key: Keys = Keys.UNKNOWN, val mods: Modifier = Modifier(0)) : Keyboard

    data class CodeTyped(val code: Int = 0, val mods: Modifier = Modifier(0)) : Keyboard
}

/**
 * # Focused *Events*
 *
 * Events for when an element is focused or not.
 *
 * Implements [AuroraEvent.NonSpecific].
 *
 * These events include:
 * [Focused.Gained],
 * [Focused.Lost],
 */
sealed interface Focused : AuroraEvent.NonSpecific {

    /**
     * Gets posted when an element is focused.
     *
     * @see EventManager.focused
     */
    data object Gained : Focused

    /**
     * Gets posted when an element is unfocused.
     *
     * @see EventManager.focused
     */
    data object Lost : Focused
}