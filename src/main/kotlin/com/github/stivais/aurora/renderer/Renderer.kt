package com.github.stivais.aurora.renderer

interface Renderer {

    fun resize(width: Float, height: Float)

    fun cleanup()

    fun render()

    fun upload()

    fun rect(x: Float, y: Float, width: Float, height: Float, color: Int)

    fun roundedRect(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int,
        tl: Float,
        tr: Float,
        bl: Float,
        br: Float
    )

    fun circle(x: Float, y: Float, radius: Float, color: Int)
}