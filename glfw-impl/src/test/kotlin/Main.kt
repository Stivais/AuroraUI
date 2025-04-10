import com.github.stivais.GLFWWindow
import com.github.stivais.NVGRenderer
import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.components.Component
import com.github.stivais.aurora.components.impl.Shadow
import com.github.stivais.aurora.dsl.*


fun main() {
//    val window = EmptyWindow()

    val window = GLFWWindow(
        "Aurora",
        500,
        500,
    )

    window.open(
        Aurora(renderer = NVGRenderer) {
            // custom element so it doesnt trigger redraw
            object : Component(constraints = constrain(0.px, 0.px, 1.px, 1.px)) {
                override fun draw() {
                    renderer.text("fps: ${window.fps}", 0f, 0f, 20f, Color.WHITE.rgba, AuroraUI.defaultFont, blur = 0f)
                }
            }.add()

//            image(
//                Image("/assets/aurora/test.svg"),
//                size(100.px, 100.px)
//            )

//            repeat(10) {
//                colorPicker(Color.RGB(50, 150, 220).toHSB())
//            }
//            scrollable(at(x = Center, y = 100.px)) {
//                column {
//                    repeat(5) {
//                        block(size(100.px, 100.px), Color.RED).outline(Color.WHITE, 1.px)
//                    }
//                    onScroll { (amount) ->
//                        scroll(amount * 25f)
//                    }
//                }
//            }


            val options = arrayOf(
                "first",
                "second",
                "third",
                "just in case"
            )


            block(
                constraints = size(50.percent, 50.percent),
                color = Color.RED
            ) {
                Shadow(
                    copies(),
                    blur = 50f,
                    spread = 2f,
                    radii = null
                ).add()
            }


        }
    )
}