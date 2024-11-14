package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.dsl.seconds
import com.github.stivais.aurora.elements.BlankElement
import com.github.stivais.aurora.elements.DSL
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope

class Scrollable(
    constraints: Constraints
) : BlankElement(constraints) {

    init {
        scissors = true
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

    override fun position(element: Element, newX: Float, newY: Float) {
        element.internalX = element.constraints.x.calculatePos(element, true)
        element.internalY = element.constraints.y.calculatePos(element, false) - offsetY
        element.x =  element.internalX + newX
        element.y =  element.internalY + newY
    }

    fun scroll(amount: Float) {
        fromY = offsetY
        toY = offsetY + amount
        animationY = Animation(0.15.seconds, Animation.Style.EaseOutQuint)
    }

    companion object {
        @DSL
        fun ElementScope<Scrollable>.scroll(amount: Float) {
            element.scroll(amount)
            element.redraw()
        }
    }
}