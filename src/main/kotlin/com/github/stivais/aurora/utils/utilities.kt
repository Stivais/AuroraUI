package com.github.stivais.aurora.utils

inline fun <E> ArrayList<E>.loop(block: (E) -> Unit) {
    if (size == 0) return
    for (i in 0..<size) {
        block(this[i])
    }
}