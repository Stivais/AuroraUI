package com.github.stivais.aurora.elements.impl.layout

import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.impl.size.Copying
import com.github.stivais.aurora.dsl.size
import com.github.stivais.aurora.elements.BlankElement

internal class Divider(
    width: Constraint.Size = Copying,
    height: Constraint.Size = Copying,
) : BlankElement(size(width, height)) {

    init {
        require(width != height) {
            "When using divider, it's width and height must not be equal"
        }
    }
}