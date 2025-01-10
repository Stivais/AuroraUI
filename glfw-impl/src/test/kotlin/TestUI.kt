import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Positions
import com.github.stivais.aurora.constraints.Sizes
import com.github.stivais.aurora.constraints.impl.measurements.Animatable
import com.github.stivais.aurora.constraints.impl.measurements.Center
import com.github.stivais.aurora.constraints.impl.measurements.Undefined
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.element.ComponentScope
import com.github.stivais.aurora.element.impl.Group
import com.github.stivais.aurora.renderer.Renderer

fun aurora(renderer: Renderer, block: ComponentScope<Group>.() -> Unit): Aurora {
    val ui = Aurora(renderer)
    ComponentScope(ui.main).block()
    return ui
}

fun at(x: Constraint.Position = Undefined, y: Constraint.Position = Undefined) = Positions(x, y)

fun center() = at(Center, Center)

fun size(w: Constraint.Size = Undefined, h: Constraint.Size = Undefined) = Sizes(w, h)

val animTest = Animatable(from = 20.px, 125.px)

lateinit var mainRedrawTemp: ComponentScope<*>

fun testUI(renderer: Renderer) = aurora(renderer) {

//    colorPicker()


//    val px = 100.px
    block(
        size = size(animTest, animTest),
        color = Color.RED
    )


    mainRedrawTemp = row(center()) {
        block(
            size = size(animTest, animTest),
            color = Color.RED
        )
        block(
            size = size(animTest, animTest),
            color = Color.GREEN
        )
        block(
            size = size(animTest, animTest),
            color = Color.BLUE
        )
        block(
            size = size(animTest, animTest),
            color = Color.WHITE
        )
    }

//    println(
//        measureNanoTime {
//            repeat(50) {
//                block(
//                    center(),
//                    size(animTest, animTest),
//                    color = Color.BLUE
//                )
//            }
//        }
//    )


//    row(at(Center, 10.px)) {
//        repeat(5) {
//            block(
//                size = size(100.px, 100.px),
//                color = Color.RGB(255, 0, 0, 0.25f)
//            )
//        }
//    }
}
