package com.change.demox.views.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.R

class KotlinRecycleAdapter(mContext: Context, private var list: List<String>?) :
        RecyclerView.Adapter<KotlinRecycleAdapter.MyHolder>() {
    private var context: Context? = mContext
    private var itemClickListener: IKotlinItemClickListener? = null
    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_multi_column_list, parent, false)
        return MyHolder(view)
    }

    public fun setList(list: ArrayList<String>?) {
        this.list = list
        notifyDataSetChanged()
    }

    public fun setSelectedPosition(position: Int) {
        currentPosition = position
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list?.size!!

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.itemView.setOnClickListener {
        }

    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var parent: RelativeLayout = itemView.findViewById(R.id.item_parent)
    }

    fun setOnKotlinItemClickListener(itemClickListener: IKotlinItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    interface IKotlinItemClickListener {
        fun onItemClickListener(position: Int)
    }

}