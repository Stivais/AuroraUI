package com.github.stivais

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.impl.measurements.Pixel
import com.github.stivais.aurora.constraints.impl.positions.Center
import com.github.stivais.aurora.constraints.impl.size.AspectRatio
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.constraints.impl.size.Fill
import com.github.stivais.aurora.dsl.*
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.elements.Layout.Companion.section
import com.github.stivais.aurora.elements.impl.Block.Companion.outline
import com.github.stivais.aurora.elements.impl.layout.Column.Companion.sectionRow
import com.github.stivais.aurora.elements.impl.popup
import com.github.stivais.aurora.renderer.data.Gradient
import com.github.stivais.aurora.renderer.data.Image
import com.github.stivais.aurora.utils.color

fun ElementScope<*>.colorPicker(value: Color.HSB) = popup(constrain(w = Bounding, h = Bounding), smooth = true) {
    val colorMaxBrightness = color { java.awt.Color.HSBtoRGB(value.hue, value.saturation, 1f) }
    val colorOnlyHue = color { java.awt.Color.HSBtoRGB(value.hue, 1f, 1f) }

    object : Element(copies()) {
        override fun draw() {
            renderer.dropShadow(x, y, width, height, Color.BLACK.rgba, 10f, 5f, 10f, 10f, 10f, 10f)
        }
    }.scope {  }

    draggable()

    block(
        bounds(padding = 15.px),
        color = Color.RGB(22, 22, 22),
        radius = 10.radius()
    ) {
        outline(
            colorMaxBrightness,
            thickness = 1.px
        )

        column(padding = 10.px) {
            section(20.px) {
                text(
                    "Color",
                    pos = at(x = Pixel.ZERO),
                    size = 75.percent
                )
            }

            row(padding = 7.5.px) {
                // saturation and brightness
                outline(
                    size(w = 200.px, h = 200.px),
                    colorMaxBrightness,
                    thickness = 1.px,
                    radius = 7.5.radius()
                ) {
                    block(
                        indent(amount = 2f),
                        colors = Color.WHITE to colorOnlyHue,
                        gradient = Gradient.LeftToRight,
                        radius = 6.radius()
                    )
                    block(
                        indent(amount = 2f),
                        colors = Color.TRANSPARENT to Color.BLACK,
                        gradient = Gradient.TopToBottom,
                        radius = 6.radius()
                    )

//                    onDrag { x, y ->
//                        println("$x, $y")
//                    }
                }

                // hue
                outline(
                    size(w = 15.px, h = 200.px),
                    colorMaxBrightness,
                    thickness = 1.px,
                    radius = 7.5.radius()
                ) {
                    image(
                        Image("/assets/aurora/HueGradient.png"),
                        constraints = indent(amount = 2f),
                        radius = 5.radius()
                    )
                }
            }

            sectionRow(size = 25.px, padding = 10.px) {
                text(
                    "Hex",
                    pos = at(y = Center),
                    size = 65.percent
                )
                block(
                    size(w = Fill, h = 90.percent),
                    Color.RGB(38, 38, 38),
                    radius = 6.radius()
                ) {
                    outline(
                        colorMaxBrightness,
                        thickness = 1.px,
                    )
                }
            }

            sectionRow(padding = 3.percent) {
                block(
                    size(w = 14.percent, h = AspectRatio(1f)),
                    color = Color.RGB(22, 22, 22),
                    radius = 6.radius()
                ) {
                    outline(
                        colorMaxBrightness,
                        thickness = 1.px,
                    )
                }
                repeat(5) {
                    block(
                        size(w = 14.percent, h = AspectRatio(1f)),
                        color = Color.RGB(22, 22, 22),
                        radius = 6.radius()
                    ) {
                        outline(
                            color = Color.RGB(30, 30, 30),
                            thickness = 1.px
                        )
                    }
                }
            }
        }
    }

}