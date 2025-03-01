@file:Suppress("unchecked_cast")

package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.constraints.measurements.Measurement
import com.github.stivais.aurora.constraints.measurements.impl.Percent
import com.github.stivais.aurora.constraints.measurements.impl.Pixel

inline val Number.px: Pixel
    get() = Pixel(this.toFloat())


inline val Number.percent: Percent
    get() = Percent(this.toFloat() / 100f)

fun <T : Constraint.Position> at(
    x: Measurement<T>,
    y: Measurement<T>
) = Constraints(
    // it shouldn't be possible to fail cast here
    x as Measurement<Constraint.Position>,
    y as Measurement<Constraint.Position>
)

fun <T : Constraint.Size> size(
    width: Measurement<T>,
    height: Measurement<T>
) = Constraints(
    width as Measurement<Constraint.Size>,
    height as Measurement<Constraint.Size>
)