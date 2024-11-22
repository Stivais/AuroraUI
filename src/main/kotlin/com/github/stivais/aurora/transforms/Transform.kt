package com.github.stivais.aurora.transforms

import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.renderer.Renderer
import kotlin.reflect.KProperty

/**
 * # Transforms
 *
 * Transforms are functions that run for an element,
 * which main use is to apply transformations (i.e. Scaling, Rotation), or an effect (i.e. Alpha)
 *
 * Provided transforms include:
 * [Scale][com.github.stivais.aurora.transforms.impl.Scale]
 * [Scale.Animated][com.github.stivais.aurora.transforms.impl.Scale.Animated]
 * [Rotation][com.github.stivais.aurora.transforms.impl.Rotation]
 * [Rotation.Animated][com.github.stivais.aurora.transforms.impl.Rotation.Animated]
 * [Alpha][com.github.stivais.aurora.transforms.impl.Alpha]
 * [Alpha.Animated][com.github.stivais.aurora.transforms.impl.Alpha.Animated]
 */
fun interface Transform {

    fun apply(element: Element, renderer: Renderer)

    /**
     * Helps implement delegation for transformations that use 1 main value
     */
    interface Mutable : Transform {

        /**
         * Value in [Transform.Mutable], which gets delegated.
         */
        var amount: Float

        operator fun getValue(thisRef: Element, property: KProperty<*>): Float {
            return amount
        }

        operator fun setValue(thisRef: Element, property: KProperty<*>, value: Float) {
            amount = value
        }
    }

    /**
     * Helps create a transform which uses a swapping animation
     * like the [Animatable][com.github.stivais.aurora.constraints.impl.measurements.Animatable] constraint.
     */
    abstract class Animated(
        private var from: Float,
        private var to: Float,
    ) : Transform {

        var animation: Animation? = null
            private set

        private var current: Float = 0f
        private var before: Float? = null

        protected fun get(): Float {
            animation?.let { anim ->
                val progress = anim.get()
                val from = before ?: from
                current = from + (to - from) * progress

                if (anim.finished) {
                    animation = null
                    before = null
                    swap()
                }
                return current
            }
            return from
        }

        // todo: maybe implement common class for this, Animatable and Color.Animated
        fun animate(duration: Float, style: Animation.Style): Animation? {
            if (duration == 0f) {
                swap()
                return null
            }

            animation = if (animation != null) {
                before = current
                swap()
                Animation(duration * animation!!.get(), style)
            } else {
                Animation(duration, style)
            }
            return animation
        }

        fun swap() {
            val temp = to
            to = from
            from = temp
        }
    }
}
