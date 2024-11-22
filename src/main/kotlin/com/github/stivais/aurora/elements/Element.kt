package com.github.stivais.aurora.elements

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.constraints.impl.positions.Center
import com.github.stivais.aurora.events.AuroraEvent
import com.github.stivais.aurora.events.Lifetime
import com.github.stivais.aurora.events.Mouse
import com.github.stivais.aurora.transforms.Transform
import com.github.stivais.aurora.utils.loop

/**
 * # Element
 *
 * This class controls everything that appears inside a UI.
 *
 * @param constraints Handles how this element is positioned and sized.
 */
abstract class Element(
    val constraints: Constraints,
    var color: Color? = null,
) {

    /**
     * Reference to the UI instance holding this element
     */
    lateinit var ui: AuroraUI

    /**
     * Current [renderer][com.github.stivais.aurora.renderer.Renderer] used to draw to the screen.
     */
    inline val renderer get() = ui.renderer

    /**
     * Reference to the parent element holding this element.
     */
    var parent: Element? = null

    /**
     * Reference to the children elements under this element.
     */
    var children: ArrayList<Element>? = null

    /**
     * Event handler for this element.
     *
     * The key *should* be either [AuroraEvent] or [Class] representing an event.
     *
     * @see accept
     */
    private var events: HashMap<Any, ArrayList<(AuroraEvent) -> Boolean>>? = null

    /**
     * Flag to indicate if this element contains any input-related events
     */
    var acceptsInput = false

    /**
     * Flag to indicate if the mouse is hovered over this element
     *
     * This also handles the [Mouse.Entered] and [Mouse.Exited] events
     */
    var hovered: Boolean = false
        set(value) {
            if (field == value) return
            if (value) accept(Mouse.Entered) else accept(Mouse.Exited)
            field = value
        }

    var x = 0f
    var y = 0f
    var width = 0f
    var height = 0f

    var internalX: Float = 0f
    var internalY: Float = 0f

    /**
     * List of [Transform] to apply to this element.
     *
     * If this is null, there is no transforms to apply.
     */
    var transforms: ArrayList<Transform>? = null

    /**
     * Current x scale multiplier.
     */
    var scaleX: Float = 1f

    /**
     * Current y scale multiplier.
     */
    var scaleY: Float = 1f

    /**
     * If the element should redraw on next frame.
     */
    var redraw = false

    var renders: Boolean = true
        get() = field && enabled
        private set

    open var enabled: Boolean = true

    var scissors: Boolean = false

    /**
     * Initializes this element.
     */
    fun initialize() {
        accept(Lifetime.Initialized)
        children?.loop {
            it.initialize()
        }
    }

    /**
     * This renders the elements contents.
     */
    abstract fun draw()

    /**
     * Renders this element and all elements under it.
     *
     * It will reposition it if [redraw] is true.
     */
    fun render() {
        if (redraw) {
            redraw = false
            size()
            positionChildren()
            clip()
        }
        if (renders) {
            renderer.push()
            if (scissors) renderer.pushScissor(x, y, width, height)
            transforms?.loop {
                it.apply(element = this, renderer)
            }
            draw()
            children?.loop {
                it.render()
            }
//            if (hovered) renderer.hollowRect(x, y, width, height, 1f, Color.WHITE.rgba)
            if (scissors) renderer.popScissor()
            renderer.pop()
        }
    }

    /**
     * Marks the element so it can redraw it.
     *
     * If the element's parent size relies on children, it will redraw it.
     */
    fun redraw() {
        var p = parent
        while (p != null) {
            if (!p.constraints.sizeReliesOnChildren()) break
            p = p.parent
        }
        p?.redraw = true
    }

    /**
     * First stage of positioning pipeline.
     *
     * Sizes the current element, and does that for all elements under this one.
     *
     * If the size constraint [relies on its element's children][com.github.stivais.aurora.constraints.Constraint.Size.reliesOnChildren],
     * it will wait until after positioning to size itself.
     *
     * @see positionChildren
     * @see postSize
     */
    fun size() {
        if (!enabled) return
        if (!constraints.width.reliesOnChildren()) width = constraints.width.calculateSize(this, true)
        if (!constraints.height.reliesOnChildren()) height = constraints.height.calculateSize(this, false)
        children?.loop {
            it.size()
        }
    }

    /**
     * Gets ran at the start of [positionChildren].
     *
     * @see positionChildren
     */
    open fun prePosition() {}

    /**
     * Gets ran at the end of [positionChildren].
     *
     * @see positionChildren
     */
    open fun postPosition() {}

    /**
     * Second stage of the positioning pipeline.
     *
     * Positions the elements children, and does it for all elements under this one.
     *
     * @param continue If it should continue positioning to its children.
     * @see size
     * @see position
     * @see postSize
     */
    fun positionChildren(`continue`: Boolean = true) {
        if (!enabled) return
        prePosition()
        children?.loop {
            position(it, x, y)
            it.positionChildren(`continue`)
        }
        postSize(`continue`)
        postPosition()
    }

    open fun position(element: Element, newX: Float, newY: Float) {
        element.internalX = element.constraints.x.calculatePos(element, true)
        element.internalY = element.constraints.y.calculatePos(element, false)
        element.x =  element.internalX + newX
        element.y =  element.internalY + newY
    }

    /**
     * "Final" stage of the positioning pipeline.
     *
     * Sizes this element if a size constraint [relies on its element's children][com.github.stivais.aurora.constraints.Constraint.Size.reliesOnChildren],
     * and then it corrects its position, Note: It causes all elements (starting from this parent) to be repositioned again.
     *
     * @param continue Used to prevent a stackoverflow.
     * @see positionChildren
     */
    private fun postSize(`continue`: Boolean = true) {
        val widthRelies = constraints.width.reliesOnChildren()
        val heightRelies = constraints.height.reliesOnChildren()
        if (widthRelies) width = constraints.width.calculateSize(this, true)
        if (heightRelies) height = constraints.height.calculateSize(this, false)

        if ((widthRelies || heightRelies) && `continue`) {
            size()
            parent?.positionChildren(false)
        }
    }

    /**
     * Disables rendering for any element outside of it's parent element.
     */
    fun clip() {
        children?.loop {
            it.renders = it.intersects(x, y, width, height) && !(it.width == 0f && it.height == 0f)
            if (it.renders) {
                it.clip()
            }
        }
    }

    /**
     * Adds an element to this element's children.
     *
     * If this element is already initialized, it will initialize the added element.
     */
    fun addElement(element: Element) {
        if (children == null) children = arrayListOf()
        children!!.add(element)
        element.parent = this
        element.constraints.apply {
            val (newX, newY) = getDefaultPositions()
            if (x is Undefined) x = newX
            if (y is Undefined) y = newY
        }
        element.ui = ui
        element.initialize()
        redraw = true
    }

    fun removeElement(element: Element?) {
        if (element == null || children.isNullOrEmpty()) return
        children!!.remove(element)
        element.parent = null
        ui.eventManager.postToAll(Lifetime.Uninitialized, element)
    }

    /**
     * Gets default the [Positions][Constraint.Position] for when an element is added and its positions are undefined.
     */
    open fun getDefaultPositions(): Pair<Constraint.Position, Constraint.Position> = Pair(Center, Center)

    /**
     * Checks if this element has events matching the input, if so it will loop through all functions for it.
     *
     * @return true if event should be consumed.
     */
    fun accept(event: AuroraEvent): Boolean {
        if (events != null) {
            // if i ever make mouse clicks focusable, find a way to separate them while using same class
            val action = when (event) {
                is AuroraEvent.NonSpecific -> events!![event::class.java] ?: events!![event]
                else -> events!![event]
            } ?: return false
            action.loop { if (it(event)) return true }
        }
        return false
    }

    /**
     * Checks if this element has events matching the class, if so it will loop through all functions for it.
     *
     * @return true if event should be consumed.
     */
    fun acceptFocused(event: AuroraEvent): Boolean {
        if (events != null) {
            val action = events!![event::class.java] ?: return false
            action.loop { if (it(event)) return true }
        }
        return false
    }

    /**
     * Registers an event to this element.
     *
     * If the event inherits [AuroraEvent.NonSpecific], it's [Class] will be added to [events].
     *
     * If the event isn't a lifetime event, it will mark [acceptsInput] as true.
     */
    @Suppress("UNCHECKED_CAST")
    fun <E : AuroraEvent> registerEvent(event: E, block: (E) -> Boolean) {
        acceptsInput = true
        if (events == null) {
            events = hashMapOf()
        }
        val key: Any = if (event is AuroraEvent.NonSpecific) event::class.java else event
        events!!.getOrPut(key) { arrayListOf() }.add(block as (AuroraEvent) -> Boolean)
    }

    /**
     * Registers a [Transform] to this element.
     */
    fun addTransform(transform: Transform) {
        if (transforms == null) transforms = arrayListOf()
        transforms!!.add(transform)
    }

    // constraint util, maybe somewhere el;se
    fun getSize(horizontal: Boolean) = (if (horizontal) width else height)
    fun getPosition(horizontal: Boolean) = if (horizontal) internalX else internalY

    /**
     * Checks if a point is inside of this element.
     */
    fun isInside(x: Float, y: Float): Boolean {
        val tx = this.x
        val ty = this.y
        return x in tx..tx + (screenWidth()) && y in ty..ty + (screenHeight())
    }

    /**
     * Checks if an element intersects with this element.
     */
    fun intersects(other: Element): Boolean {
        return intersects(other.x, other.y, other.screenWidth(), other.screenHeight())
    }

    /**
     * Checks if a rectangle intersects with this element.
     */
    private fun intersects(x: Float, y: Float, width: Float, height: Float): Boolean {
        val tx = this.x
        val ty = this.y
        val tw = screenWidth()
        val th = screenHeight()
        return (x < tx + tw && tx < x + width) && (y < ty + th && ty < y + height)
    }

    /**
     * Gets this elements width on the screen
     */
    fun screenWidth() = width * scaleX

    /**
     * Gets this elements height on the screen
     */
    fun screenHeight() = height * scaleY
}