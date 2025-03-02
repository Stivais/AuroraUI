package com.github.stivais.aurora.utils

import org.lwjgl.opengl.GL43.*
import org.lwjgl.system.MemoryUtil

fun printSSBO(id: Int) {
    glBindBuffer(GL_ARRAY_BUFFER, id)
    val data = glMapBuffer(GL_ARRAY_BUFFER, GL_READ_ONLY)
    if (data != null) {
        println("ssbo capacity: ${data.capacity()}")
        for (i in 0..<data.capacity() / 4) {
            val int = data.getShort(i * 4)
            // skips 0
            if (int.toInt() == 0) continue
//            println("short $int")


            println("as int: ${int}, as float: ${data.getFloat(i * 4)}")
        }
    }
    glUnmapBuffer(GL_ARRAY_BUFFER)
}

private val buffer = MemoryUtil.memAllocInt(1)

fun getIntFromSSBO(ssbo: Int, index: Int): Int {
    glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo)
    glGetBufferSubData(GL_SHADER_STORAGE_BUFFER, index.toLong(), buffer)
    return buffer.get(0)
}

//fun getFloatFromSSBO(ssbo: Int, index: Int): Float {
//    glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
//    glBindBuffer(GL_SHADER_STORAGE_BUFFER, ssbo)
//    val data = glMapBuffer(GL_SHADER_STORAGE_BUFFER, GL_READ_ONLY)
//    if (data != null) return data.getFloat(index)
//    glUnmapBuffer(GL_SHADER_STORAGE_BUFFER)
//    return 0f
//}