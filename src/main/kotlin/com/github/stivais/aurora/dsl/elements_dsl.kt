package com.github.stivais.aurora.dsl

import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.elements.ElementScope
import com.github.stivais.aurora.elements.impl.Block
import com.github.stivais.aurora.elements.impl.Block.Companion.outline
import com.github.stivais.aurora.renderer.data.Radii

//
// This file contains functions
// which allows to make certain elements simpler/cleaner
//

/**
 * Creates a [Block], that is transparent with an outline.
 */
inline fun ElementScope<*>.outlineBlock(
    constraints: Constraints,
    color: Color,
    thickness: Constraint.Measurement,
    radius: Radii? = null,
    block: ElementScope<Block>.() -> Unit = {}
) = block(constraints, Color.TRANSPARENT, radius, block).outline(color, thickness)