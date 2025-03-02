package com.github.stivais.aurora.engine.renderer

import com.github.stivais.aurora.engine.renderer.context.AuroraContext
import com.github.stivais.aurora.engine.renderer.context.DrawCommand
import com.github.stivais.aurora.engine.renderer.shader.Shader
import com.github.stivais.aurora.utils.loop
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11.*

// maybe use it a class for multiple instances?
object Renderer {

    private val shader = Shader.fromFile(
        "/assets/shader/aurora_vertex.vert",
        "/assets/shader/aurora_fragment.frag",
    )

    private val projectionMatrix = Matrix4f()

    fun render(context: AuroraContext, drawCommands: ArrayList<DrawCommand>) {
        shader.bind()
        context.bind()
        drawCommands.loop { cmd ->
            glDrawElements(GL_TRIANGLES, cmd.size.toInt(), GL_UNSIGNED_INT, cmd.first * 4L)
        }
        context.unbind()
        shader.unbind()
    }

    fun resize(width: Int, height: Int) {
        shader.bind()
        projectionMatrix.setOrtho(0f, width.toFloat(), height.toFloat(), 0f, -1000f, 1000f)
        shader.setMat4f("projection", projectionMatrix)
        shader.unbind()
    }
}