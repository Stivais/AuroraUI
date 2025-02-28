package com.github.stivais.aurora.element.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints
import com.github.stivais.aurora.element.Component

class Group(
    aurora: Aurora,
    position: Constraints<Constraint.Position>,
    size: Constraints<Constraint.Size>,
) : Component(aurora, position, size)