/*
 * ViewPagerAdapter.java
 *
 * Created by xingjunchao on 2020/06/04.
 */
package com.change.demox.views.tutorial

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

/**
 * Tutorial ViewPagerAdapter
 *
 * @property viewList
 */
class ViewPagerAdapter(private val viewList: List<View>?) :
        PagerAdapter() {

    override fun getCount(): Int {
        return viewList?.size ?: 0
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(viewList!![position])
        return viewList[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(viewList!![position])
    }

}