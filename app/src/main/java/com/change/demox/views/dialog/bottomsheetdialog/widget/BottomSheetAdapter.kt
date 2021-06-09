package com.change.demox.views.dialog.bottomsheetdialog.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.change.demox.R

/**
 * [itemLayout] must has two item:
 * TextView whose id is R.id.textItem
 * ImageView whose id is R.id.imageItem
 * */
class BottomSheetAdapter(
        private val mContext: Context,
        private val dataList: List<String?>,
        @LayoutRes private val itemLayout: Int
) : BaseAdapter() {

    private var checked = -1

    fun setChecked(checked: Int) {
        this.checked = checked
    }

    fun isItemChecked(position: Int): Boolean {
        return checked == position
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val holder: ViewHolder
        var view = convertView
        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = LayoutInflater.from(mContext).inflate(itemLayout, parent, false)
            holder = ViewHolder(view)

            view.tag = holder
        }
        if (checked == position) {
            holder.imageItem.visibility = View.VISIBLE
        } else {
            holder.imageItem.visibility = View.INVISIBLE
        }
        holder.bindData(dataList[position] ?: "")

        return holder.itemView
    }

    override fun getItem(position: Int): Any = dataList[position]!!

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = dataList.size

    class ViewHolder(var itemView: View) {
        var textItem: TextView = itemView.findViewById(R.id.textItem)
        var imageItem: ImageView = itemView.findViewById(R.id.imageItem)

        fun bindData(string: String) {
            textItem.text = string
        }
    }
}