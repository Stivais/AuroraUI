package com.github.stivais.aurora.elements

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.impl.positions.Center
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.dsl.at
import com.github.stivais.aurora.dsl.percent
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.elements.impl.*
import com.github.stivais.aurora.elements.impl.layout.Column
import com.github.stivais.aurora.elements.impl.layout.Row
import com.github.stivais.aurora.renderer.data.Font
import com.github.stivais.aurora.renderer.data.Gradient
import com.github.stivais.aurora.renderer.data.Image
import com.github.stivais.aurora.renderer.data.Radii

class ElementScope<E : Element>(val element: E) {

    inline val ui get() = element.ui

    @DSL
    inline fun block(
        constraints: Constraints,
        color: Color,
        radius: Radii? = null,
        block: ElementScope<Block>.() -> Unit = {}
    ) = Block(constraints, color, radius).scope(block)

    @DSL
    inline fun block(
        constraints: Constraints,
        colors: Pair<Color, Color>,
        gradient: Gradient,
        radius: Radii? = null,
        block: ElementScope<Block.Gradient>.() -> Unit = {}
    ) = Block.Gradient(constraints, colors.first, colors.second,gradient, radius).scope(block)

    @DSL
    inline fun text(
        string: String,
        font: Font = AuroraUI.defaultFont,
        color: Color = Color.WHITE,
        pos: Positions = at(Center, Center),
        size: Constraint.Size = 50.percent,
        block: ElementScope<Text>.() -> Unit = {}
    ) = Text(string, font, color, pos, size).scope(block)

    @DSL
    inline fun image(
        image: Image,
        constraints: Constraints,
        radius: Radii? = null,
        block: ElementScope<ImageElement>.() -> Unit = {}
    ) = ImageElement(image, constraints, radius).scope(block)

    @DSL
    inline fun column(
        constraints: Constraints = size(Bounding, Bounding),
        padding: Constraint.Size? = null,
        block: ElementScope<Column>.() -> Unit = {}
    ) = Column(constraints, padding).scope(block)

    @DSL
    inline fun row(
        constraints: Constraints = size(Bounding, Bounding),
        padding: Constraint.Size? = null,
        block: ElementScope<Row>.() -> Unit = {}
    ) = Row(constraints, padding).scope(block)

    @DSL
    inline fun group(
        constraints: Constraints = size(Bounding, Bounding),
        block: ElementScope<Group>.() -> Unit = {}
    ) = Group(constraints).scope(block)

    @DSL
    inline fun scrollable(
        constraints: Constraints = size(Bounding, Bounding),
        block: ElementScope<Scrollable>.() -> Unit
    ) = Scrollable(constraints).scope(block)

    inline fun <E : Element> E.scope(block: ElementScope<E>.() -> Unit): ElementScope<E> {
        element.addElement(this)
        val scope = ElementScope(this)
        scope.block()
        return scope
    }
}

@DslMarker
annotation class DSL