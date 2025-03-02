package com.github.stivais.aurora.component.impl

import com.github.stivais.aurora.Aurora
import com.github.stivais.aurora.component.Component
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Constraints

class Group(
    aurora: Aurora,
    position: Constraints<Constraint.Position>,
    size: Constraints<Constraint.Size>,
) : Component(aurora, position, size)