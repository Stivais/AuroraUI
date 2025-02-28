package com.github.stivais.aurora.constraints.measurements

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.element.Component

interface Measurement<E : Constraint> {
    fun calculate(component: Component, type: Int): Float
}