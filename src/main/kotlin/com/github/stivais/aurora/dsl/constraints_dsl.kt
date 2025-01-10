package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.constraints.impl.measurements.Percent
import com.github.stivais.aurora.constraints.impl.measurements.Pixel

inline val Number.px: Pixel
    get() = Pixel(this.toFloat())

inline val Pixel.mutable: Pixel.Mutable
    get() = Pixel.Mutable(this.value)

inline val Number.percent: Percent
    get() = Percent(this.toFloat() / 100f)