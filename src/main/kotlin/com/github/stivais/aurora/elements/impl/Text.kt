package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.elements.AuroraDSL
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.renderer.data.Font
import com.github.stivais.aurora.utils.multiply

open class Text(
    string: String,
    private val font: Font,
    color: Color,
    constraints: Positions,
    size: Constraint.Size
) : Element(constraints, color) {

    init {
        constraints.height = size
    }

    open var text: String = string
        set(value) {
            if (field == value) return
            field = value
            redraw()
            previousHeight = 0f
        }

    /**
     * Flag for this text element, if it should render the text with a shadow.
     *
     * The color is 80% darker than the main color.
     */
    private var shadow: Boolean = false

    protected var previousHeight = 0f

    override fun prePosition() {
        if (previousHeight != height) {
            previousHeight = height
            if (constraints.width.undefined()) width = getTextWidth()
        }
    }

    override fun draw() {
        drawText(text, x, y, color!!.rgba)
    }

    open fun getTextWidth(): Float = textWidth(text)

    /**
     * Utility function, which draws a text, with this element's data (size/font/shadow).
     */
    protected fun drawText(string: String, x: Float = this.x, y: Float = this.y, color: Int) {
        if (shadow) {
            val offset = height / 25f
            renderer.text(string, x + offset, y + offset, height, color.multiply(0.8f), font)
        }
        renderer.text(string, x, y, height, color, font)
    }

    /**
     * Utility function, which returns a text width using this element's size/font as input.
     */
    protected fun textWidth(string: String) = renderer.textWidth(string, height, font)

    companion object {
        @AuroraDSL
        var <E : Text> ElementScope<E>.string
            get() = element.text
            set(value) { element.text = value }

        /**
         * If the text should have a shadow.
         */
        @AuroraDSL
        var <E : Text> ElementScope<E>.shadow
            get() = element.shadow
            set(value) { element.shadow = value }
    }
}