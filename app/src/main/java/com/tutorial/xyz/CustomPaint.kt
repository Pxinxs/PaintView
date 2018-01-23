package com.tutorial.xyz

import android.graphics.Color
import android.graphics.Paint

/**
 *Created by AndroidDev on 04.01.2018.
 */
class CustomPaint : Paint() {

    companion object {
        const val COLOR_DEF = Color.BLACK
        const val SIZE_DEF = 10f
    }

    init {
        this.isAntiAlias = true         // smooths out the edges
        this.style = Paint.Style.STROKE
        this.strokeWidth = SIZE_DEF
        this.color = COLOR_DEF

        this.isDither = true            // colors that are higher precision than the device are down-sampled
        this.strokeJoin = Paint.Join.ROUND
        this.strokeCap = Paint.Cap.ROUND
        this.xfermode = null            // Set or clear the transfer mode object
        this.alpha = 0xff
    }
}