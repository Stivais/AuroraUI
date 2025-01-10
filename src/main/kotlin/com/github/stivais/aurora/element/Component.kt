package com.github.stivais.aurora.element

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.Sizes
import com.github.stivais.aurora.utils.loop

/*

Position pipeline changes:

Columns/Rows will always position all its elements, except if specially defined to ignore

Columns/Rows will use a 'fast' bounding, calculating width/height based combined heights rather than relying on Bounding

Bounding will not consider position if it only has 1 element

Percent will get position based on first component that doesn't rely on children


 */
abstract class Component(
    val ui: Aurora,

    val positions: Positions,
    val size: Sizes,
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
        ui.reupload = true
    }

    open fun size() {
        width = size.width.calculate(this, 2)
        height = size.height.calculate(this, 3)
    }

    fun position(x: Float, y: Float) {
        internalX = positions.x.calculate(this, 0)
        internalY = positions.y.calculate(this, 1)
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
}