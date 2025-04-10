@file:Suppress("nothing_to_inline")

package com.github.stivais.aurora.components.scope

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.components.Component
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.dsl.at
import com.github.stivais.aurora.dsl.percent
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.components.impl.*
import com.github.stivais.aurora.components.impl.TextInput.Companion.onTextChanged
import com.github.stivais.aurora.components.impl.layout.Column
import com.github.stivais.aurora.components.impl.layout.Row
import com.github.stivais.aurora.operations.Operation
import com.github.stivais.aurora.renderer.data.Font
import com.github.stivais.aurora.renderer.data.Gradient
import com.github.stivais.aurora.renderer.data.Image
import com.github.stivais.aurora.renderer.data.Radii
import com.github.stivais.aurora.transforms.Transform

/**
 * # ElementScope
 *
 * **ElementScope** is a class that utilizes ***[Kotlin DSL](https://kotlinlang.org/docs/type-safe-builders.html)***
 * to create element hierarchies, while also hiding away the internals.
 *
 * This class contains DSL for all the base elements provided in Aurora, such as [block], [text] or [column].
 *
 * To make your own function, it is best to make an extension function with this class,
 * this will allow you to use these functions inside an ElementScope block.
 *
 * For example, you can make a function which creates a button:
 * ```
 *  fun ElementScope<*>.button(title: String) {
 *      block(size(Bounding, Bounding), Color.BLUE) {
 *          text(
 *              string = title,
 *              size = 20.px
 *          )
 *          onClick {
 *              println("Clicked this button!")
 *          }
 *      }
 *  }
 * ```
 * This creates a [Block], whose size is based on the [Text] inside,
 * and when clicked it prints "Clicked this button!"
 */
open class ComponentScope<E : Component>(val component: E) {

    /**
     * Gets the current [ui][AuroraUI].
     */
    inline val ui: AuroraUI
        get() = component.ui

    /**
     * Backing property for [Component.enabled].
     */
    inline var enabled: Boolean
        get() = component.enabled
        set(value) {
            component.enabled = value
        }

    /**
     * Tells the first element that doesn't have a size constraint,
     * which relies on children, to redraw its hierarchy.
     */
    inline fun redraw() {
        val element = component.parent ?: component
        element.redraw()
    }

    /**
     * Adds a [Block] to this current scope.
     */
    @AuroraDSL
    inline fun block(
        constraints: Constraints,
        color: Color,
        radius: Radii? = null,
        block: ComponentScope<Block>.() -> Unit = {}
    ) = Block(constraints, color, radius).scope(block)

    /**
     * Adds a [Block.Gradient] to this current scope.
     */
    @AuroraDSL
    inline fun block(
        constraints: Constraints,
        colors: Pair<Color, Color>,
        gradient: Gradient,
        radius: Radii? = null,
        block: ComponentScope<Block.Gradient>.() -> Unit = {}
    ) = Block.Gradient(constraints, colors.first, colors.second,gradient, radius).scope(block)

    /**
     * Adds a [Text] to this current scope.
     *
     * ***NOTE:*** Default size is 50.percent,
     * so it is a possibility that the size maybe off if it's parent's size is [Bounding].
     */
    @AuroraDSL
    inline fun text(
        string: String,
        font: Font = AuroraUI.defaultFont,
        color: Color = Color.WHITE,
        pos: Positions = at(Undefined, Undefined),
        size: Constraint.Size = 50.percent,
        block: ComponentScope<Text>.() -> Unit = {}
    ) = Text(string, font, color, pos, size).scope(block)

    /**
     * Adds an [Image] to this current scope.
     */
    @AuroraDSL
    inline fun image(
        image: Image,
        constraints: Constraints,
        radius: Radii? = null,
        block: ComponentScope<ImageElement>.() -> Unit = {}
    ) = ImageElement(image, constraints, radius).scope(block)

    /**
     * Adds a [Column] to this current scope.
     */
    @AuroraDSL
    inline fun column(
        constraints: Constraints = size(Bounding, Bounding),
        padding: Constraint.Size? = null,
        block: ComponentScope<Column>.() -> Unit = {}
    ) = Column(constraints, padding).scope(block)

    /**
     * Adds a [Row] to this current scope.
     */
    @AuroraDSL
    inline fun row(
        constraints: Constraints = size(Bounding, Bounding),
        padding: Constraint.Size? = null,
        block: ComponentScope<Row>.() -> Unit = {}
    ) = Row(constraints, padding).scope(block)

    /**
     * Adds a [Group] to this current scope.
     */
    @AuroraDSL
    inline fun group(
        constraints: Constraints = size(Bounding, Bounding),
        block: ComponentScope<Group>.() -> Unit = {}
    ) = Group(constraints).scope(block)

    /**
     * Adds a [Scrollable] to this current scope.
     */
    @AuroraDSL
    inline fun scrollable(
        constraints: Constraints = size(Bounding, Bounding),
        block: ComponentScope<Scrollable>.() -> Unit
    ) = Scrollable(constraints).scope(block)

    /**
     * Adds a [TextInput] to this current scope.
     *
     * If you want to do stuff when the string changes, use [TextInput.onTextChanged] inside of [Block].
     *
     * ***NOTE:*** Default size is 50.percent,
     * so it is a possibility that the size maybe off if it's parent's size is [Bounding].
     */
    @AuroraDSL
    inline fun textInput(
        string: String = "",
        placeholder: String = "",
        font: Font = AuroraUI.defaultFont,
        color: Color = Color.WHITE,
        pos: Positions = at(Undefined, Undefined),
        size: Constraint.Size = 50.percent,
        block: ComponentScope<TextInput>.() -> Unit
    ) = TextInput(string, placeholder, font, color, pos, size).scope(block)

    /**
     * Extension function version of [createScope].
     */
    inline fun <E : Component> E.scope(block: ComponentScope<E>.() -> Unit) = createScope(this, block)

    /**
     * Adds an element to the current scope and creates a new one from it.
     */
    inline fun <E : Component> createScope(element: E, block: ComponentScope<E>.() -> Unit): ComponentScope<E> {
        this.component.addComponent(element)
        val scope = ComponentScope(element)
        scope.block()
        element.initialize()
        return scope
    }

    /**
     * Adds an element to the current scope's element.
     */
    inline fun <E : Component> E.add(): E {
        component.addComponent(this)
        component.initialize()
        return this
    }


    /**
     * Registers an [Operation] to the [ui].
     */
    fun operation(operation: Operation) = component.ui.addOperation(operation)

    /**
     * Registers a [Transform] to the element in this scope.
     */
    fun transform(transform: Transform) = component.addTransform(transform)
}
