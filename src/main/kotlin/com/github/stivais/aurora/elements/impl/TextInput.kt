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
import com.github.stivais.aurora.utils.multiply
import com.github.stivais.aurora.utils.removeRangeSafe
import com.github.stivais.aurora.utils.substringSafe

class TextInput(
    string: String,
    private val placeholder: String,
    positions: Positions,
    size: Constraint.Size
) : Text(string, AuroraUI.defaultFont, Color.WHITE, positions, size) {

    override var text: String = string
        set(value) {
            if (field == value) return
            val event = TextChanged(value)
            accept(event)
            if (!event.cancelled) {
                field = event.string
                parent?.redraw()
                previousHeight = 0f
            }
        }

    private var caret = text.length
    private var selection = text.length

    private var caretX = 0f
    private var selectionWidth = 0f

    private var textOffset = 0f

    override fun draw() {
        var offset = 0f
        if (ui.eventManager.focused == this) {
            offset = textOffset
            val x = x + caretX - offset

            renderer.line(x, y, x, y + height, 1f, Color.WHITE.rgba)
            if (selectionWidth != 0f) renderer.rect(x, y, selectionWidth, height, SELECTION_COLOR)
        }

        if (text.isEmpty()) {
            renderer.text(placeholder, x, y, height, color!!.rgba.multiply(0.8f), font)
        } else {
            renderer.text(text, x - offset, y, height, color!!.rgba, font)
        }
    }

    init {
        scissors = true

        var dragging = false
        var clickCount = 1
        var lastClickTime = 0L

        registerEventUnit(Focused.Lost) {
            textOffset = 0f
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
                   'w', 'W' -> {
                       selectWord()
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
                    } else if (mods.hasControl) {
                        text = ""
                        caret = 0
                    } else {
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

    override fun getTextWidth(): Float {
        return if (text.isEmpty()) renderer.textWidth(placeholder, height, font) else super.getTextWidth()
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
        val mx = ui.mx - x + textOffset
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
     *
     * Will also adjust the visible part of the text, if width is defined.
     */
    private fun updateCaretPosition() {
        if (selection != caret) {
            selectionWidth = renderer.textWidth(text.substringSafe(selection, caret), height, font)
            if (selection <= caret) selectionWidth *= -1
        } else selectionWidth = 0f

        if (caret != 0) {
            val previousX = caretX
            caretX = renderer.textWidth(text.substring(0, caret), height, font)

            if (!constraints.width.undefined()) {
                if (previousX < caretX) {
                    if (caretX - textOffset >= width) {
                        textOffset = caretX - width
                    }
                } else {
                    if (caretX - textOffset <= 0f) {
                        textOffset =  renderer.textWidth(text.substring(0, caret - 1), height, font)
                    }
                }
            }
        } else {
            caretX = 0f
            textOffset = 0f
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
     * The input is able to modify the string.
     * This event is cancellable.
     */
    data class TextChanged(var string: String = "", var cancelled: Boolean = false) : AuroraEvent.NonSpecific {
        fun cancel() {
            cancelled = true
        }

        /**
         * Modifies the event's string with the input.
         */
        fun modifyString(string: String) {
            this.string = string
        }
    }

    companion object {

        // this is equivalent to getRGBA(255, 255, 255, 0.5f)
        private const val SELECTION_COLOR: Int = -2147483393

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

        /**
         * Defines the max width a [TextInput] can be.
         *
         * When the text surpasses that width, it will start scrolling to show the part of the text the caret is over.
         */
        @AuroraDSL
        fun ElementScope<TextInput>.maxWidth(size: Constraint.Size) {
            element.constraints.width = size
        }
     }
}