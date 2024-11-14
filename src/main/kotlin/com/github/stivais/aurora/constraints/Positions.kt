package com.github.stivais.aurora.constraints

import com.github.stivais.aurora.constraints.impl.measurements.Undefined

/**
 * # Positions
 *
 * This class extends [Constraints], and only holds [positions][Constraint.Position],
 * width and height are both [Undefined]
 */
class Positions(
    x: Constraint.Position,
    y: Constraint.Position
) : Constraints(x, y, Undefined, Undefined)