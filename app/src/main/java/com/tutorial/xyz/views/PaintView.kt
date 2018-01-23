package com.tutorial.xyz.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.tutorial.xyz.CustomPaint
import com.tutorial.xyz.R

/**
 *Created by AndroidDev on 03.01.2018.
 */
class PaintView : View {

    private val currentPaint = CustomPaint()
    private var bgColor: Int = Color.WHITE

    private val paths = mutableListOf<Path>()
    private val paints = mutableListOf<CustomPaint>()
    private val undonePaths = mutableListOf<Path>()
    private val undonePaints = mutableListOf<CustomPaint>()

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        // create default paint
//        paths.add(Path())
//        paints.add(CustomPaint())

        // no blur by default
        setBlur(false)

        if (attrs == null)  // rewrite first element if attrs not null
            return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PaintView)

//        val lastPaint = paints[paints.size - 1]
//        lastPaint.color = typedArray.getColor(R.styleable.PaintView_brush_color, this.currentPaint.color)
//        lastPaint.strokeWidth = typedArray.getFloat(R.styleable.PaintView_brush_size, this.currentPaint.strokeWidth)

        typedArray.recycle()
    }

    fun setStrokeWidth(brushSize: Float) {
        currentPaint.strokeWidth = brushSize
        postInvalidate()
    }

    fun setBrushColor(brushColor: Int) {
        currentPaint.color = brushColor
        postInvalidate()
    }

    fun setBlur(isBlur: Boolean = false) {
        if (isBlur)
            currentPaint.maskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL)
        else
            currentPaint.maskFilter = null
        postInvalidate()
    }

    fun clear() {
        paths.clear()
        paints.clear()

        undonePaths.clear()
        undonePaints.clear()

        postInvalidate()
        init(null)
    }

    fun undo() {
        if (paths.isNotEmpty()) {
            undonePaths.add(paths[paths.size - 1])  // add last item
            paths.removeAt(paths.size - 1)

            undonePaints.add(paints[paints.size - 1])   // add last item
            paints.removeAt(paints.size - 1)

            postInvalidate()
        }

        if (paths.isEmpty()) {
            init(null)
        }
    }

    fun redo() {
        if (undonePaths.isNotEmpty()) {
            paths.add(undonePaths[undonePaths.size - 1])
            paints.add(undonePaints[undonePaints.size - 1])

            undonePaths.removeAt(undonePaths.size - 1)
            undonePaints.removeAt(undonePaints.size - 1)

            postInvalidate()
        }
    }

    fun setBgColor(color: Int) {
        bgColor = color
        postInvalidate()
    }

    private fun getCurrentPaint(): CustomPaint {
        val customPaint = CustomPaint()
        customPaint.strokeWidth = currentPaint.strokeWidth
        customPaint.color = currentPaint.color
        return customPaint
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(bgColor)

        for ((index, path) in paths.withIndex()) {
            canvas?.drawPath(path, paints[index])

            Log.i("TAG", "Path: " + paints[index].toString() + ", strokeWidth: " + paints[index].strokeWidth.toString() + ", color: " + paints[index].color)
        }

        Log.i("TAG", "---------------------------------------")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                addPathPaint()
                paths[paths.size - 1].moveTo(touchX, touchY)
                postInvalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                paths[paths.size - 1].lineTo(touchX, touchY)
                postInvalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                postInvalidate()
                return true
            }
            else -> return true
        }
    }

    private fun addPathPaint() {
        paths.add(Path())
        paints.add(getCurrentPaint())

        // clear undone
        undonePaths.clear()
        undonePaints.clear()
    }
}