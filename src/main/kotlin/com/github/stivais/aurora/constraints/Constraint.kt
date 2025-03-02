package com.github.stivais.aurora.constraints

import com.github.stivais.aurora.constraints.measurements.Measurement

class Constraints<T: Constraint>(
    val first: Measurement<T>,
    val second: Measurement<T>,
)

interface Constraint {

    interface Position : Constraint

    interface Size: Constraint

    interface Any : Position, Size

}