@file:Suppress("nothing_to_inline")

package com.github.stivais.aurora.element

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.measurements.impl.Undefined
import com.github.stivais.aurora.dsl.at
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.element.impl.Block
import com.github.stivais.aurora.element.impl.Column
import com.github.stivais.aurora.element.impl.Row

open class ComponentScope<C : Component>(val component: C) {

    fun redraw() {
        component.redraw = true
    }

    inline fun block(
        at: Constraints<Constraint.Position> = at(Undefined, Undefined),
        size: Constraints<Constraint.Size>,
        color: Color,
        scope: ComponentScope<Block>.() -> Unit = {}
    ) = Block(component.aurora, at, size, color).scope(scope)

    inline fun column(
        at: Constraints<Constraint.Position>,
        size: Constraints<Constraint.Size> = size(Undefined, Undefined),
        scope: ComponentScope<Column>.() -> Unit = {}
    ) = Column(component.aurora, at, size).scope(scope)

    inline fun row(
        at: Constraints<Constraint.Position>,
        size: Constraints<Constraint.Size> = size(Undefined, Undefined),
        scope: ComponentScope<Row>.() -> Unit = {}
    ) = Row(component.aurora, at, size).scope(scope)


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