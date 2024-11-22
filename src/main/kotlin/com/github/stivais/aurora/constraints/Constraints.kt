package com.github.stivais.aurora.constraints

/**
 * # Constraints
 *
 * This class holds the [positions][Constraint.Position] and [sizes][Constraint.Size] for an element to utilize
 */
open class Constraints(
    var x: Constraint.Position,
    var y: Constraint.Position,
    var width: Constraint.Size,
    var height: Constraint.Size,
) {

    /**
     * Utility function to return either x constraint or y constraint based on [horizontal]
     */
    fun getPosition(horizontal: Boolean) = if (horizontal) x else y

    /**
     * Utility function to return either width constraint or height constraint based on [horizontal]
     */
    fun getSize(horizontal: Boolean) = if (horizontal) width else height

    /**
     * Utility function to return if either width or height constraints [relies on children][Constraint.reliesOnChildren]
     */
    fun sizeReliesOnChildren() = width.reliesOnChildren() || height.reliesOnChildren()
}