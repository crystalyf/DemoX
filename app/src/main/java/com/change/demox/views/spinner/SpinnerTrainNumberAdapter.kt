package com.change.demox.views.spinner

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.change.demox.R

/**
 * E02-1_編成設定spinner adapter
 *
 * @property list spinnerデータソース
 * @property leftTag 左のspinnerですか？? true: はい   false:ない
 */
class SpinnerTrainNumberAdapter(
        activity: AppCompatActivity?,
        var list: MutableList<String>?,
        var leftTag: Boolean,
        var resources: Resources
) : BaseAdapter() {
    var context: Context = activity!!

    /**
     * 没下拉显示的布局，用R.layout.simple_spinner_item
     */
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView: View? = view
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        convertView = layoutInflater.inflate(R.layout.simple_spinner_item, null)
        if (convertView != null) {
            val textContent = convertView.findViewById(R.id.text_content) as TextView
            textContent.text = list?.get(position) ?: ""
        }
        return convertView
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItem(position: Int): Any {
        return list?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list?.size!!
    }

    /**
     * 下拉显示的布局式样，用此回调
     * 渲染下拉时显示的布局，用R.layout.spinner_drop_down_item_left
     */
    override fun getDropDownView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView: View? = view
        val layoutInflater: LayoutInflater = LayoutInflater.from(context)
        convertView = layoutInflater.inflate(R.layout.spinner_drop_down_item_left, null)
        if (convertView != null) {
            val textContent = convertView.findViewById(R.id.text_content) as TextView
            textContent.text = list?.get(position) ?: ""
        }
        return convertView
    }
}