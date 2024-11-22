package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.events.AuroraEvent
import com.github.stivais.aurora.events.Focused
import com.github.stivais.aurora.events.Keyboard
import com.github.stivais.aurora.events.Mouse
import com.github.stivais.aurora.input.Keys
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
            caretX = renderer.textWidth(text.substring(0, value), height, font)
        }
    private var selection = caret
        set(value) {
            field = value.coerceIn(0, text.length)
            selectionX = renderer.textWidth(text.substring(0, value), height, font)
        }

    private var caretX: Float = 0f
    private var selectionX: Float = 0f
    private var offset = 0f

    private var lastBlinkTime = System.currentTimeMillis()
    private var cursorVisible = true
    private val cursorBlinkRate = 500L

    init {
        var dragging = false
        var lastClickTime = 0L
        var clickCount = 0

        registerEvent(Mouse.Clicked(0)) {
            dragging = true

            val current = System.currentTimeMillis()
            if (current - lastClickTime < 300) clickCount++ else clickCount = 1
            lastClickTime = current

            when (clickCount) {
                1 -> {
                    // Inline caretFromMouse
                    val mouseX = ui.mx - x + offset
                    var newPos = 0
                    var currentWidth = 0f

                    for (i in text.indices) {
                        val charWidth = renderer.textWidth(text[i].toString(), height, font)
                        if (currentWidth + (charWidth / 2) > mouseX) break
                        currentWidth += charWidth
                        newPos = i + 1
                    }

                    caret = newPos
                    //if (!Modifier.SHIFT.isPressed) selection = caret you dont have modifiers in this event..
                }
                2 -> {
                    // Inline selectWord
                    var start = caret
                    var end = caret

                    while (start > 0 && !text[start - 1].isWhitespace()) start--
                    while (end < text.length && !text[end].isWhitespace()) end++

                    selection = start
                    caret = end
                }
                3 -> {
                    selection = 0
                    caret = text.length
                }
            }
            false
        }

        registerEvent(Mouse.Moved) {
            if (dragging) {
                val mouseX = ui.mx - x + offset
                var newPos = 0
                var currentWidth = 0f

                for (i in text.indices) {
                    val charWidth = renderer.textWidth(text[i].toString(), height, font)
                    if (currentWidth + (charWidth / 2) > mouseX) break
                    currentWidth += charWidth
                    newPos = i + 1
                }

                caret = newPos
//                if (!Modifiers.SHIFT.isPressed) { you dont have modifiers in this event..
//                    selection = caret
//                }
                true
            } else {
                lastClickTime = 0L
                false
            }
        }

        registerEvent(Mouse.Released(0)) {
            dragging = false
            false
        }

        registerEvent(Focused.Lost) {
            selection = caret
            false
        }

        registerEvent(Keyboard.CharTyped()) { (char) -> // filter allowed characters
            val start = min(caret, selection).coerceIn(0, text.length)
            val end = max(caret, selection).coerceIn(0, text.length)
            text = text.substring(0, start) + char + text.substring(end)
            caret = start + 1
            selection = caret
            false
        }

        registerEvent(Keyboard.KeyTyped()) { (key, modifier) ->
            when (key) {
                Keys.LEFT -> {
                    if (modifier.hasControl) {
                        var newPos = caret
                        while (newPos > 0 && text[newPos - 1].isWhitespace()) newPos--
                        while (newPos > 0 && !text[newPos - 1].isWhitespace()) newPos--
                        if (modifier.hasShift) caret = newPos else {
                            caret = newPos
                            selection = caret
                        }
                    } else {
                        if (modifier.hasShift) caret-- else {
                            caret--
                            selection = caret
                        }
                    }
                }
                Keys.RIGHT -> {
                    if (modifier.hasControl) {
                        var newPos = caret
                        while (newPos < text.length && text[newPos].isWhitespace()) newPos++
                        while (newPos < text.length && !text[newPos].isWhitespace()) newPos++
                        if (modifier.hasShift) caret = newPos else {
                            caret = newPos
                            selection = caret
                        }
                    } else {
                        if (modifier.hasShift) caret++ else {
                            caret++
                            selection = caret
                        }
                    }
                }
                Keys.HOME -> {
                    if (modifier.hasShift) caret = 0 else {
                        caret = 0
                        selection = 0
                    }
                }
                Keys.END -> {
                    if (modifier.hasShift) caret = text.length else {
                        caret = text.length
                        selection = text.length
                    }
                }
                Keys.BACKSPACE -> {
                    if (selection != caret) {
                        val start = min(caret, selection)
                        val end = max(caret, selection)
                        text = text.substring(0, start) + text.substring(end)
                        caret = start
                        selection = caret
                    } else if (caret > 0) {
                        if (modifier.hasControl) {
                            var newPos = caret
                            while (newPos > 0 && text[newPos - 1].isWhitespace()) newPos--
                            while (newPos > 0 && !text[newPos - 1].isWhitespace()) newPos--
                            text = text.substring(0, newPos) + text.substring(caret)
                            caret = newPos
                        } else {
                            text = text.substring(0, caret - 1) + text.substring(caret)
                            caret--
                        }
                        selection = caret
                    }
                }
                Keys.DELETE -> {
                    if (selection != caret) {
                        val start = min(caret, selection)
                        val end = max(caret, selection)
                        text = text.substring(0, start) + text.substring(end)
                        caret = start
                        selection = caret
                    } else if (caret < text.length) {
                        if (modifier.hasControl) {
                            var newPos = caret
                            while (newPos < text.length && text[newPos].isWhitespace()) newPos++
                            while (newPos < text.length && !text[newPos].isWhitespace()) newPos++
                            text = text.substring(0, caret) + text.substring(newPos)
                        } else {
                            text = text.substring(0, caret) + text.substring(caret + 1)
                        }
                    }
                }

                else -> {}
            }

            /*if (modifier.hasControl) {
                when (key) {
                    Keys.A -> {
                        selection = 0
                        caret = text.length
                    }
                    Keys.C -> {
                        if (selection != caret) {
                            val start = min(selection, caret)
                            val end = max(selection, caret)
                            GuiScreen.setClipboardString(text.substring(start, end))
                        }
                    }
                    Keys.V -> {
                        val clipboard = GuiScreen.getClipboardString()
                        if (clipboard.isNotEmpty()) {
                            val start = min(caret, selection).coerceIn(0, text.length)
                            val end = max(caret, selection).coerceIn(0, text.length)
                            text = text.substring(0, start) + clipboard + text.substring(end)
                            caret = start + clipboard.length
                            selection = caret
                        }
                    }
                    Keys.X -> {
                        if (selection != caret) {
                            val start = min(selection, caret)
                            val end = max(selection, caret)
                            GuiScreen.setClipboardString(text.substring(start, end))
                            text = text.substring(0, start) + text.substring(end)
                            caret = start
                            selection = caret
                        }
                    }
                    else -> {}
                }
            }*/
            false
        }
    }

    override fun draw() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBlinkTime > cursorBlinkRate) {
            cursorVisible = !cursorVisible
            lastBlinkTime = currentTime
            //redraw = true
        }

        if (selection != caret) {
            val startX = x + min(selectionX, caretX)
            val endX = x + max(selectionX, caretX)
            renderer.rect(startX - offset, y + 2, endX - startX, height - 2, Color.RGB(0, 120, 215, 0.4f).rgba)
        }

        renderer.text(text, x - offset, y, height, color!!.rgba, font) // should we assert this?

        if (/*ui.isFocused(this) &&*/ cursorVisible) { // only draw if focused
            renderer.rect(x + caretX - offset, y + 2, 1f, height - 4, Color.WHITE.rgba)
        }
    }

    /*override fun preSize() {
        super.preSize()
        if (size != Size.Undefined) {
            val textWidth = renderer.textWidth(text, height, font)
            if (textWidth > width) {
                if (caretX - offset < 0) {
                    offset = caretX
                } else if (caretX - offset > width) {
                    offset = caretX - width
                }
            } else {
                offset = 0f
            }
        }
    }*/

    data class TextChanged(val string: String) : AuroraEvent.NonSpecific
}