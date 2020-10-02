/*
 * DragLayout.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.bottomsheet.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.R

/**
 * レイアウトをドラッグ、ボトムシートの使用
 *
 */
class DragLayout : RelativeLayout {

    /**
     * ViewDragHelper是针对 ViewGroup 中的拖拽和重新定位 views 操作时提供了一系列非常有用的方法和状态追踪。基本上使用在自定义ViewGroup处理拖拽中
     */

    private var dragHelper: ViewDragHelper? = null
    private var dragView: View? = null
    private var contentView: FrameLayout? = null
    private var progressLayout: FrameLayout? = null
    private var recyclerView: RecyclerView? = null
    private var netWorkErrorLayout: LinearLayout? = null
    private var maintenanceLayout: LinearLayout? = null
    private var dragRange = 0
    private var topMargin = 0
    var currentState = State.AT_BOTTOM

    companion object {
        const val SUPER_STATUS_KEY = "superState"
        const val CURRENT_STATUS_KEY = "state"
    }

    object State {
        const val AT_BOTTOM = 0
        const val AT_MIDDLE = 1
        const val AT_TOP = 2
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
            context,
            attrs
    ) {
        init()
    }

    constructor(
            context: Context?,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        dragHelper = ViewDragHelper.create(this, callback)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        dragView = findViewById(R.id.dragView)
        contentView = findViewById(R.id.frame_list)
        recyclerView = findViewById(R.id.rv_machine_part)
    }

    private val callback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(
                child: View,
                pointerId: Int
        ): Boolean {
            return child === dragView
        }

        override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int,
                dx: Int,
                dy: Int
        ) {
            dragView!!.layout(
                    width - dragView!!.width,
                    top,
                    width,
                    top + dragView!!.height
            )
            contentView!!.layout(
                    width - contentView!!.width,
                    top + dragView!!.height - 2,
                    width,
                    height
            )
            recyclerView!!.layout(
                    0,
                    0,
                    contentView!!.width,
                    dragRange - top
            )
        }

        override fun clampViewPositionVertical(
                child: View,
                top: Int,
                dy: Int
        ): Int {
            val topBound = height - dragRange - dragView!!.height
            val bottomBound = height - dragView!!.height
            topMargin = top
            return topBound.coerceAtLeast(top).coerceAtMost(bottomBound)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return dragRange
        }

        override fun onViewReleased(
                releasedChild: View,
                xVel: Float,
                yVel: Float
        ) {
            super.onViewReleased(releasedChild, xVel, yVel)
            if (topMargin < dragRange / 4) {
                smoothToTop()
            } else if (topMargin > dragRange / 4 && topMargin < 3 * dragRange / 4) {
                smoothToMid()
            } else if (topMargin > 3 * dragRange / 4) {
                smoothToBottom()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        dragRange = contentView!!.measuredHeight
    }

    override fun onLayout(
            changed: Boolean,
            l: Int,
            t: Int,
            r: Int,
            b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        reLayout(currentState)
    }

    fun reLayout(currentState: Int) {
        when (currentState) {
            State.AT_BOTTOM -> {
                dragView!!.layout(
                        width - dragView!!.width,
                        height - dragView!!.height,
                        width,
                        height
                )
                contentView!!.layout(width - dragView!!.width, height, width, height)
            }
            State.AT_MIDDLE -> {
                dragView!!.layout(
                        width - dragView!!.width,
                        (height - dragView!!.height) / 2,
                        width,
                        (height + dragView!!.height) / 2
                )
                contentView!!.layout(
                        width - dragView!!.width,
                        (height + dragView!!.height) / 2 - 2,
                        width,
                        height
                )
                recyclerView!!.layout(
                        0,
                        0,
                        contentView!!.width,
                        contentView!!.height
                )
                recyclerView!!.layoutParams.height = (height - dragView!!.height) / 2
            }
            State.AT_TOP -> {
                dragView!!.layout(
                        width - dragView!!.width,
                        height - dragRange - dragView!!.height,
                        width,
                        height - dragRange
                )
                contentView!!.layout(
                        width - dragView!!.width,
                        dragView!!.height - 2,
                        width,
                        height
                )
                recyclerView!!.layout(
                        0,
                        0,
                        contentView!!.width,
                        contentView!!.height
                )
                recyclerView!!.layoutParams.height = height - dragView!!.height
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val pointY = ev?.y
        val pointX = ev?.x

        if (pointY != null && pointX != null) {
            return if (pointY >= dragView?.top!! && pointY <= dragView!!.bottom && pointX >= dragView!!.left && pointX <= dragView!!.right) {
                if (ev.action == MotionEvent.ACTION_DOWN) {
                    onTouchEvent(ev)
                } else {
                    super.dispatchTouchEvent(ev)
                }
            } else
                super.dispatchTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragHelper!!.processTouchEvent(event)
        return true
    }

    private fun smoothToTop() {
        currentState = State.AT_TOP
        recyclerView!!.layoutParams.height = height - dragView!!.height
        if (dragHelper!!.smoothSlideViewTo(
                        dragView!!,
                        width - dragView!!.width,
                        height - dragRange - dragView!!.height
                )
        ) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    fun smoothToMid() {
        currentState = State.AT_MIDDLE
        recyclerView!!.layoutParams.height = (height - dragView!!.height) / 2
        if (dragHelper!!.smoothSlideViewTo(
                        dragView!!,
                        width - dragView!!.width,
                        (height - dragView!!.height) / 2
                )
        ) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun smoothToBottom() {
        currentState = State.AT_BOTTOM
        if (dragHelper!!.smoothSlideViewTo(
                        dragView!!,
                        width - dragView!!.width,
                        height - dragView!!.height
                )
        ) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun computeScroll() {
        if (dragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(SUPER_STATUS_KEY, super.onSaveInstanceState())
        bundle.putInt(CURRENT_STATUS_KEY, currentState)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var mState = state
        if (mState is Bundle) {
            val bundle = mState
            currentState = bundle.getInt(CURRENT_STATUS_KEY)
            mState = bundle.getParcelable(SUPER_STATUS_KEY)
            reLayout(currentState)
        }
        super.onRestoreInstanceState(mState)
    }
}