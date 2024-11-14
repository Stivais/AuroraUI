package com.github.stivais.aurora.operations

/**
 * Operations are functions that are run every frame inside a [UI][com.github.stivais.aurora.AuroraUI],
 * usually to calculate or time something, and aren't tied to any element.
 */
fun interface Operation {

    /**
     * Function to run inside a [UI][com.github.stivais.aurora.AuroraUI]
     *
     * Returning true signals that the operation is finished and should be removed.
     */
    fun run(): Boolean
}