package com.github.stivais

import com.github.stivais.aurora.AuroraUI
import com.github.stivais.aurora.Window

class EmptyWindow : Window {

    fun open(ui: AuroraUI) {
        ui.initialize(1920, 1080, this)
        while (true) {
            ui.render()
        }
    }

    override fun getClipboard(): String? = null


    override fun setClipboard(string: String?) {
        //
    }

}