package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.events.AuroraEvent
import com.github.stivais.aurora.events.Focused
import com.github.stivais.aurora.events.Keyboard
import com.github.stivais.aurora.events.Mouse
import com.github.stivais.aurora.renderer.data.Font
import kotlin.math.max
import kotlin.math.min


/*
Unfinished - (syntax guidelines since I don't want ugly code)

Don't make functions that only get used in place and if it's small
Don't make censored text input (that can be done outside)
Don't make numbers only (that can be done inside the event)

If making offset, make it only happen if width is not Undefined,

Document if possible

 */
class TextInput(
    default: String,
    font: Font,
    color: Color,
    positions: Positions,
    size: Constraint.Size,
) : Text(default, font, color, positions, size) {

    private var caret = text.length
        set(value) {
            field = value.coerceIn(0, text.length)
            updateCaretPos()
        }

    private var selection = caret
        set(value) {
            field = value.coerceIn(0, text.length)
            updateSelectionPos()
        }

    private var caretX: Float = 0f
    private var selectionX: Float = 0f

    // all events need to return false
    init {
        // anything that only gets used inside events like this should be inside init block:
        var dragging = false

        registerEvent(Mouse.Clicked(0)) {
            dragging = true
            false
        }
        registerEvent(Mouse.Moved) {
            if (dragging) {}
            false
        }
        registerEvent(Mouse.Released(0)) {
            dragging = false
            false
        }

        registerEvent(Focused.Lost) {
            false
        }

        // anything for when characters, for checking for stuff like copy paste:
        // check with Modifiers (not implemented tho)
        // dont make a new function just to only use inside here
        registerEvent(Keyboard.CharTyped()) { (char, mods) ->
            println("$char ${mods.hasControl}")

//            if (caret > text.length) caret = text.length
//            insert(char.toString())
            false
        }

        // use for anything that uses Keys enum, utilize modifiers to check
        registerEvent(Keyboard.KeyTyped()) { (key) ->
            false
        }
    }

    override fun draw() {
//        renderer.
        super.draw()
    }

    private fun insert(string: String) {
        val start = min(caret, selection).coerceIn(0, text.length)
        val end = max(caret, selection).coerceIn(0, text.length)

        text = text.substring(0, start) + string + text.substring(end)
        caret = start + string.length
        selection = caret
    }

    private fun updateCaretPos() {
        caretX = renderer.textWidth(text.substring(0, caret), height, font)
    }

    private fun updateSelectionPos() {
        selectionX = renderer.textWidth(text.substring(0, selection), height, font)
    }

    private fun caretFromMouse() {
    }

    data class TextChanged(val string: String) : AuroraEvent.NonSpecific
}