package com.github.stivais.aurora.utils

@JvmInline
value class Timing private constructor(val value: Float) {
    companion object {
        /**
         * Returns an instance of [Timing]
         */
        val Number.seconds: Timing
            get() = Timing(this.toFloat() * 1_000_000_000f)
    }
}