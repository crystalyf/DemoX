package com.change.demox.views.spinner.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.R
import java.util.*

class SpinnerPopWindow<T> constructor(
        private val context: Context?,
        list: List<T>?,
        listener: OnRecyclerItemClickListener?
) : PopupWindow(
        LayoutInflater.from(context)
                .inflate(R.layout.spinner_window_layout, null),
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var rvList: RecyclerView
    private var list: List<T>?
    private var popAdapter: PopUpAdapter? = null
    private var onItemClickListener: OnRecyclerItemClickListener?

    private fun init() {
        width =
                context?.resources?.getDimensionPixelOffset(R.dimen.train_number_input_spinner_layout_drop_width)
                        ?: 0
        height =
                context?.resources?.getDimensionPixelOffset(R.dimen.train_number_input_spinner_layout_drop_height)
                        ?: 0
        isFocusable = true
        rvList = contentView.findViewById(R.id.rv_list)
        if (null == list) {
            list = ArrayList()
        }
        popAdapter = PopUpAdapter(list!!)
        rvList.adapter = popAdapter
    }

    fun setNewData(list: List<T>) {
        this.list = list
        popAdapter!!.setNewData(list)
    }

    fun showWindow(view: View) {
        if (!isShowing) {
            //コントロールの場所を取得、Androidシステム> 7.0
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1])
        }
    }

    private inner class PopUpAdapter(private var list: List<T>) :
            RecyclerView.Adapter<PopUpAdapter.ViewHolder>() {
        override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
        ): ViewHolder {
            val convertView = inflater.inflate(
                    R.layout.spinner_drop_down_item,
                    parent,
                    false
            )
            return ViewHolder(
                    convertView
            )
        }

        override fun onBindViewHolder(
                holder: ViewHolder,
                position: Int
        ) {
            holder.textContent.text = list[position].toString()
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemCount(): Int {
            return list.size
        }

        fun setNewData(list: List<T>) {
            this.list = list
            notifyDataSetChanged()
        }

        private inner class ViewHolder(itemView: View) :
                RecyclerView.ViewHolder(itemView) {
            var textContent: TextView = itemView.findViewById(R.id.text_content)

            init {
                itemView.findViewById<View>(R.id.linearlayout_root)
                        .setOnClickListener {
                            if (null != onItemClickListener) {
                                onItemClickListener!!.onItemClick(adapterPosition, textContent, list)
                            }
                        }
            }
        }

    }

    interface OnRecyclerItemClickListener {
        //RecyclerViewのクリックイベント、表示するコールバック情報
        fun onItemClick(
                position: Int,
                textView: TextView?,
                list: List<*>?
        )
    }

    init {
        this.list = list
        onItemClickListener = listener
        init()
    }
}