package com.example.trianglerotate

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class drawing(context: Context, attrs: AttributeSet): View(context, attrs){

    private var mDrawPath : CustomPath? = null
    internal inner class CustomPath(var color : Int, var brushThickness : Float) : Path() {
    }
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 10F
    private var color = Color.BLACK
    private var canvas: Canvas? = null

    private var mPaths = ArrayList<CustomPath>()
    private var mUndoPaths = ArrayList<CustomPath>()

    var touchXo = 0.0F
    var touchYo = 0.0F

    init {
        setUpDrawing()
    }

    private fun setUpDrawing(){
        mDrawPaint = Paint()
        mDrawPath = CustomPath(Color.BLACK, 10F)
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w:Int, h:Int, oldw: Int, oldh: Int){
        super.onSizeChanged(w,h,oldw,oldh)
        mCanvasBitmap= Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    // what happens when we draw?
    override fun onDraw(canvas : Canvas){
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!, 0f,0f,mCanvasPaint)
        for (p in mPaths){
            mDrawPaint!!.strokeWidth = p.brushThickness
            mDrawPaint!!.color = p.color
            canvas.drawPath(p, mDrawPaint!!)
        }
    }

    // When do we actually want to draw?
    override fun onTouchEvent(e: MotionEvent?) : Boolean{
        when (e?.action){
            MotionEvent.ACTION_DOWN ->{
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize
                mDrawPath!!.reset()
                mDrawPath!!.moveTo(e.x, e.y)
                mPaths.add(mDrawPath!!)
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.quadTo(touchXo, touchYo,e.x, e.y)
            }
            MotionEvent.ACTION_UP -> {
                //mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }
        touchXo = e.x
        touchYo = e.y
        invalidate()
        return true
    }
}