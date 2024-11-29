package com.github.stivais.aurora.elements.impl

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.color.Color
import com.github.stivais.aurora.constraints.Constraint
import com.github.stivais.aurora.constraints.Positions


class TextInput(
    string: String,
    positions: Positions,
    size: Constraint.Size
) : Text(string, AuroraUI.defaultFont, Color.WHITE, positions, size) {
}