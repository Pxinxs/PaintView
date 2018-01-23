package com.tutorial.xyz.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.tutorial.xyz.R
import java.util.*

/**
 *Created by AndroidDev on 03.01.2018.
 */
class CustomView : View {

    companion object {
        private val SIZE_DEF = 200
        private val COLOR_DEF = Color.GREEN
    }

    private lateinit var rectSquare: Rect
    private lateinit var paintSquare: Paint

    private var squareColor: Int = Color.GREEN
    private var squareSize: Int = SIZE_DEF

    private lateinit var paintCircle: Paint

    private var circleX: Float = 0f
    private var circleY: Float = 0f
    private var circleRadius = 100f

    private lateinit var image: Bitmap

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        rectSquare = Rect()
        paintSquare = Paint(Paint.ANTI_ALIAS_FLAG)

        paintCircle = Paint()
        paintCircle.isAntiAlias = true
        paintCircle.color = Color.parseColor("#00ccff")

        image = BitmapFactory.decodeResource(resources, R.drawable.image)

        viewTreeObserver.addOnGlobalLayoutListener {
            viewTreeObserver.removeOnGlobalLayoutListener({})

            val padding = 50

            image = getResizedBitmap(image, width - padding, height - padding)

            Timer().scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val newWidth = image.width - 50
                    val newHeight = image.height - 50

                    if (newWidth <= 0 || newHeight <= 0) {
                        cancel()
                        return
                    }

                    image = getResizedBitmap(image, newWidth, newHeight)
                    postInvalidate()
                }
            }, 2000L, 500L)
        }

        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomView)

        squareColor = typedArray.getColor(R.styleable.CustomView_square_color, Color.BLACK)
        squareSize = typedArray.getDimensionPixelSize(R.styleable.CustomView_square_size, SIZE_DEF)

        paintSquare.color = squareColor

        typedArray.recycle()
    }

    fun swapColor() {
        paintSquare.color = if (paintSquare.color == COLOR_DEF)
            Color.RED
        else
            COLOR_DEF

        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        // draw square
        rectSquare.left = 50
        rectSquare.top = 50
        rectSquare.right = rectSquare.left + squareSize
        rectSquare.bottom = rectSquare.top + squareSize

        canvas?.drawRect(rectSquare, paintSquare)

        // draw circle
        if (circleX == 0f || circleY == 0f) {
            circleX = width / 2f
            circleY = height / 2f
        }

        canvas?.drawCircle(circleX, circleY, circleRadius, paintCircle)

        // draw bitmap
        val imageX: Float = ((width - image.width) / 2).toFloat()
        val imageY: Float = ((height - image.height) / 2).toFloat()

        canvas?.drawBitmap(image, imageX, imageY, null)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val value: Boolean = super.onTouchEvent(event)

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y

                if (rectSquare.left < x && rectSquare.right > x) {
                    if (rectSquare.top < y && rectSquare.bottom > y) {
                        circleRadius += 10f
                        postInvalidate()
                    }
                }

                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y

                val dx = Math.pow((x - circleX).toDouble(), 2.0)
                val dy = Math.pow((y - circleY).toDouble(), 2.0)

                if (dx + dy < Math.pow(circleRadius.toDouble(), 2.0)) {
                    // touched
                    circleX = x
                    circleY = y

                    postInvalidate()

                    return true
                }

                return value
            }
        }

        return value
    }

    private fun getResizedBitmap(image: Bitmap, width: Int, height: Int): Bitmap {
        val matrix = Matrix()

        val src = RectF(0f, 0f, image.width.toFloat(), image.height.toFloat())
        val dst = RectF(0f, 0f, width.toFloat(), height.toFloat())

        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER)

        return Bitmap.createBitmap(image, 0, 0, image.width, image.height, matrix, true)
    }

}