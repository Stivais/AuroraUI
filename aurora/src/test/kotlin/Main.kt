import com.github.stivais.GLFWWindow
import com.github.stivais.NVGRenderer
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.impl.size.Bounding
import com.github.stivais.aurora.dsl.*
import com.github.stivais.aurora.elements.impl.Block.Companion.outline
import com.github.stivais.aurora.elements.impl.Scrollable
import com.github.stivais.aurora.elements.impl.Scrollable.Companion.scroll


fun main() {

    val window = GLFWWindow(
        "Aurora",
        800, 800,
    )

    window.open(
        Aurora(renderer = NVGRenderer) {
//            colorPicker(Color.RGB(50, 150, 220).toHSB())

//            block(
//                size(Bounding + 10.px, 100.px),
//                Color.RGB(255, 0, 0)
//            ) {
//                val input = TextInput(
//                    default = "hi",
//                    font = AuroraUI.defaultFont,
//                    color = Color.WHITE,
//                    at(),
//                    50.px
//                ).scope{}
//
//                onClick {
//                    ui.eventManager.focused = input.element
//                    true
//                }
//            }

            Scrollable(constrain(x = 75.percent, y = 25.percent, w = 100.px, h = 100.px)).scope {
                column(constrain(0.px, 0.px, w = Bounding, h = Bounding)) {
                    repeat(5) {
                        block(size(100.px, 100.px), Color.RGB(255, 0, 0)).outline(Color.WHITE, thickness = 1.px)
                    }
                    onScroll { (amount) ->
                        scroll(amount * 30f)
                    }
                }
            }


            Scrollable(constrain(x = 25.percent, y = 25.percent, w = Bounding, h = Bounding)).scope {
                column(constrain(0.px, 0.px, w = Bounding, h = Bounding)) {
                    repeat(5) {
                        block(size(100.px, 100.px), Color.RGB(255, 0, 0)).outline(Color.WHITE, thickness = 1.px)
                    }
                    onScroll { (amount) ->
                        scroll(amount * 30f)
                    }
                }
            }
        }
    )
}