package com.change.demox.views.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.change.demox.R

class SlideView : View {
    var onSlideDone: () -> Unit = {}

    private var mPaint: Paint
    private var mSlideBitmap: Bitmap? = null
    private var mMargin: Int = 0
    private var mSlideBgWidth: Int = 0
    private var mSlideBgHeight: Int = 0
    private var mSlideBgRadius: Int = 0
    private var mBackgroundColor: Int = 0
    private var mSlideColor: Int = 0
    private var mTextColor: Int = 0
    private var mSlideLeft: Int = 0
    private var mSlideIsMoving: Boolean = false
    private var mLastX: Int = 0
    private var mIsStartAnimator: Boolean = false
    private var mTextSize: Int = 0
    private var mTextRect: Rect? = null
    private var mTrColor: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    init {
        mPaint = Paint()
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND

        mMargin = 18
        mSlideBgWidth = 200
        mSlideBgHeight = 60
        mSlideBgRadius = 35
        mTextSize = 14
        mBackgroundColor = ContextCompat.getColor(context, R.color.color_slide_bg)
        mSlideColor = ContextCompat.getColor(context, R.color.color_slide_over_bg)
        mTextColor = ContextCompat.getColor(context, R.color.white)
        mTrColor = ContextCompat.getColor(context, R.color.transparent)

        mSlideLeft = 0
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mSlideBitmap = getBitmap(2 * mMargin)
        mTextRect = Rect(mSlideBitmap!!.width, 0, mSlideBgWidth - mMargin / 2, mSlideBgHeight)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mSlideBitmap?.recycle()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val action = event.action

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = event.rawX.toInt()

                if (mIsStartAnimator) {
                    return false
                }

                // check finger is touch above slide
                if (event.x > mSlideLeft && event.x < mSlideBitmap!!.width + mSlideLeft + mMargin) {
                    mSlideIsMoving = true
                    return true
                }

                return false
            }

            MotionEvent.ACTION_MOVE -> {

                val dx = event.rawX.toInt() - mLastX

                mSlideLeft += dx

                if (mSlideLeft < 0) {
                    mSlideLeft = 0
                }

                if (mSlideLeft >= mSlideBgWidth - mSlideBitmap!!.width) {
                    mSlideLeft = mSlideBgWidth - mSlideBitmap!!.width
                }

                if (mSlideIsMoving) {
                    invalidate()
                    mLastX = event.rawX.toInt()
                }
            }
            MotionEvent.ACTION_UP -> if (mSlideIsMoving) {
                if (mSlideLeft + mSlideBitmap!!.width >= mSlideBgWidth) {
                    onSlideDone()
                } else {
                    if (mSlideLeft > 0) {
                        backZero()
                    }
                }
                mSlideIsMoving = false
            }
            else -> {
            }
        }

        return super.onTouchEvent(event)

    }

    override fun onDraw(canvas: Canvas) {
        // mPaint mBackgroundColor
        mPaint.color = mBackgroundColor
        canvas.drawRoundRect(0F, 0F, mSlideBgWidth.toFloat(),
                mSlideBgHeight.toFloat(), mSlideBgRadius.toFloat(), mSlideBgRadius.toFloat(), mPaint)

        //mPaint text
        mPaint.textSize = mTextSize.toFloat()
        mPaint.color = mTrColor
        canvas.drawRect(mTextRect!!, mPaint)
        mPaint.color = mTextColor
        mPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        val fontMetrics = mPaint.fontMetricsInt

        val baseline = (mTextRect!!.bottom + mTextRect!!.top - fontMetrics.bottom - fontMetrics.top) / 2
        mPaint.textAlign = Paint.Align.CENTER

        canvas.drawText(context.getString(R.string.slide_text),
                mTextRect!!.centerX().toFloat(), baseline.toFloat(), mPaint)

        //mPaint mSlideColor bg
        mPaint.color = mSlideColor
        canvas.drawRoundRect(0F, 0F, (mSlideLeft + 2 * mMargin).toFloat(),
                mSlideBgHeight.toFloat(), mSlideBgRadius.toFloat(), mSlideBgRadius.toFloat(), mPaint)

        //mPaint slide
        canvas.drawBitmap(mSlideBitmap!!, mSlideLeft.toFloat(), 0f, mPaint)

        super.onDraw(canvas)
    }

    fun setSlideLeft(slideLeft: Int) {
        this.mSlideLeft = slideLeft
        invalidate()
    }

    private fun backZero() {

        val slideAnimator = ObjectAnimator.ofInt(this,
                "slideLeft", this.mSlideLeft, 0)
        slideAnimator
                .setDuration(ANIMATOR_DURATION.toLong()).start()

        slideAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                mIsStartAnimator = true
            }

            override fun onAnimationEnd(animation: Animator) {
                mIsStartAnimator = false
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

    }

    private fun getBitmap(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.ic_accept_phone, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.ic_accept_phone, options)
    }

    companion object {
        val ANIMATOR_DURATION = 100
    }
}
