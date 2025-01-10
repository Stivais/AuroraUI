package com.github.stivais.aurora

import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.Sizes
import com.github.stivais.aurora.dsl.mutable
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.element.Component
import com.github.stivais.aurora.element.Drawable
import com.github.stivais.aurora.element.impl.Group
import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.utils.loop

//
// 2.0 Main changes
//
// - Specialized renderer

class Aurora(
    val renderer: Renderer,
) {
    private val width = 1920.px.mutable
    private val height = 1080.px.mutable

    val main = Group(
        ui = this,
        Positions(0.px, 0.px),
        Sizes(width, height)
    )

    fun initialize(width: Int, height: Int) {
        this.width.value = width.toFloat()
        this.height.value = height.toFloat()
        main.size()
        main.layout()
        renderer.resize(width.toFloat(), height.toFloat())
    }

    var reupload = false

    // todo: alternative solution
    fun upload() {
        upload(main)
        renderer.upload()
//        renderer.uploadPalette(palette)
    }

    private fun upload(component: Component) {
        if (component is Drawable) {
            component.generate(renderer)
        }
        component.children?.loop {
            upload(it)
        }
    }

    fun render() {
        main.preRender()
        if (reupload) {
            reupload = false
            upload()
        }
        renderer.render()
}

    fun resize(width: Int, height: Int) {
        this.width.value = width.toFloat()
        this.height.value = height.toFloat()

        main.size()
        main.layout()
        renderer.resize(width.toFloat(), height.toFloat())
        upload()
    }

    fun cleanup() {
        renderer.cleanup()
    }

//    fun outline(renderer: RendererOld, x: Float, y: Float, width: Float, height: Float) {
//        renderer {
//            color(Color.WHITE, palette)
//            quad(x, y, width, 1f)
//            quad(x, y + height, width, 1f)
//            quad(x, y, 1f, height)
//            quad(x + width, y, 1f, height)
//        }
//    }
}