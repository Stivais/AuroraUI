package com.github.stivais.aurora

import com.github.stivais.aurora.events.EventManager
import com.github.stivais.aurora.input.Modifier

/**
 * # Window
 *
 * This interface is used on classes which renders/handles an [AuroraUI].
 *
 * This also contains functions which need to be implemented for certain parts of [AuroraUI] to function properly.
 *
 * To fully implement [AuroraUI] you need to implement these methods:
 *  - call [AuroraUI.initialize] when opening an [AuroraUI].
 *  - call [AuroraUI.close] when closing an [AuroraUI].
 *
 *  - call [AuroraUI.resize] when the window gets resized.
 *  - call [AuroraUI.render] every frame.
 *
 *  - callbacks for [EventManager.onMouseClick], when the mouse is pressed.
 *  - callbacks for [EventManager.onMouseRelease], when the mouse is released.
 *  - callbacks for [EventManager.onMouseMove], when the mouse is moved.
 *  - callbacks for [EventManager.onMouseScroll], when scrolled.
 *
 *  - call [EventManager.onKeycodePressed], when a key is pressed with the keycode as the input, this shouldn't block other key presses.
 *  - callbacks for [EventManager.onKeyTyped], when a key, that can be represented by a valid char, is pressed.
 *  - callbacks for [EventManager.onKeyTyped], when a key, that can't be represented by a valid char, is pressed.
 *  - callbacks for [EventManager.addModifier], when a [modifier key][Modifier] is pressed.
 *  - callbacks for [EventManager.addModifier], when a [modifier key][Modifier] is released.
 */
interface Window {

    /**
     * Gets the clipboard string.
     *
     * If data isn't a string or is empty, it will return null
     */
    fun getClipboard(): String?

    /**
     * Sets the clipboard string.
     */
    fun setClipboard(string: String?)

}