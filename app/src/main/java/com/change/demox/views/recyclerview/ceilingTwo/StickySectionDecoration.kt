package com.change.demox.views.recyclerview.ceilingTwo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.change.demox.views.recyclerview.ceiling.bean.CeilingItemModel

/**
 * Created by frank on 2017/4/11.
 */
class StickySectionDecoration(private val context: Context) : ItemDecoration() {

    private var dataList: List<CeilingItemModel>? = null

    private var mHeaderHeight: Int
    private val mDividerHeight: Int

    //用来绘制Header上的文字
    private val mTextPaint: TextPaint
    private val mPaint: Paint
    private val mTextSize: Float
    private val mFontMetrics: Paint.FontMetrics
    private val mTextOffsetX = 0f
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        //如果是组内的第一个则将间距撑开为一个Header的高度，这样就能看到推着走的效果了
        if (isGroupFirstItem(position, dataList!![position].tage)) {
            outRect.top = mHeaderHeight
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val view = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(view)
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            //屏幕上第一个可见的 ItemView 时，i == 0;
            if (i != 0) {
                //只有组内的第一个ItemView之上才绘制
                if (isGroupFirstItem(index, dataList!![index].tage)) {
                    val top = view.top - mHeaderHeight
                    val bottom = view.top
                    drawHeaderRect(c, left, top, right, bottom, dataList!![index].groupFieldName)
                }
            } else {
                //当 ItemView 是屏幕上第一个可见的View 时，不管它是不是组内第一个View
                //它都需要绘制它对应的 StickyHeader。

                // 还要判断当前的 ItemView 是不是它组内的最后一个 View
                var top = parent.paddingTop
                if (isGroupLastItem(index, dataList!![index].tage)) {
                    val suggestTop = view.bottom - mHeaderHeight
                    // 当 ItemView 与 Header 底部平齐的时候，判断 Header 的顶部是否小于
                    // parent 顶部内容开始的位置，如果小于则对 Header.top 进行位置更新，
                    //否则将继续保持吸附在 parent 的顶部
                    if (suggestTop < top) {
                        top = suggestTop
                    }
                }
                val bottom = top + mHeaderHeight
                drawHeaderRect(c, left, top, right, bottom, dataList!![index].groupFieldName)
            }
        }
    }

    private fun drawHeaderRect(c: Canvas, left: Int, top: Int, right: Int, bottom: Int, title: String) {
        //绘制Header
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        val titleX = left + mTextOffsetX
        val titleY = bottom - mFontMetrics.descent
        //绘制Title
        c.drawText(title, titleX, titleY, mTextPaint)
    }

    private fun isGroupFirstItem(index: Int, tag: Int): Boolean {
        var result = false
        if (index - 1 >= 0) {
            val frontTag = dataList!![index - 1].tage
            if (tag != frontTag) {
                //说明是这个组的第一个数据
                result = true
            }
        }else{
            //第一个必然是一个组的头部
            result = true
        }
        return result
    }

    private fun isGroupLastItem(index: Int, tag: Int): Boolean {
        var result = false
        if (index + 1 < dataList!!.size) {
            val nextTag = dataList!![index + 1].tage
            if (tag != nextTag) {
                //说明是这个组的最后一个数据
                result = true
            }
        }
        return result
    }

    /**
     * 列表的数据包括分组信息 ,每个组的开始会有个tage字段标记.通过set方法把数据给设置进去
     */
    fun setCitiList(citiList: MutableList<CeilingItemModel>?) {
        this.dataList = citiList
    }

    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    init {
        mDividerHeight = dip2px(2f)
        mHeaderHeight = dip2px(24f)
        mTextSize = dip2px(22f).toFloat()
        mHeaderHeight = Math.max(mHeaderHeight.toFloat(), mTextSize).toInt()
        mTextPaint = TextPaint()
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = mTextSize
        mFontMetrics = mTextPaint.fontMetrics
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = Color.YELLOW
    }
}