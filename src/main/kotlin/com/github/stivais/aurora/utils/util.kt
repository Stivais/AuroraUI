package com.github.stivais.aurora.utils

import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Faster version of [forEach], which avoids allocating iterator objects,
 * when indexing with arraylists is just as fast
 */
inline fun <E> ArrayList<E>.loop(block: (E) -> Unit) {
    if (this.size == 0) return
    for (i in 0..this.size - 1) {
        block(this[i])
    }
}

/**
 * Faster version of [reversed()][reversed].[forEach], which avoids allocating a new list and iterator objects,
 * when indexing with arraylists is just as fast
 */
inline fun <E> ArrayList<E>.reverseLoop(block: (E) -> Unit) {
    if (this.size == 0) return
    for (i in this.size - 1 downTo 0) {
        block(this[i])
    }
}

/**
 * Faster version of [MutableList.removeIf], which avoids allocating iterator objects
 */
inline fun <E> ArrayList<E>.loopRemoveIf(block: (E) -> Boolean) {
    if (this.size == 0) return
    for (i in this.size - 1 downTo 0) {
        if (block(this[i])) {
            removeAt(i)
        }
    }
}

/**
 * Sets up a connection and retrieves an input stream from a provided url.
 */
fun setupConnection(
    url: String,
    timeout: Int = 5000,
    useCaches: Boolean = true,
): InputStream {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.setRequestMethod("GET")
    connection.setUseCaches(useCaches)
    connection.addRequestProperty("User-Agent", "Aurora")
    connection.setReadTimeout(timeout)
    connection.setConnectTimeout(timeout)
    connection.setDoOutput(true)
    return connection.inputStream
}
