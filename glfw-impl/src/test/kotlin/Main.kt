import com.github.stivais.aurora.animation.Animation
import com.github.stivais.aurora.element.Component
import com.github.stivais.aurora.renderer.impl.RendererImpl
import com.github.stivais.aurora.utils.loop
import com.github.stivais.GLFWWindow
import com.github.stivais.aurora.utils.Timing.Companion.seconds

fun main() {

    val window = GLFWWindow(500, 500)

    val renderer = RendererImpl()
    val aurora = testUI(renderer)

    aurora.initialize(500, 500)
    aurora.upload()


    var amount = 0
    fun count(c: Component) {
        amount++
        c.children?.loop {
            count(it)
        }
    }
    count(aurora.main)
    println("component count: $amount")

    var fps = 0
    var time = System.currentTimeMillis()

    window.onClick {
        animTest.animate(0.25.seconds, style = Animation.Style.EaseOutQuint)
        mainRedrawTemp.redraw()
    }

    window.openAndRun(aurora) {
        fps++
        val start = System.currentTimeMillis()
        if (start - time > 1000) {
            animTest.animate(1.seconds, style = Animation.Style.Linear)
            mainRedrawTemp.redraw()
            window.setTitle("aurora - fps: $fps")
            time = start
            fps = 0
        }
        aurora.render()
    }
    aurora.cleanup()
    window.cleanup()
}