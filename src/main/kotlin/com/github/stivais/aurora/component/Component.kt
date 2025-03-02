package com.github.stivais.aurora.component

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.utils.loop

/*

Position pipeline changes:

Columns/Rows will always position all its elements, except if specially defined to ignore

Columns/Rows will use a 'fast' bounding, calculating width/height based combined heights rather than relying on Bounding

Bounding will not consider position if it only has 1 element

Percent will get position based on first component that doesn't rely on children


 */
abstract class Component(
    val aurora: Aurora,
    val position: Constraints<Constraint.Position>,
    val size: Constraints<Constraint.Size>,
) {
    var parent: Component? = null

    var children: ArrayList<Component>? = null

    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f

    var internalX = 0f
    var internalY = 0f

    var redraw: Boolean = false

    open fun layout() {
        redraw = false
        children?.loop {
            it.size()
            it.position(x, y)
            it.layout()
        }
        aurora.reupload = true
    }

    open fun size() {
        width = size.first.calculate(this, 3)
        height = size.second.calculate(this, 4)
    }

    fun position(x: Float, y: Float) {
        internalX = position.first.calculate(this, 1)
        internalY = position.second.calculate(this, 2)
        this.x = internalX + x
        this.y = internalY + y
    }

    fun preRender() {
        if (redraw) {
            size()
            parent?.let {
                position(it.x, it.y)
            }
            layout()
        }
        children?.loop {
            it.preRender()
        }
    }

    open fun addComponent(component: Component) {
        if (children == null) children = arrayListOf()
        children!!.add(component)
        component.parent = this
    }

    fun measurement(type: Int): Float {
        return when (type) {
             1 -> x
             2 -> y
             3 -> width
             4 -> height
            else -> throw IllegalArgumentException("Measurement type must be between 1-4")
        }
    }
}