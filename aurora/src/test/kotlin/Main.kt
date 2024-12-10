import com.github.stivais.GLFWWindow
import com.github.stivais.NVGRenderer
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.impl.positions.Center
import com.github.stivais.aurora.dsl.*
import com.github.stivais.aurora.elements.impl.Block.Companion.outline
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

            scrollable(at(x = Center, y = 100.px)) {
                column {
                    repeat(5) {
                        block(size(100.px, 100.px), Color.RED).outline(Color.WHITE, 1.px)
                    }
                    onScroll { (amount) ->
                        scroll(amount * 25f)
                    }
                }
            }
        }
    )
}