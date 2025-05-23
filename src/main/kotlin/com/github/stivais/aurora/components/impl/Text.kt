@file:Suppress("unused")

package com.github.stivais.aurora.components.impl

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.dsl.at
import com.github.stivais.aurora.dsl.percent
import com.github.stivais.aurora.components.scope.AuroraDSL
import com.github.stivais.aurora.components.Component
import com.github.stivais.aurora.components.scope.ComponentScope
import com.github.stivais.aurora.constraints.impl.measurements.Animatable
import com.github.stivais.aurora.renderer.data.Font
import com.github.stivais.aurora.utils.multiply

/**
 * # Text
 *
 * Text is an element that renders a string to the screen.
 *
 * @param size The size of the string's height.
 */
open class Text(
    string: String,
    protected val font: Font,
    color: Color,
    constraints: Positions,
    size: Constraint.Size
) : Component(constraints, color) {

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

    var animatable: Animatable? = null

    /**
     * Flag for this text element, if it should render the text with a shadow.
     *
     * The color is 65% darker than the main color.
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
        val blur = animatable?.calculateSize(this, true) ?: 0f
        if (shadow) {
            val offset = height / 25f
            renderer.text(string, x + offset, y + offset, height, color.multiply(0.65f), font, blur)
        }
        renderer.text(string, x, y, height, color, font, blur)
    }

    /**
     * Utility function, which returns a text width using this element's size/font as input.
     */
    protected fun textWidth(string: String) = renderer.textWidth(string, height, font)

    companion object {
        @AuroraDSL
        var <E : Text> ComponentScope<E>.string
            get() = component.text
            set(value) { component.text = value }

        /**
         * If the text should have a shadow.
         */
        @AuroraDSL
        var <E : Text> ComponentScope<E>.shadow
            get() = component.shadow
            set(value) { component.shadow = value }

        /**
         * Subclass of [Text], where text is supplied from a function.
         *
         * NOTE: It should only be used if text changes really often.
         */
        @AuroraDSL
        inline fun ComponentScope<*>.textSupplied(
            crossinline supplier: () -> Any?,
            font: Font = AuroraUI.defaultFont,
            color: Color = Color.WHITE,
            pos: Positions = at(),
            size: Constraint.Size = 50.percent
        ): ComponentScope<Text> = object : Text(supplier().toString(), font, color, pos, size) {
            override fun draw() {
                text = supplier().toString()
                super.draw()
            }
        }.scope { /* no-op */ }
    }
}