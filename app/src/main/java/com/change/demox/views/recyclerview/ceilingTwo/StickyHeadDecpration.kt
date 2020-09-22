package com.change.demox.views.recyclerview.ceilingTwo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.change.demox.views.recyclerview.ceiling.bean.CeilingItemModel
import java.lang.Float.max

/**
 * Created by WANG on 18/1/30.
 */
class StickyHeadDecpration(private val context: Context) : ItemDecoration() {
    private var citiList: List<CeilingItemModel>? = null
    private var paint: Paint? = null
    private var rectOver: Rect? = null
    private var index: MutableList<String> = mutableListOf()
    private var mTextPaint = TextPaint()
    private var mTextSize = dip2px(12f).toFloat()
    private var mFontMetrics: Paint.FontMetrics? = mTextPaint.fontMetrics
    private val headHeight = max(dip2px(20f).toFloat(),mTextSize)


    fun setIndexData(list: MutableList<String>) {
        index = list
    }

    /**
     * 设置Item的布局四周的间隙.
     *
     * @param outRect 确定间隙Left  Top Right Bottom 的数值的矩形.
     * @param view    RecyclerView的ChildView也就是每个Item的的布局.
     * @param parent  RecyclerView本身.
     * @param state   RecyclerView的各种状态.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (citiList == null || citiList!!.size == 0) {
            return
        }
        val adapterPosition = parent.getChildAdapterPosition(view)
        val beanByPosition = getBeanByPosition(adapterPosition) ?: return
        var preTage = -1
        val tage: Int = beanByPosition.tage
        /**
         * 1.我们要在每组的第一个位置绘制我们需要的头部.
         *
         * 2.绘制头部局有两种方式:
         *   第一种方式:给Item 的头部留出空间,也就是outRect.top.该种方式对应的就是当前的Item就是分组的第一个Item.
         *   第二种方式:给Item 的底部留出空间也就是outRect.bottpm.该种方式对应的就是当前的Item是当前分组的最后一个Item.
         *
         *   这个该怎么选择呢?
         *   1.如果第一个Item需要有分组的布局,那就选择第一种方式.
         *   2.其他可以选择第二种方式.
         *
         *
         *   该方法是给Item设置间距的,有四个属性可以设置四个间距,Left  Top Right Bottom.简单来说如果Item 的高度是50dp 我们再该方法里面设置了outRect.top = 40;
         *   也就是给Item区域的顶部多出了40dp的间隙,那么实际上该Item显示出来的高度为 50 + 40 = 90dp.正好这个40dp用来绘制我们所需要的头布局.
         *
         * 3.这里拿第一种方式,那么怎么判断当前的Item是不是分组的第一个Item呢?
         *
         *   我们再Item的设置的数据里面做好分组的标记,即属于同一组的tag都一样,不同组tag都不一样.
         *   当前Item为头布局的话就要跟前一个Item 的tag比较了,因为每个分组头部的tag的值都是不一样的,如果前一个的Tag跟当前的不一样那么,当前就是下个分组的头部.
         *
         *   a  b c    d e f   g h i
         *
         *   如果 a  d  g  是分组的头部的 .a的tag = 1 , b的tag = 2, c 的tag = 3....等等 ,前一个Item 用 preTag 来表示 ,初始值为 -1.
         *
         *   假如当前的Item为a,当前tag = 1,那么它前一个Item为空,也就是发现preTag和a的tag不一样,那么a就是分组的头部.
         *   假如当前的Item为b,当前tag = 1,那么它前一个preTag 也就是a的tag = 1,发现一样那就是是同一组的.
         *   假如当前的Item为d,当前tag = 2,那么它前一个preTag 也就是c的tag = 1,发现前一个的tag跟当前的不一样,那么当前的就是新分组的第一个头部Item.
         * */
        //一定要记住这个 >= 0
        if (adapterPosition - 1 >= 0) {
            val nextBean = getBeanByPosition(adapterPosition - 1) ?: return
            preTage = nextBean.tage
        }
        Log.e("WANG", "当前的Position is $adapterPosition 当前的Tage 是  $tage   前一个 tage  是  $preTage")

