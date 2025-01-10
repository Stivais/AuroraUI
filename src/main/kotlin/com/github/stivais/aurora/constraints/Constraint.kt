package com.github.stivais.aurora.constraints

import com.github.stivais.aurora.element.Component

sealed interface Constraint {

    fun calculate(component: Component, type: Int): Float

    fun mutates(component: Component): Boolean

    interface Position : Constraint {

        fun calculatePos(component: Component, horizontal: Boolean): Float

        override fun calculate(component: Component, type: Int): Float {
            return calculatePos(component, type == 0)
        }
    }

    interface Size : Constraint {

        fun calculateSize(component: Component, horizontal: Boolean): Float

        override fun calculate(component: Component, type: Int): Float {
            return calculateSize(component, type == 2)
        }

    }

    interface Measurement : Position, Size {

        override fun calculatePos(component: Component, horizontal: Boolean): Float {
            return calculate(component, if (horizontal) 0 else 1)
        }

        override fun calculateSize(component: Component, horizontal: Boolean): Float {
            return calculate(component, if (horizontal) 2 else 3)
        }

        override fun calculate(component: Component, type: Int): Float
    }

    fun Component.getSize(horizontal: Boolean): Float {
        return if (horizontal) width else height
    }
}