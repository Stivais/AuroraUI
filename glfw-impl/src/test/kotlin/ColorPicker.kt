import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.impl.measurements.Center
import com.github.stivais.aurora.dsl.px
import com.github.stivais.aurora.element.ComponentScope


fun ComponentScope<*>.colorPicker() {

    column(
        at(Center, Center),
//        color = Color.RGB(22, 22, 22)
    ) {
        block(
            size = size(50.px, 50.px),
            color = Color.RED
        )
    }


}