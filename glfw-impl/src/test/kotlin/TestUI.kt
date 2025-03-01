import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.measurements.impl.Animatable
import com.github.stivais.aurora.constraints.measurements.impl.Center
import com.github.stivais.aurora.dsl.at
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.element.ComponentScope
import com.github.stivais.aurora.element.impl.Group

fun aurora(block: ComponentScope<Group>.() -> Unit): Aurora {
    val ui = Aurora()
    ComponentScope(ui.main).block()
    return ui
}

fun center() = at(Center, Center)

val animTest = Animatable(from = 20.px, 125.px)

lateinit var mainRedrawTemp: ComponentScope<*>

fun testUI() = aurora() {

//    colorPicker()


//    val px = 100.px
    block(
        size = size(animTest, animTest),
        color = Color.RED
    )

    mainRedrawTemp = this

//    mainRedrawTemp = row(center()) {
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
//    }
}
