package com.github.stivais.aurora

import com.github.stivais.aurora.component.Component
import com.github.stivais.aurora.component.Drawable
import com.github.stivais.aurora.component.impl.Group
import com.github.stivais.aurora.dsl.at
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.engine.renderer.Renderer
import com.github.stivais.aurora.engine.renderer.context.AuroraContext
import com.github.stivais.aurora.engine.renderer.context.DrawCommand
import com.github.stivais.aurora.engine.renderer.tessellator.Tessellator
import com.github.stivais.aurora.utils.loop

//
// 2.0 Main changes
//
// - Specialized renderer

class Aurora {

    private val context: AuroraContext = AuroraContext()

    private val drawCommands: ArrayList<DrawCommand> = arrayListOf()

    // I don't really like this
    private var width: Int = 0
        set(value) {
            field = value
            widthPx.amount = value.toFloat()
        }

    // I don't really like this either yk
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

    // todo: batch draw commands if their render type is different
    private fun upload(component: Component) {
        if (component is Drawable) {
            component.generate()
            drawCommands.add(DrawCommand(component.bufferSlotIndex, component.bufferSlotSize))
        }
        component.children?.loop {
            upload(it)
        }
    }

    fun render() {
        main.preRender()
        if (reupload) {
            reupload = false
            upload(main)
            context.upload(Tessellator)
        }
        Renderer.render(context, drawCommands)
    }

    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height

        main.size()
        main.layout()

        reupload = true

        Renderer.resize(width, height)
    }

    fun cleanup() {
        context.cleanup()
    }
}