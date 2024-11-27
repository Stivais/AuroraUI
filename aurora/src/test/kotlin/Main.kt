import com.github.stivais.GLFWWindow
import com.github.stivais.NVGRenderer
import com.github.stivais.aurora.animations.Animation
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.impl.measurements.Animatable
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.constraints.impl.size.Copying
import com.github.stivais.aurora.dsl.*
import com.github.stivais.aurora.elements.impl.Block.Companion.outline
import com.github.stivais.aurora.elements.impl.Scrollable
import com.github.stivais.aurora.elements.impl.Scrollable.Companion.scroll
import com.github.stivais.aurora.utils.toHSB
import com.github.stivais.colorPicker


fun main() {

    val window = GLFWWindow(
        "Aurora",
        800, 800,
    )

    window.open(
        Aurora(renderer = NVGRenderer) {
            colorPicker(Color.RGB(50, 150, 220).toHSB())

//            var index = 0
//            operation {
//                println("hi")
//                index++
//                index == 5
//            }
//
//            val popup = popup(size(50.px, 50.px), smooth = true) {
//                block(copies(), Color.WHITE) {
////                    hoverEffect()
//                }
//                onClick {
//                    closePopup()
//                    true
//                }
//            }
//
            group {
                block(
                    size(100.px, 100.px),
                    Color.WHITE
                ) {
                    draggable(moves = element.parent!!)
                }
            }
//
//            Scrollable(constrain(x = 75.percent, y = 25.percent, w = 100.px, h = 100.px)).scope {
//                column(constrain(0.px, 0.px, w = Bounding, h = Bounding)) {
//                    repeat(5) {
//                        block(size(100.px, 100.px), Color.RGB(255, 0, 0)).outline(Color.WHITE, thickness = 1.px)
//                    }
//                    onScroll { (amount) ->
//                        scroll(amount * 30f)
//                    }
//                }
//            }


            var shouldAnimate = false
            val sliderWidth = Animatable.Raw(0f)


            block(
                constraints = constrain(y = 75.percent, w = Copying, h = 20.percent),
                color = Color.WHITE,
                radius = 5.radius()
            ) {
                block(
                    constrain(0.px, 0.px, sliderWidth, Copying),
                    color = Color.RED,
                    radius = 5.radius()
                )
//            hoverEffect()

                onClick {
                    shouldAnimate = true
                }
                onMouseDrag { percent, _ ->
                    val to = percent * element.width
                    if (shouldAnimate || !ui.eventManager.mouseDown) {
                        shouldAnimate = false
                        sliderWidth.animate(to = to, 0.75.seconds, Animation.Style.EaseOutQuint)
                    } else {
                        sliderWidth.to(to = to)
                    }
//                    set(percent * (max - min) + min)
                    redraw()
                }
            }


            column(at(10.px, 10.px)) {
                block(size(100.px, 100.px), Color.RGB(0, 0, 255))
                val s = Scrollable(constrain(w = Bounding, h = Bounding)).scope {
                    column(constrain(w = Bounding, h = Bounding)) {
                        repeat(5) {
                            block(size(100.px, 100.px), Color.RGB(255, 0, 0)) {
                                outline(Color.WHITE, thickness = 1.px)
                                hoverEffect(factor = 1.25f)
                            }
                        }
                    }
                }
                onScroll { (amount) ->
                    s.scroll(amount * 30f)
                }
                block(size(100.px, 100.px), Color.RGB(0, 0, 255))
            }
        }
    )
}