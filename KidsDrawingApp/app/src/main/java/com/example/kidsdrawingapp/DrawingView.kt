package com.example.kidsdrawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var mDrawPath : CustomPath? = null
    private var mCanvasBitmap : Bitmap? = null
    private var mDrawPaint : Paint? = null
    private var mCanvasPaint : Paint? = null
    private var mBrushSize : Float = 0.0F
    private var color = Color.BLACK
    private var canvas : Canvas? = null
    private var mPaths = ArrayList<CustomPath>()


    init {
        setUpDrawing()
    }

    private fun setUpDrawing(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(color, mBrushSize)
        mDrawPaint!!.color = color // !! throws an exception if object is null
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    // this will take the recorded paths and actually trace them on the screen
    override fun onDraw(canvas : Canvas){
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f,0f, mCanvasPaint)
        for (path in mPaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }
    }

    // this will record person's fingertip movements on the screen
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN -> { // -> is a lambda expression
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize
                //mDrawPath!!.reset() // this will reset the entire path
                //mPaths.clear() // this piece of code is not necessary and I am not sure why
                mDrawPath!!.moveTo(touchX!!,touchY!!)
                mPaths.add(mDrawPath!!)
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(touchX!!, touchY!!)
            }
            MotionEvent.ACTION_UP ->{
                mPaths.add(mDrawPath!!) // this will add the current path to the ListArray to save it
                mDrawPath = CustomPath(color, mBrushSize) // set drawPath to a new Custom Path
            }
            else -> return false
        }
        invalidate()
        return true
    }

    fun setSizeForBrush(newSize: Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        newSize, resources.displayMetrics) // this will set the size according to the screen size
        mDrawPaint!!.strokeWidth = mBrushSize
    }

    fun setColor(newColor : String){
        color = Color.parseColor(newColor) // parses a given color string into a color type
        mDrawPaint!!.color = color
    }

    internal inner class CustomPath(var color : Int, var brushThickness : Float) : Path(){  // internal will make it usable only within DrawingView

    }
}