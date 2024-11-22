package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.dsl.seconds
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.transforms.impl.Alpha
import com.github.stivais.aurora.transforms.impl.Scale

class Popup(
    element: Group,
    private val alphaAnimation: Alpha.Animated,
    private val scaleAnimation: Scale.Animated,
    private val smooth: Boolean
) : ElementScope<Group>(element) {

    private var closing = false

    fun closePopup(smooth: Boolean = this.smooth) {
        if (!closing) {
            closing = true
            val length = if (!smooth) 0f else 0.25.seconds
            alphaAnimation.animate(length, Animation.Style.EaseInQuint)
            val anim = scaleAnimation.animate(length, Animation.Style.EaseInQuint)

            element.ui.addOperation {
                val finished = anim == null || anim.finished
                if (finished) element.ui.main.removeElement(element)
                finished
            }
        }
    }
}

fun ElementScope<*>.popup(
    constraints: Constraints,
    smooth: Boolean,
    block: Popup.() -> Unit
): Popup {

    val alphaAnimation = Alpha.Animated(from = 0f, to = 1f)
    val scaleAnimation = Scale.Animated(from = 0f, to = 1f, centered = true)

    val group = Group(constraints).apply {
        addTransform(alphaAnimation)
        addTransform(scaleAnimation)
    }

    ui.main.addElement(group)

    val popup = Popup(group, alphaAnimation, scaleAnimation, smooth)
    popup.block()

    if (smooth) {
        alphaAnimation.animate(0.25.seconds, Animation.Style.EaseOutQuint)
        scaleAnimation.animate(0.25.seconds, Animation.Style.EaseOutQuint)
    } else {
        alphaAnimation.swap()
        scaleAnimation.swap()
    }
    return popup
}