package com.change.demox.views.bottomsheet.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
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
     *
     *  Android布局中要对布局中的控件进行自由拖动,一种方法是重写父类点击事件的方法, 对触摸事件进行处理, 这种方法代码量过大暂不讨论.
     *  另一种方法是利用ViewDragHelper来处理触摸事件
     */
    private var dragHelper: ViewDragHelper? = null

    //拖拽布局的横条
    private var dragViewLayout: View? = null

    //拖拽显示出来的列表父布局
    private var contentView: FrameLayout? = null

    //拖拽显示出来的列表
    private var recyclerView: RecyclerView? = null
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
        dragViewLayout = findViewById(R.id.dragView)
        contentView = findViewById(R.id.frame_list)
        recyclerView = findViewById(R.id.rv_machine_part)
    }

    /**
     * 处理CallBack逻辑：
     *
    ​CallBack中判断控件是否可以拖动有以下几个关键方法:

    tryCaptureView() : 判断View是否是可拖动, 返回true表示可该view可拖动

    clampViewPositionHorizontal() / clampViewPositionVertical() : 决定子view在水平/垂直方向上应该移动到的位置, 返回0表示不允许该方向上的运动

    getViewHorizontalDragRange() / getViewVerticalDragRange() : 以像素为单位返回子view在水平/垂直方向上可移动的距离, 返回0表示不能在该方向上进行移动

    onViewPositionChanged()会在控件位置变化时不断被回调

    onViewReleased()则是在手指松开时进行回调

     */

    private val callback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(
                child: View,
                pointerId: Int
        ): Boolean {
            //若控件是dragView,那么可拖动
            return child === dragViewLayout
        }

        /**
         * 限制View纵向的拖拉操作
         *
         * 返回0表示不允许该方向上的运动
         */
        override fun clampViewPositionVertical(
                child: View,
                top: Int,
                dy: Int
        ): Int {
            //滑动限制距离，控制view纵向不超出屏幕
            val topBound = height - dragRange - dragViewLayout!!.height
            val bottomBound = height - dragViewLayout!!.height
            topMargin = top
            return topBound.coerceAtLeast(top).coerceAtMost(bottomBound)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return dragRange
        }


        /**
         * 重写, 对自定义布局进行一些处理
         */
        override fun onViewReleased(
                releasedChild: View,
                xVel: Float,
                yVel: Float
        ) {
            super.onViewReleased(releasedChild, xVel, yVel)
            //根据上边距，判断滑动显示的bottomSheet是显示在屏幕中的什么位置:top,middle,bottom
            if (topMargin < dragRange / 4) {
                smoothToTop()
            } else if (topMargin > dragRange / 4 && topMargin < 3 * dragRange / 4) {
                smoothToMid()
            } else if (topMargin > 3 * dragRange / 4) {
                smoothToBottom()
            }
        }

        /**
         * 重写, 对自定义布局进行一些处理
         *
         * 记录控件所在的位置, 然后ViewGroup的onLayout()方法并指定其位置就可以了
         */
        override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int,
                dx: Int,
                dy: Int
        ) {
            dragViewLayout!!.layout(
                    width - dragViewLayout!!.width,
                    top,
                    width,
                    top + dragViewLayout!!.height
            )
            contentView!!.layout(
                    width - contentView!!.width,
                    top + dragViewLayout!!.height - 2,
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
    }

    /**
     * 测量控件的高度，可以得到每个控件的最终高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val pointY = ev?.y
        val pointX = ev?.x

        if (pointY != null && pointX != null) {
            return if (pointY >= dragViewLayout?.top!! && pointY <= dragViewLayout!!.bottom && pointX >= dragViewLayout!!.left && pointX <= dragViewLayout!!.right) {
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

    /**
     * 写拖拽的同时，一般要重写onTouchEvent()方法, 使ViewDragHelper接管触摸事件的处理
     *
     */

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        //通过这个方法判断是否处理拦截的触摸事件，这里使ViewDragHelper接管触摸事件的处理
        dragHelper!!.processTouchEvent(event)
        return true
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

    private fun reLayout(currentState: Int) {
        when (currentState) {
            State.AT_BOTTOM -> {
                dragViewLayout!!.layout(
                        width - dragViewLayout!!.width,
                        height - dragViewLayout!!.height,
                        width,
                        height
                )
                contentView!!.layout(width - dragViewLayout!!.width, height, width, height)
            }
            State.AT_MIDDLE -> {
                dragViewLayout!!.layout(
                        width - dragViewLayout!!.width,
                        (height - dragViewLayout!!.height) / 2,
                        width,
                        (height + dragViewLayout!!.height) / 2
                )
                contentView!!.layout(
                        width - dragViewLayout!!.width,
                        (height + dragViewLayout!!.height) / 2 - 2,
                        width,
                        height
                )
                recyclerView!!.layout(
                        0,
                        0,
                        contentView!!.width,
                        contentView!!.height
                )
                recyclerView!!.layoutParams.height = (height - dragViewLayout!!.height) / 2
            }
            State.AT_TOP -> {
                dragViewLayout!!.layout(
                        width - dragViewLayout!!.width,
                        height - dragRange - dragViewLayout!!.height,
                        width,
                        height - dragRange
                )
                contentView!!.layout(
                        width - dragViewLayout!!.width,
                        dragViewLayout!!.height - 2,
                        width,
                        height
                )
                recyclerView!!.layout(
                        0,
                        0,
                        contentView!!.width,
                        contentView!!.height
                )
                recyclerView!!.layoutParams.height = height - dragViewLayout!!.height
            }
        }
    }

    private fun smoothToTop() {
        currentState = State.AT_TOP
        recyclerView!!.layoutParams.height = height - dragViewLayout!!.height
        if (dragHelper!!.smoothSlideViewTo(
                        dragViewLayout!!,
                        width - dragViewLayout!!.width,
                        height - dragRange - dragViewLayout!!.height
                )
        ) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun smoothToMid() {
        currentState = State.AT_MIDDLE
        recyclerView!!.layoutParams.height = (height - dragViewLayout!!.height) / 2
        if (dragHelper!!.smoothSlideViewTo(
                        dragViewLayout!!,
                        width - dragViewLayout!!.width,
                        (height - dragViewLayout!!.height) / 2
                )
        ) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    private fun smoothToBottom() {
        currentState = State.AT_BOTTOM
        if (dragHelper!!.smoothSlideViewTo(
                        dragViewLayout!!,
                        width - dragViewLayout!!.width,
                        height - dragViewLayout!!.height
                )
        ) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }
}