        /* 判断当前 item 是否和上一个 item 属于同一组,不同就需要设置分割线 */
        if (preTage != tage) {
            outRect.set(0, headHeight.toInt(), 0, 0)
        }
    }

    /**
     * 绘制*除Item内容*以外的东西,这个方法是再****Item的内容绘制之前****执行的,
     * 所以呢如果两个绘制区域重叠的话,Item的绘制区域会覆盖掉该方法绘制的区域.
     * 一般配合getItemOffsets来绘制分割线等.
     *
     * @param c      Canvas 画布
     * @param parent RecyclerView
     * @param state  RecyclerView的状态
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    /**
     * 绘制*除Item内容*以外的东西,这个方法是在****Item的内容绘制之后****才执行的,
     * 所以该方法绘制的东西会将Item的内容覆盖住,既显示在Item之上.
     * 一般配合getItemOffsets来绘制分组的头部等.
     *
     * @param c      Canvas 画布
     * @param parent RecyclerView
     * @param state  RecyclerView的状态
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (citiList == null || citiList!!.size == 0) {
            return
        }
        val parentLeft = parent.paddingLeft
        val parentRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        var tag = -1

        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i) ?: continue
            val adapterPosition = parent.getChildAdapterPosition(childView)
            var top = childView.top
            tag = citiList!![adapterPosition].tage
            val name = index[if (tag - 1 < 0) 0 else tag - 1]

           //屏幕上第一个可见的 ItemView 时，i == 0;
            if (adapterPosition != 0) {
                if (adapterPosition + 1 < citiList!!.size) {
                    if (adapterPosition - 1 >= 0) {
                        if (citiList!![adapterPosition - 1].tage != tag) {
                            //说明前一个tage和当前bean不相同，当前bean是组内第一个,只有组内的第一个ItemView之上才绘制
                            top -= headHeight.toInt()
                            val bottom = childView.top
                            drawHeaderRect(c,parentLeft,top,parentRight,bottom,name)
                        }
                    }
                }
            } else {
                //当 ItemView 是屏幕上第一个可见的View 时，不管它是不是组内第一个View
                //它都需要绘制它对应的 StickyHeader。
                // 还要判断当前的 ItemView 是不是它组内的最后一个 View
                top = childView.paddingTop
                if (adapterPosition == citiList!!.size-1) {
                    val suggestTop: Int = childView.bottom - headHeight.toInt()
                    // 当 ItemView 与 Header 底部平齐的时候，判断 Header 的顶部是否小于
                    // parent 顶部内容开始的位置，如果小于则对 Header.top 进行位置更新，
                    //否则将继续保持吸附在 parent 的顶部
                    if (suggestTop < top) {
                        top = suggestTop
                    }
                }
                val bottom: Int = top + headHeight.toInt()
                drawHeaderRect(c,parentLeft,top,parentRight,bottom,name)
            }
        }
    }

    private fun drawHeaderRect(c:Canvas, left:Int,top:Int,right:Int,bottom:Int,name:String=""){
        paint!!.color = Color.parseColor("#D34415")
        c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
        paint!!.color = Color.BLACK
        paint!!.getTextBounds(name, 0, name.length, rectOver)
        var titleY: Float = bottom - mFontMetrics?.descent!!
        c.drawText(name, dip2px(10f).toFloat(), titleY, paint!!)
    }

    private fun getBeanByPosition(position: Int): CeilingItemModel? {
        return if (position < citiList!!.size) {
            citiList!![position]
        } else null
    }

    /**
     * 列表的数据包括分组信息 ,每个组的开始会有个tage字段标记.通过set方法把数据给设置进去
     */
    fun setCitiList(citiList: MutableList<CeilingItemModel>?) {
        this.citiList = citiList
    }

    fun dip2px(dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    init {
        if (paint == null) {
            paint = Paint()
            paint?.isAntiAlias = true
            rectOver = Rect()
        }
    }
}