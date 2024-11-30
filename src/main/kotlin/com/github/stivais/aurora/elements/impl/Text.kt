package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.elements.AuroraDSL
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.renderer.data.Font

open class Text(
    string: String,
    val font: Font,
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

    protected var previousHeight = 0f

    override fun prePosition() {
        if (previousHeight != height) {
            previousHeight = height
            if (constraints.width is Undefined) width = getTextWidth()
        }
    }

    override fun draw() {
        renderer.text(text, x, y, height, color!!.rgba, font)
    }

    open fun getTextWidth(): Float {
        return renderer.textWidth(text, height, font)
    }

    companion object {
        @AuroraDSL
        var ElementScope<Text>.string
            get() = element.text
            set(value) { element.text = value }
    }
}