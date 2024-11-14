@file:Suppress("unused")

package com.github.stivais.aurora.color

import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.utils.*
import kotlin.math.roundToInt
import java.awt.Color as AWTColor

/**
 * # Color
 *
 * The color interface is used to represent a 32-bit integer in ARGB format.
 *
 * It is primarily used in [Aurora UIs][com.github.stivais.aurora.AuroraUI]
 */
interface Color {

    /**
     * Integer representation of this color in ARGB format
     */
    val rgba: Int

//    /**
//     * Uses internally by UIs
//     */
//    fun get(element: Element): Int = rgba

    /**
     * # Color.RGB
     *
     * Low cost implementation of [Color], due to Kotlin's inline classes
     *
     * This is the most common [color][Color], because it mainly represents red, blue and green.
     */
    @JvmInline
    value class RGB(override val rgba: Int) : Color {
        constructor(
            red: Int,
            green: Int,
            blue: Int,
            alpha: Float = 1f
        ) : this(getRGBA(red, green, blue, (alpha * 255).roundToInt()))
    }

    /**
     * # Color.HSB
     *
     * This [Color] implementation represents the color in HSBA format.
     *
     * It only updates the [rgba][Color.rgba] value if any of the hue, saturation or brightness values have been changed.
     */
    open class HSB(hue: Float, saturation: Float, brightness: Float, alpha: Float = 1f) : Color {

        constructor(hsb: FloatArray, alpha: Float = 1f) : this(hsb[0], hsb[1], hsb[2], alpha)

        constructor(other: HSB) : this(other.hue, other.saturation, other.brightness, other.alpha)

        var hue = hue
            set(value) {
                field = value
                needsUpdate = true
            }

        var saturation = saturation
            set(value) {
                field = value
                needsUpdate = true
            }

        var brightness = brightness
            set(value) {
                field = value
                needsUpdate = true
            }

        var alpha = alpha
            set(value) {
                field = value
                needsUpdate = true
            }

        @Transient
        private var needsUpdate: Boolean = true

        override var rgba: Int = 0
            get() {
                if (needsUpdate) {
                    field =
                        (AWTColor.HSBtoRGB(hue, saturation, brightness) and 0X00FFFFFF) or ((alpha * 255).toInt() shl 24)
                    needsUpdate = false
                }
                return field
            }
            set(value) {
                if (field != value) {
                    field = value
                    val hsb = FloatArray(3)
                    AWTColor.RGBtoHSB(value.red, value.blue, value.green, hsb)
                    hue = hsb[0]
                    saturation = hsb[1]
                    brightness = hsb[2]
                    alpha = value.alpha / 255f
                }
            }




        override fun equals(other: Any?): Boolean {
            return other is Color && other.rgba == this.rgba
        }

        override fun hashCode(): Int {
            return rgba.hashCode()
        }
    }

    /**
     * # Color.Animated
     *
     * This [Color] implementation allows you to animate between 2 different colors, utilizing [Animations][Animation].
     *
     * @see Animation
     */
    class Animated(from: Color, to: Color) : Color {

        constructor(from: Color, to: Color, swapIf: Boolean) : this(from, to) {
            if (swapIf) {
                swap()
            }
        }

        /**
         * Current animation for this [Color.Animated].
         *
         * If this is null, that means it isn't animating.
         */
        var animation: Animation? = null

        /**
         * The color to animate from.
         *
         * When an animation is finished, it will swap with [color2].
         */
        private var color1: Color = from

        /**
         * The color to animate to.
         *
         * When an animation is finished, it will swap with [color1].
         */
        private var color2: Color = to

        private var from: Int = color1.rgba

        override val rgba: Int
            get() {
                if (animation != null) {
                    val progress = animation!!.get()
                    val to = color2.rgba
                    val current = getRGBA(
                        (from.red + (to.red - from.red) * progress).toInt(),
                        (from.green + (to.green - from.green) * progress).toInt(),
                        (from.blue + (to.blue - from.blue) * progress).toInt(),
                        (from.alpha + (to.alpha - from.alpha) * progress).toInt(),
                    )
                    if (animation!!.finished) {
                        animation = null
                        swap()
                    }
                    return current
                }
                return color1.rgba
            }


        fun animate(duration: Float, style: Animation.Style): Animation? {
            if (duration == 0f) {
                swap()
                return null
            }
            animation = if (animation != null) {
                from = rgba
                swap()
                Animation(duration * animation!!.get(), style)
            } else {
                Animation(duration, style)
            }
            return animation
        }

        private fun swap() {
            val temp = color2
            color2 = color1
            color1 = temp
        }
    }

    companion object {
        @JvmField
        val TRANSPARENT: RGB = RGB(0, 0, 0, 0f)

        @JvmField
        val WHITE: RGB = RGB(255, 255, 255, 1f)

        @JvmField
        val BLACK: RGB = RGB(0, 0, 0, 1f)
    }
}