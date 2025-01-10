package com.github.stivais.aurora.animation

import kotlin.math.pow

class Animation(
    private var duration: Float,
    private var style: Style,
) {

    private var time: Long = System.nanoTime()

    var finished: Boolean = false

    private var onFinish: (() -> Unit)? = null

    fun get(): Float {
        val percent = ((System.nanoTime() - time) / duration)
        if (percent >= 1f) {
            finished = true
            onFinish?.invoke()
            return 1f
        }
        return style.getValue(percent)
    }

    fun onFinish(onFinish: () -> Unit): Animation {
        this.onFinish = onFinish
        return this
    }

    fun restart(duration: Float, style: Style) {
        this.duration = duration
        this.style = style
        this.time = System.nanoTime()
    }

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