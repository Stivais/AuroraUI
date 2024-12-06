import com.github.stivais.GLFWWindow
import com.github.stivais.NVGRenderer
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.dsl.Aurora
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
        }
    )
}