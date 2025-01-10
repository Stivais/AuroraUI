import com.github.stivais.aurora.renderer.shader.impl.Shader
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL43.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.IntBuffer
import kotlin.random.Random

fun main() {
    if (!glfwInit()) {
        throw IllegalStateException("Unable to initialize GLFW")
    }

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

    val window = glfwCreateWindow(500, 500, "Compute Shader Example", 0, 0)
    if (window == MemoryUtil.NULL) { throw RuntimeException("Failed to create the GLFW window") }

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)

    GL.createCapabilities()
    //----------------------------------------------------------------//
    //----------------------------------------------------------------//

    // Start


    val vao = glGenVertexArrays()

    val vertShader = Shader.create(
        "assets/shader/screen.vert",
        "assets/shader/screen.frag"
    )

    val computeShader = Shader.create(
        "assets/shader/compute_shader.glsl"
    )

    val componentBuffer: ByteBuffer = MemoryUtil.memAlloc(2048 * 4)
    val pointerBuffer: IntBuffer = MemoryUtil.memAllocInt(2048)

    val vertexSSBO = glGenBuffers()
    val componentSSBO = glGenBuffers()
    val pointerSSBO = glGenBuffers()
    val dataSSBO = glGenBuffers()

    glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, vertexSSBO)
    glBufferData(GL_SHADER_STORAGE_BUFFER, 2048 * 4, GL_DYNAMIC_DRAW)

    glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, componentSSBO)
    glBufferData(GL_SHADER_STORAGE_BUFFER, 2048 * 4, GL_DYNAMIC_DRAW)

    glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, pointerSSBO)
    glBufferData(GL_SHADER_STORAGE_BUFFER, 2048 * 4, GL_DYNAMIC_DRAW)

    glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 3, dataSSBO)
    glBufferData(GL_SHADER_STORAGE_BUFFER, 8, GL_DYNAMIC_DRAW)

    // unbind to simulate in a full app
    glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, 0)
    glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, 0)
    glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, 0)
    glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 3, 0)

    var amount = 0
    var current = 0

    fun addTriangle(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) {
        pointerBuffer.put(current)
        componentBuffer
            .putInt(0)
            .putFloat(x1)
            .putFloat(y1)
            .putFloat(x2)
            .putFloat(y2)
            .putFloat(x3)
            .putFloat(y3)
        current += 7
        amount += 1
    }

    fun addCircle(x: Float, y: Float, radius: Float) {
        pointerBuffer.put(amount, current)
        componentBuffer
            .putInt(2)
            .putFloat(x)
            .putFloat(y)
            .putFloat(radius)
        current += 4
        amount += 1
    }

    fun addRect(x: Float, y: Float, width: Float, height: Float) {
        pointerBuffer.put(amount, current)

        componentBuffer
            .putInt(1)
            .putFloat(x)
            .putFloat(y)
            .putFloat(width)
            .putFloat(height)
        current += 5
        amount += 1
    }

    addCircle(100f, 100f, 50f)

    addCircle(400f, 400f, 100f)
    addRect(250f, 250f, 100f, 100f)

    var triangleAmount = 0

    fun upload() {
        componentBuffer.position(0)
        componentBuffer.limit(current * 4)
        pointerBuffer.position(0)
        pointerBuffer.limit(amount)

        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, vertexSSBO)

        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, componentSSBO)
        glBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, componentBuffer)

        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, pointerSSBO)
        glBufferSubData(GL_SHADER_STORAGE_BUFFER, 0, pointerBuffer)

        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 3, dataSSBO)

        computeShader.start()
        computeShader.dispatch(amount, 1, 1)

        triangleAmount = getSSBOData(dataSSBO)

        amount = 0
        current = 0
        componentBuffer.clear()
        pointerBuffer.clear()
    }

    upload()

    val projectionMatrix = org.joml.Matrix4f()

    glfwSetMouseButtonCallback(window) { _, button, action, _ ->
        if (button == 0 && action == GLFW_PRESS) {
            val x = Random.nextDouble(100.0, 400.0).toFloat()
            val y = Random.nextDouble(100.0, 400.0).toFloat()
            val r = Random.nextDouble(10.0, 100.0).toFloat()

            addCircle(x, y, r)

            upload()
        }
    }

    // Main render loop
    while (!GLFW.glfwWindowShouldClose(window)) {
//

        glClear(GL_COLOR_BUFFER_BIT)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_ALPHA)

        vertShader.start()

        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 0, vertexSSBO)
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 1, componentSSBO)
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 2, pointerSSBO)
        glBindBufferBase(GL_SHADER_STORAGE_BUFFER, 3, dataSSBO)

        projectionMatrix.setOrtho(0f, 500f, 500f, 0f, 1000f, -1000f)
        vertShader.setMat4f("projection", projectionMatrix)

        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLES, 0, triangleAmount)

        glfwSwapBuffers(window)
        glfwPollEvents()
    }

    glfwTerminate()
}

fun readSSBO(id: Int) {
    GL15.glBindBuffer(GL_SHADER_STORAGE_BUFFER, id)
    val data = glMapBuffer(GL_SHADER_STORAGE_BUFFER, GL_READ_ONLY)
    if (data != null) {
        println("buffer capacity: ${data.capacity()}")
        for (i in 0..<data.capacity() / 4) {
            val int = data.getInt(i * 4)
            if (int == 0) {
                continue
            }
            println("int: ${int}, float: ${data.getFloat(i * 4)}")
        }
    }
    glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
}

fun getSSBOData(id: Int): Int {
    glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
    GL15.glBindBuffer(GL_SHADER_STORAGE_BUFFER, id)
    val data = glMapBuffer(GL_SHADER_STORAGE_BUFFER, GL_READ_ONLY)
    if (data != null) {
        return data.getInt(0)
    }
    glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
    return 0
}