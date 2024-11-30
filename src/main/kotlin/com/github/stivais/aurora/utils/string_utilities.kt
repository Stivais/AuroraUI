package com.github.stivais.aurora.utils

import kotlin.math.max
import kotlin.math.min

fun String.substringSafe(from: Int, to: Int): String {
    val f = min(from, to)
    val t = max(to, from)
    return substring(f, t)
}

fun String.removeRangeSafe(from: Int, to: Int): String {
    val f = min(from, to)
    val t = max(to, from)
    return removeRange(f, t)
}

fun String.dropAt(at: Int, amount: Int): String {
    return removeRangeSafe(at, at + amount)
}