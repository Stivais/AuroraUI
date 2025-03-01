package com.github.stivais.aurora

import com.github.stivais.aurora.dsl.at
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.element.impl.Group

//
// 2.0 Main changes
//
// - Specialized renderer

class Aurora(
//    val renderer: Renderer,
) {

    private var width: Int = 0
        set(value) {
            field = value
            widthPx.amount = value.toFloat()
        }

    private var height: Int = 0
        set(value) {
            field = value
            heightPx.amount = value.toFloat()
        }

    private val widthPx = width.px
    private val heightPx = height.px

    val main = Group(
        aurora = this,
        position = at(0.px, 0.px),
        size = size(widthPx, heightPx)
    )

    fun initialize(width: Int, height: Int) {
        this.width = width
        this.height = height
        main.size()
        main.layout()
//        renderer.resize(width.toFloat(), height.toFloat())
    }

    var reupload = false

    fun render() {
        main.preRender()
        if (reupload) {
            reupload = false
//            upload()
        }
//        renderer.render()
}

    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height

        main.size()
        main.layout()
//        renderer.resize(width.toFloat(), height.toFloat())
//        upload()
    }

    fun cleanup() {
//        renderer.cleanup()
    }
}