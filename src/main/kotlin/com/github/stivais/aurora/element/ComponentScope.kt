@file:Suppress("nothing_to_inline")

package com.github.stivais.aurora.element

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.Sizes
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.element.impl.Block
import com.github.stivais.aurora.element.impl.Column
import com.github.stivais.aurora.element.impl.Row

open class ComponentScope<C : Component>(val component: C) {

    fun redraw() {
        component.redraw = true
    }

    inline fun block(
        at: Positions = Positions(Undefined, Undefined),
        size: Sizes,
        color: Color,
        scope: ComponentScope<Block>.() -> Unit = {}
    ) = Block(component.ui, at, size, color).scope(scope)

    inline fun column(
        at: Positions,
        size: Sizes = Sizes(Undefined, Undefined),
        scope: ComponentScope<Column>.() -> Unit = {}
    ) = Column(component.ui, at, size).scope(scope)

    inline fun row(
        at: Positions,
        size: Sizes = Sizes(Undefined, Undefined),
        scope: ComponentScope<Row>.() -> Unit = {}
    ) = Row(component.ui, at, size).scope(scope)


    inline fun <C : Component> C.scope(block: ComponentScope<C>.() -> Unit): ComponentScope<C> {
        this.add()
        val scope = ComponentScope(this)
        scope.block()
        return scope
    }

    inline fun <C : Component> C.add() {
        component.addComponent(this)
    }

}