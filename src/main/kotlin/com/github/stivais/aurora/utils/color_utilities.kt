@file:Suppress("unused", "nothing_to_inline")

package com.github.stivais.aurora.utils

import com.github.stivais.aurora.color.Color
import kotlin.math.roundToInt

inline val Int.red
    get() = this shr 16 and 0xFF

inline val Int.green
    get() = this shr 8 and 0xFF

inline val Int.blue
    get() = this and 0xFF

inline val Int.alpha
    get() = (this shr 24) and 0xFF

inline val Color.red
    get() = rgba.red

inline val Color.green
    get() = rgba.green

inline val Color.blue
    get() = rgba.blue

inline val Color.alpha
    get() = rgba.alpha

/**
 * Compacts 4 integers representing red, green, blue and alpha into a single integer
 */
inline fun getRGBA(red: Int, green: Int, blue: Int, alpha: Int): Int {
    return ((alpha shl 24) and 0xFF000000.toInt()) or ((red shl 16) and 0x00FF0000) or ((green shl 8) and 0x0000FF00) or (blue and 0x000000FF)
}


/**
 * Compacts 3 integers representing red, green, blue and an alpha value into a single integer
 */
inline fun getRGBA(red: Int, green: Int, blue: Int, alpha: Float): Int {
    return (((alpha * 255).roundToInt() shl 24) and 0xFF000000.toInt()) or ((red shl 16) and 0x00FF0000) or ((green shl 8) and 0x0000FF00) or (blue and 0x000000FF)
}

/**
 * Gets an RGBA value from a hexadecimal color string (#RRGGBB or #RRGGBBAA).
 *
 * @throws IllegalArgumentException If hex value is invalid.
 */
fun hexToRGBA(hex: String): Int {
    return when (hex.length) {
        7 -> (255 shl 24) or hex.substring(1, 7).toInt(16)
        9 -> (hex.substring(7, 9).toInt(16) shl 24) or hex.substring(1, 7).toInt(16) // needs 2 substrings because it is unable to correctly parse with 1
        else -> throw IllegalArgumentException("Invalid hex color format: $hex. Use #RRGGBB or #RRGGBBAA.")
    }
}

/**
 * Gets a string representing a hexadecimal color value. (#RRGGBB or #RRGGBBAA)
 */
fun Color.toHexString(returnAlpha: Boolean = false): String {
    return if (returnAlpha) {
        String.format("#%02X%02X%02X%02X", red, green, blue, alpha)
    } else {
        String.format("#%02X%02X%02X", red, green, blue)
    }
}

/**
 * Copies a color with the new alpha value provided.
 */
inline fun Color.withAlpha(alpha: Float): Color = Color.RGB(red, green, blue, alpha)

/**
 * Copies a color with the new alpha value provided.
 */
inline fun Color.withAlpha(alpha: Int): Color = Color.RGB(red, green, blue, alpha / 255f)

/**
 * Copies a color, multiplying its alpha value by a certain factor
 */
inline fun Color.multiplyAlpha(factor: Float): Color = withAlpha((alpha * factor).roundToInt())

/**
 * Multiples an integer representing a hexadecimal color.
 */
inline fun Int.multiply(
    r: Float = 1f,
    g: Float = 1f,
    b: Float = 1f,
    a: Float = 1f
) = getRGBA(
    (red * r).roundToInt().coerceIn(0, 255),
    (green * g).roundToInt().coerceIn(0, 255),
    (blue * b).roundToInt().coerceIn(0, 255),
    (alpha * a).roundToInt().coerceIn(0, 255)
)

/**
 * Multiples red, blue and green from a hexadecimal color by a factor.
 */
inline fun Int.multiply(factor: Float = 1f) = multiply(factor, factor, factor, 1f)

/**
 * Converts any [Color] into a [Color.HSB]
 */
fun Color.toHSB(): Color.HSB {
    return Color.HSB(
        java.awt.Color.RGBtoHSB(
            red,
            green,
            blue,
            FloatArray(size = 3)
        ),
        alpha / 255f
    )
}

/**
 * Implementation of [Color], where it gets supplied the rgba value from [block].
 *
 * This isn't recommended to use with expensive calculations because it runs every frame.
 */
inline fun color(crossinline block: () -> Int): Color = object : Color {
    override val rgba: Int
        get() = block()
}