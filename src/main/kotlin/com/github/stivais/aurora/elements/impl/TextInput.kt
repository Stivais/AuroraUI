package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.dsl.registerEventUnit
import com.github.stivais.aurora.elements.AuroraDSL
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.events.AuroraEvent
import com.github.stivais.aurora.events.Focused
import com.github.stivais.aurora.events.Keyboard
import com.github.stivais.aurora.events.Mouse
import com.github.stivais.aurora.input.Keys
import com.github.stivais.aurora.utils.dropAt
import com.github.stivais.aurora.utils.removeRangeSafe
import com.github.stivais.aurora.utils.substringSafe

class TextInput(
    string: String,
    placeholder: String,
    positions: Positions,
    size: Constraint.Size
) : Text(string, AuroraUI.defaultFont, Color.WHITE, positions, size) {

    override var text: String = string
        set(value) {
            if (field == value) return
            val event = TextChanged(value)
            accept(event)
            if (!event.cancelled) {
                field = value
                redraw()
                previousHeight = 0f
            }
        }

    private var caret = text.length
    private var selection = text.length

    private var caretX = 0f
    private var selectionWidth = 0f

    override fun draw() {
        if (ui.eventManager.focused == this) {
            val cx = x + caretX
            renderer.line(cx, y, cx, y + height, 1f, Color.WHITE.rgba)
            if (selectionWidth != 0f) {
                renderer.rect(cx, y, selectionWidth, height, Color.RGB(0, 0, 255, 0.5f).rgba)
            }
        }
//        renderer.hollowRect(x - 5, y - 5, width + 10, height + 10, 1f, Color.WHITE.rgba)
        super.draw()
    }

    init {
        var dragging = false
        var clickCount = 1
        var lastClickTime = 0L

        registerEventUnit(Focused.Lost) {
            clearSelection()
        }
        registerEventUnit(Mouse.Clicked(0)) {
            if (ui.eventManager.focused != this) {
                ui.focus(this)
            }
            // todo: integrate this into event manager
            val current = System.currentTimeMillis()
            if (current - lastClickTime < 300) clickCount++ else clickCount = 1
            dragging = true
            lastClickTime = current

            when (clickCount) {
                1 -> {
                    caretFromMouse()
                    clearSelection()
                }
                2 -> {
                    selectWord()
                }
                3 -> {
                    selection = 0
                    caret = text.length
                    updateCaretPosition()
                }
                4 -> clickCount = 0
            }
        }
        registerEventUnit(Mouse.Released(0)) {
            dragging = false
        }
        registerEventUnit(Mouse.Moved) {
            if (dragging) {
                caretFromMouse()
            }
        }

        registerEventUnit(Keyboard.CharTyped()) { (char, mods) ->
            if (mods.hasControl && !mods.hasShift) {

               when (char) {
                   'v', 'V' -> {
                       val clipboard = ui.window?.getClipboard()
                       if (clipboard != null) insert(clipboard)
                   }
                   'c', 'C' -> {
                        if (caret != selection) {
                            ui.window?.setClipboard(text.substringSafe(caret, selection))
                        }
                   }
                   'x', 'X' -> {
                        if (caret != selection) {
                            ui.window?.setClipboard(text.substringSafe(caret, selection))
                            deleteSelection()
                        }
                   }
                   'a', 'A' -> {
                       selection = 0
                       caret = text.length
                   }
               }
            } else {
                insert(char.toString())
            }
            updateCaretPosition()
        }
        registerEventUnit(Keyboard.KeyTyped()) { (key, mods) ->
            when (key) {
                Keys.BACKSPACE -> {
                    if (selection != caret) {
                        deleteSelection()
                    } else if (!mods.hasControl) {
                        if (caret != 0) {
                            text = text.dropAt(caret, -1)
                            caret--
                        }
                    }
                    clearSelection()
                }

                Keys.DELETE -> {
                    if (selection != caret) {
                        deleteSelection()
                        clearSelection()
                    } else if (caret != text.length) {
                        text = text.dropAt(caret, 1)
                    }
                }

                Keys.RIGHT -> {
                    if (caret != text.length) {
                        caret = if (mods.hasControl) getNextSpace() else caret + 1
                        if (!mods.hasShift) selection = caret
                    }
                }
                Keys.LEFT -> {
                    if (caret != 0) {
                        caret = if (mods.hasControl) getPreviousSpace() else caret - 1
                        if (!mods.hasShift) selection = caret
                    }
                }

                Keys.HOME -> {
                    caret = 0
                    if (!mods.hasShift) selection = caret
                }
                Keys.END -> {
                    caret = text.length
                    if (!mods.hasShift) selection = caret
                }

                Keys.ESCAPE, Keys.ENTER -> {
                    ui.unfocus()
                }
                else -> {}
            }
            updateCaretPosition()
        }
    }

    /**
     * Inserts a provided string into [text].
     *
     * If there is a selection, it will replace it with the string.
     * It will remove any selection active prior.
     */
    private fun insert(string: String) {
        if (caret != selection) {
            text = text.removeRangeSafe(caret, selection)
            caret = if (selection > caret) caret else selection
        }
        val tl = text.length
        text = text.substring(0, caret) + string + text.substring(caret)
        if (text.length != tl) caret += string.length
        clearSelection()
    }

    /**
     * Deletes the selected part of the text.
     */
    private fun deleteSelection() {
        if (caret == selection) return
        text = text.removeRangeSafe(caret, selection)
        caret = if (selection > caret) caret else selection
    }

    /**
     * Updates [caret], based on mouse position on the text input.
     */
    private fun caretFromMouse() {
        val mx = ui.mx - x
        var newCaret = 0
        var currWidth = 0f

        for (index in text.indices) {
            val charWidth = renderer.textWidth(text[index].toString(), height, font)
            if ((currWidth + charWidth / 2) > mx) break
            currWidth += charWidth
            newCaret = index + 1
        }
        caret = newCaret.coerceIn(0, text.length)
        updateCaretPosition()
    }

    /**
     * Clears current selection.
     */
    private fun clearSelection() {
        selection = caret
        selectionWidth = 0f
    }

    /**
     * Updates caret's screen position.
     */
    private fun updateCaretPosition() {
        caretX = renderer.textWidth(text.substring(0, caret), height, font)
        if (selection != caret) {
            selectionWidth = renderer.textWidth(text.substringSafe(selection, caret), height, font)
            if (selection <= caret) selectionWidth *= -1
        } else {
            selectionWidth = 0f
        }
    }

    /**
     * Selects word around the caret.
     */
    private fun selectWord() {
        var start = caret
        var end = caret
        while (start > 0 && !text[start - 1].isWhitespace()) start--
        while (end < text.length && !text[end].isWhitespace()) end++

        selection = start
        caret = end
        updateCaretPosition()
    }

    /**
     * Gets index of the previous space based on the carets position.
     *
     * If the first character checked is a space it will continue until the next one.
     */
    private fun getPreviousSpace(): Int {
        var start = caret
        while (start > 0) {
            if (start != caret && text[start - 1].isWhitespace()) break
            start--
        }
        return start
    }

    /**
     * Gets index of the next space based on the carets position.
     *
     * If the first character checked is a space it will continue until the next one.
     */
    private fun getNextSpace(): Int {
        var end = caret
        while (end < text.length) {
            if (end != caret && text[end].isWhitespace()) break
            end++
        }
        return end
    }

    /**
     * Event, which gets registered when the text changes in a [TextInput].
     *
     * This event is cancellable.
     */
    data class TextChanged(val string: String = "", var cancelled: Boolean = false) : AuroraEvent.NonSpecific {
        fun cancel() {
            cancelled = true
        }
    }

    companion object {
        /**
         * Registers an event listener for [TextChanged].
         *
         * The event gets called when the text is changed inside a [TextInput] element.
         * This event is cancellable.
         */
        @AuroraDSL
        inline fun ElementScope<TextInput>.onTextChanged(crossinline block: (TextChanged) -> Unit) {
            element.registerEvent(TextChanged()) {
                block(it); false
            }
        }
     }
}