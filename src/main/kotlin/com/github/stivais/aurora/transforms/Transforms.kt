package com.github.stivais.aurora.transforms

import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.renderer.Renderer

fun interface Transforms {

    fun apply(element: Element, renderer: Renderer)

    class Scale(var amount: Float, private val centered: Boolean) : Transforms {
        override fun apply(element: Element, renderer: Renderer) {
            element.scaleX = amount
            element.scaleY = amount
        }

        class Animated(
            val from: Float,
            val to: Float,
            val centered: Boolean = true
        ) : Transforms {
            override fun apply(element: Element, renderer: Renderer) {

            }
        }
    }
}