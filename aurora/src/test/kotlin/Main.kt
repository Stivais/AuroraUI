import com.github.stivais.GLFWWindow
import com.github.stivais.NVGRenderer
import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.dsl.Aurora
import com.github.stivais.aurora.dsl.constrain
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.elements.Element
import com.github.stivais.aurora.utils.toHSB
import com.github.stivais.colorPicker


fun main() {
//    val window = EmptyWindow()

    val window = GLFWWindow(
        "Aurora",
        1720,
        1080,
    )

    window.open(
        Aurora(renderer = NVGRenderer) {
            // custom element so it doesnt trigger redraw
            object : Element(constraints = constrain(0.px, 0.px, 1.px, 1.px)) {
                override fun draw() {
                    renderer.text("fps: ${window.fps}", 0f, 0f, 20f, Color.WHITE.rgba, AuroraUI.defaultFont)
                }
            }.add()
//            textSupplied({ "fps:" }, pos = at(0.px, 0.px), size = 20.px)

//            text("hi")

            repeat(10) {
                colorPicker(Color.RGB(50, 150, 220).toHSB())
            }
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
        }
    )
}