package com.change.demox.views.spinner.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import java.util.*

class SpinnerTextView : AppCompatTextView, SpinnerPopWindow.OnRecyclerItemClickListener,
        View.OnClickListener {
    private var dataList: List<String> =
            ArrayList()
    private var popWindow: SpinnerPopWindow<String>? = null
    var onItemSelectListener: OnItemSelectListener? = null
    var onViewClickListener: OnViewClickListener? = null

    constructor(context: Context?) : super(context)
    constructor(
            context: Context,
            attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        dataList = ArrayList()
        popWindow = SpinnerPopWindow(context, dataList, this)
        setOnClickListener(this)
    }

    fun setDataList(list: List<String>) {
        dataList = list
        setNewData(list)
    }

    override fun onItemClick(position: Int, textView: TextView?, list: List<*>?) {
        val `object` = list?.get(position)
        val result = arrayOf("")
        if (null != `object`) {
            result[0] = `object`.toString()
        }
        text = result[0]
        if (null != onItemSelectListener) {
            onItemSelectListener!!.OnItemSelected(position, result[0])
        }
        popWindow!!.dismiss()
    }

    override fun onClick(v: View) {
        popWindow!!.showWindow(v)
        if (null != onViewClickListener) {
            onViewClickListener!!.viewClick(v)
        }
    }

    private fun setNewData(dataLists: List<String>?) {
        var dataList = dataLists
        if (null == dataList) {
            dataList = ArrayList()
        }
        this.dataList = dataList
        popWindow!!.setNewData(this.dataList)
    }

    interface OnViewClickListener {
        fun viewClick(v: View?)
    }

    interface OnItemSelectListener {
        fun OnItemSelected(position: Int, text: String?)
    }
}