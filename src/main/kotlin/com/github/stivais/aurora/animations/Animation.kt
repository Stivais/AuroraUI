package com.github.stivais.aurora.animations

import kotlin.math.pow

/**
 * # Animation
 *
 * Helps assist in interpolating values, which is usually used to smoothly animate something
 *
 * @param duration Duration time in nanoseconds.
 * @param style Style of the animations
 */
class Animation(
    private var duration: Float,
    private val style: Style
) {

    private var time: Long = System.nanoTime()

    var finished: Boolean = false

    fun get(): Float {
        val percent = ((System.nanoTime() - time) / duration)
        if (percent >= 1f) {
            finished = true
        }
        return if (finished) 1f else style.getValue(percent)
    }

    /**
     * A bunch of animations commonly used in UI
     *
     * Animations taken from [https://easings.net/](https://easings.net/)
     *
     * @see Animation
     */
    enum class Style : Strategy {
        Linear {
            override fun getValue(percent: Float): Float = percent
        },
        EaseInQuad {
            override fun getValue(percent: Float): Float = percent * percent
        },
        EaseOutQuad {
            override fun getValue(percent: Float): Float = 1 - (1 - percent) * (1 - percent)
        },
        EaseInOutQuad {
            override fun getValue(percent: Float): Float {
                return if (percent < 0.5) 2 * percent * percent
                else 1 - (-2 * percent + 2).pow(2f) / 2
            }
        },
        EaseInQuint {
            override fun getValue(percent: Float): Float = percent * percent * percent * percent * percent
        },
        EaseOutQuint {
            override fun getValue(percent: Float): Float = 1 - (1 - percent).pow(5f)
        },
        EaseInOutQuint {
            override fun getValue(percent: Float): Float {
                return if (percent < 0.5f) 16f * percent * percent * percent * percent * percent
                else 1 - (-2 * percent + 2).pow(5f) / 2f
            }
        },
        EaseInBack {
            override fun getValue(percent: Float): Float {
                val c1 = 1.70158f
                val c3 = c1 + 1
                return c3 * percent * percent * percent - c1 * percent * percent
            }
        },
    }
}

private interface Strategy {
    fun getValue(percent: Float): Float
}