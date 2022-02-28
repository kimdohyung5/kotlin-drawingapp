package com.kimdo.drawingapp

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet

// color와 stroke size를 받아서 적용한다.

class DrawView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private var mDrawPath: CustomPath? = null
//    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null               // 실제로 많이 쓰임.
//    private var mCanvasPaint:Paint? = null              // 한번 설정하고 나서 땡침. 이미지 그려줄때 현재는 쓰임이 없음.

    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK;
//    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()
    init {
        setupDrawing()
    }
    private fun setupDrawing() {

        mDrawPath = CustomPath(color, mBrushSize)


        mDrawPaint = Paint()
        mDrawPaint?.color = color
        mDrawPaint?.style = Paint.Style.STROKE
        mDrawPaint?.strokeJoin = Paint.Join.ROUND
        mDrawPaint?.strokeCap = Paint.Cap.ROUND

//        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        Log.i("kimdo","onSizeChanged hhhh ${w}, ${h}")
//        canvas = Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        mCanvasBitmap?.let {
//            canvas.drawBitmap(it, 0f, 0f, mCanvasPaint)
//        }
        for(p in mPaths) {
            mDrawPaint?.strokeWidth = p.brushThickness
            mDrawPaint?.color = p.color
            canvas.drawPath(p, mDrawPaint!!)
        }

        if(!mDrawPath!!.isEmpty) {
            mDrawPaint?.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint?.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath?.color = color
                mDrawPath?.brushThickness = mBrushSize

                mDrawPath?.reset()
                mDrawPath?.moveTo( touchX, touchY)
            }
            MotionEvent.ACTION_MOVE -> {
                mDrawPath?.lineTo( touchX, touchY)
            }
            MotionEvent.ACTION_UP -> {
                mPaths.add( mDrawPath!!)
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }
        invalidate()
        return true
    }



    internal inner class CustomPath(var color:Int, var brushThickness:Float) : Path()
}