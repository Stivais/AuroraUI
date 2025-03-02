package com.github.stivais.aurora.color

import com.github.stivais.aurora.utils.getRGBA

sealed interface Color {

    sealed interface Static : Color

    val rgba: Int

    @JvmInline
    value class RGB(override val rgba: Int) : Static {
        constructor(
            red: Int,
            green: Int,
            blue: Int,
            alpha: Float = 1f
        ) : this(getRGBA(red, green, blue, alpha))
    }

    // todo: fix
    class Animated(from: Color, to: Color) : Color {
//        over

        override val rgba: Int = 0
    }


    companion object {

        @JvmField
        val WHITE = RGB(255, 255, 255)

        @JvmField
        val RED = RGB(255, 0, 0)

        @JvmField
        val GREEN = RGB(0, 255, 0)

        @JvmField
        val BLUE = RGB(0, 0, 255)

    }
}