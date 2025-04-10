package com.github.stivais

import com.github.stivais.aurora.renderer.Renderer
import com.github.stivais.aurora.renderer.data.Font
import com.github.stivais.aurora.renderer.data.Gradient
import com.github.stivais.aurora.renderer.data.Image

object EmptyRenderer : Renderer {

    override fun globalFontBlur(amount: Float) {
        TODO("Not yet implemented")
    }

    override fun beginFrame(width: Float, height: Float) {
    }

    override fun endFrame() {
    }

    override fun push() {
    }

    override fun pop() {
    }

    override fun scale(x: Float, y: Float) {
        
    }

    override fun translate(x: Float, y: Float) {
        
    }

    override fun rotate(amount: Float) {
        
    }

    override fun globalAlpha(amount: Float) {
        
    }

    override fun pushScissor(x: Float, y: Float, w: Float, h: Float) {
        
    }

    override fun popScissor() {
        
    }

    override fun line(x1: Float, y1: Float, x2: Float, y2: Float, thickness: Float, color: Int) {
        
    }

    override fun rect(x: Float, y: Float, w: Float, h: Float, color: Int, tl: Float, bl: Float, br: Float, tr: Float) {
        
    }

    override fun hollowRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        thickness: Float,
        color: Int,
        tl: Float,
        bl: Float,
        br: Float,
        tr: Float
    ) {
        
    }

    override fun gradientRect(
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        color1: Int,
        color2: Int,
        gradient: Gradient,
        tl: Float,
        bl: Float,
        br: Float,
        tr: Float
    ) {
        
    }

    override fun dropShadow(
        x: Float,
        y: Float,
        width: Float,
        height: Float,
        color: Int,
        blur: Float,
        spread: Float,
        tl: Float,
        bl: Float,
        br: Float,
        tr: Float
    ) {
        
    }

    override fun text(text: String, x: Float, y: Float, size: Float, color: Int, font: Font, blur: Float) {
        
    }

    override fun textWidth(text: String, size: Float, font: Font): Float {
        return 1f
    }

    override fun image(
        image: Image,
        x: Float,
        y: Float,
        w: Float,
        h: Float,
        tl: Float,
        bl: Float,
        br: Float,
        tr: Float
    ) {
        
    }

    override fun createImage(image: Image) {
        
    }

    override fun deleteImage(image: Image) {
        
    }
}