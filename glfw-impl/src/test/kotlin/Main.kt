import com.github.stivais.GLFWWindow
import com.github.stivais.aurora.renderer.opengl.shader.Shader
import com.github.stivais.aurora.renderer.opengl.shader.VAOBuilder


fun main() {

    val window = GLFWWindow(500, 500)

    val shader = Shader(
        """
        #version 330 core
        layout (location = 0) in vec2 pos;
        layout (location = 1) in vec4 color;

        out vec4 vColor;

        void main() {
        
            gl_Position = vec4(pos.x, pos.y, 0.0, 1.0);
            
            vColor = color;
            
        }
        """.trimIndent(),
        """
        #version 330 core
        
        in vec4 vColor;
        
        out vec4 FragColor;
        
        void main() {
           FragColor = vColor;
        }
        """.trimIndent(),
    )

    val vaoBuilder = VAOBuilder()

    vaoBuilder.vertex(-0.5f, -0.5f, 255.toByte(), 0, 0)
    vaoBuilder.vertex(0.5f, -0.5f, 0, 255.toByte(), 0)
    vaoBuilder.vertex(0f, 0.0f, 0, 0, 255.toByte())
    vaoBuilder.upload()

    window.openAndRun {
        shader.use()
        vaoBuilder.render()
    }
    vaoBuilder.cleanup()
    window.cleanup()
}