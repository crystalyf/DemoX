/*
 * ChildViewPager.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 子ViewPager
 *
 */
class ChildViewPager : ViewPager {

    // false： ViewPagerが左右にスライドしないようにする true： 普通ViewPager
    var isScroll = true
    var startX = 0F
    var startY = 0F

    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    )

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val curPosition: Int
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x
                startY = ev.y
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val dX = ev.x - startX
                curPosition = this.currentItem
                val count = this.adapter!!.count
                if (curPosition == 0 && dX > 0F) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else if (curPosition == count - 1 && dX < 0F) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var result = true
        try {
            result = if (!isScroll) {
                isScroll
            } else {
                // 通常のViewPagerがインターセプトイベントを処理するとき、親クラスの通常のViewPagerでonInterceptTouchEvent（）を要求します。
                super.onInterceptTouchEvent(ev)
            }
        } catch (e: Exception) {
        }
        return result
    }
}