@file:Suppress("unused")

package com.github.stivais.aurora.renderer

import com.github.stivais.aurora.renderer.data.Font
import com.github.stivais.aurora.renderer.data.Gradient
import com.github.stivais.aurora.renderer.data.Image
import com.github.stivais.aurora.renderer.data.Radii

/**
 * # Renderer
 *
 * This interface allows you to create a rendering implementation for [Aurora][com.github.stivais.aurora.AuroraUI].
 * Please make sure to implement every method, so everything correctly renders.
 *
 * Your implementation is responsible for loading [Images][Image] and [Fonts][Font].
 */
interface Renderer {

    fun beginFrame(width: Float, height: Float)

    fun endFrame()

    fun push()

    fun pop()

    fun scale(x: Float, y: Float)

    fun translate(x: Float, y: Float)

    fun rotate(amount: Float)

    fun globalAlpha(amount: Float)

    fun globalFontBlur(amount: Float)

    fun pushScissor(x: Float, y: Float, w: Float, h: Float)

    fun popScissor()

    fun line(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, color: Int)

    fun rect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color: Int,
        tl: Float,
        bl: Float,
        br: Float,
        tr: Float
    )

    fun rect(x: Float, y: Float, w: Float, h: Float, color: Int, radius: Float = 0f) =
        rect(x, y, w, h, color, radius, radius, radius, radius)

    fun rect(x: Float, y: Float, w: Float, h: Float, color: Int, radii: Radii) =
        rect(x, y, w, h, color, radii.topLeft, radii.bottomLeft, radii.bottomRight, radii.topRight)


    fun hollowRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        thickness: Float,
        color: Int,
        tl: Float,
        bl: Float,
        br: Float,
        tr: Float
    )

    fun hollowRect(x: Float, y: Float, w: Float, h: Float, thickness: Float, color: Int, radius: Float = 0f) =
        hollowRect(x, y, w, h, thickness, color, radius, radius, radius, radius)

    fun hollowRect(x: Float, y: Float, w: Float, h: Float, thickness: Float, color: Int, radii: Radii) =
        hollowRect(x, y, w, h, thickness, color, radii.topLeft, radii.bottomLeft, radii.bottomRight, radii.topRight)


    fun gradientRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color1: Int,
        color2: Int,
        gradient: Gradient,
        tl: Float,
        bl: Float,
        br: Float,
        tr: Float
    )

    fun gradientRect(
        x: Float, y: Float, w: Float, h: Float, color1: Int, color2: Int, direction: Gradient, radius: Float = 0f
    ) = gradientRect(x, y, w, h, color1, color2, direction, radius, radius, radius, radius)

    fun gradientRect(
        x: Float, y: Float, w: Float, h: Float, color1: Int, color2: Int, direction: Gradient, radii: Radii
    ) = gradientRect(x, y, w, h, color1, color2, direction, radii.topLeft, radii.bottomLeft, radii.bottomRight, radii.topRight)

    fun dropShadow(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int,
        blur: Float,
        spread: Float,
        tl: Float, bl: Float, br: Float, tr: Float
    )

    fun text(text: String, x: Float, y: Float, size: Float, color: Int, font: Font, blur: Float)

    fun textWidth(text: String, size: Float, font: Font): Float

    fun image(image: Image, x: Float, y: Float, w: Float, h: Float, tl: Float, bl: Float, br: Float, tr: Float)

    fun image(image: Image, x: Float, y: Float, w: Float, h: Float, radius: Float = 0f) =
        image(image, x, y, w, h, radius, radius, radius, radius)

    fun image(image: Image, x: Float, y: Float, w: Float, h: Float, radii: Radii) =
        image(image, x, y, w, h, radii.topLeft, radii.bottomLeft, radii.bottomRight, radii.topRight)


    /**
     * When implementing this,
     * it is recommended to use reference counting for loading/deleting images.
     */
    fun createImage(image: Image)

    /**
     * When implementing this,
     * it is recommended to use reference counting for loading/deleting images.
     */
    fun deleteImage(image: Image)

//    fun scissor(x: Float, y: Float, width: Float, height: Float): Scissor

//    fun resetScissor(scissor: Scissor)

//    fun clearScissors()
}