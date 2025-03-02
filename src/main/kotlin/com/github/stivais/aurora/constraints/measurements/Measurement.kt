package com.github.stivais.aurora.constraints.measurements

import com.github.stivais.aurora.component.Component
import com.github.stivais.aurora.constraints.Constraint

interface Measurement<out E : Constraint> {
    fun calculate(component: Component, type: Int): Float
}