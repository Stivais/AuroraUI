@file:Suppress("nothing_to_inline", "unused")

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