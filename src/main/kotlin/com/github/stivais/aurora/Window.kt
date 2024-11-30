package com.github.stivais.aurora

interface Window {

    /**
     * Gets the clipboard string.
     *
     * If data isn't a string or is empty, it will return null
     */
    fun getClipboard(): String?

    /**
     * Sets the clipboard string.
     */
    fun setClipboard(string: String?)

}