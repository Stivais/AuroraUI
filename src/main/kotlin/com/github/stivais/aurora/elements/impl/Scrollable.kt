package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.measurements.Pixel
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.dsl.seconds
import com.github.stivais.aurora.elements.AuroraDSL
import com.github.stivais.aurora.elements.BlankElement
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope

/**
 * # Scrollable
 *
 * This element implements scrolling.
 *
 * If height is [Bounding], you will be able to scroll until the size is 0,
 * otherwise it will scroll until there isn't more elements to show.
 */
class Scrollable(
    constraints: Constraints
) : BlankElement(constraints) {

    init {
        scissors = true

        if (constraints.width.undefined()) constraints.width = Bounding
        if (constraints.height.undefined()) constraints.height = Bounding
    }

    private var offsetY: Float = 0f

    private var fromY = 0f
    private var toY = 0f

    private var animationY: Animation? = null

    override fun prePosition() {
        animationY?.let { anim ->
            val progress = anim.get()
            val newOffset = fromY + (toY - fromY) * progress

            val limit = if (constraints.height is Bounding) {
                height + offsetY
            } else {
                (Bounding.calculateSize(this, false) + offsetY) - height
            }
            offsetY = newOffset.coerceIn(0f, limit)

            // stop anim if finished or cant scroll further
            if (anim.finished || newOffset < 0f || newOffset > limit) {
                animationY = null
            }
            redraw()
        }
    }

    // idk how to feel about this
    override fun position(element: Element, newX: Float, newY: Float) {
        element.internalX = element.constraints.x.calculatePos(element, true)
        element.internalY = element.constraints.y.calculatePos(element, false) - offsetY
        element.x =  element.internalX + newX
        element.y =  element.internalY + newY
    }


    override fun getDefaultPositions() = Pixel.ZERO to Pixel.ZERO

    /**
     * Starts an animation to scroll by an amount.
     */
    fun scroll(amount: Float, duration: Float, style: Animation.Style) {
        fromY = offsetY
        toY = offsetY + amount
        animationY = Animation(duration, style)
    }

    companion object {
        /**
         * Invokes [Scrollable.scroll] and redraws the element.
         *
         * Also tells the element to [redraw].
         */
        @AuroraDSL
        fun ElementScope<Scrollable>.scroll(
            amount: Float,
            duration: Float = 0.15.seconds,
            style: Animation.Style = Animation.Style.EaseOutQuint
        ) {
            element.scroll(amount, duration, style)
            element.redraw()
        }
    }
}