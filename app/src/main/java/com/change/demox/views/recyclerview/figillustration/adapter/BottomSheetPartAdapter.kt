/*
 * BottomSheetPartAdapter.kt
 *
 * Created by xingjunchao on 2020/06/23.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.change.demox.R
import com.change.demox.views.recyclerview.figillustration.FigViewModel
import com.change.demox.views.recyclerview.figillustration.bean.PartModel


/**
 * ブック_FIGリスト(partlist) Adapter
 *
 * @property list パーツ表データソース
 * @property viewModel Fig ViewModel
 * @param activity 環境
 */
class BottomSheetPartAdapter(
        activity: FragmentActivity?,
        var list: MutableList<PartModel>?,
        val viewModel: FigViewModel
) : RecyclerView.Adapter<ViewHolder>() {
    var context: Context = activity!!

    var currentFigId = 0

    var highLightPartsIds: List<Int> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MyHolder(
                View.inflate(
                        context,
                        R.layout.item_part_bottomsheet,
                        null
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is MyHolder) {
            holder.textViewReferenceNumber.text = list!![position].reference_number1
            holder.textViewPartName.text = list!![position].part_name
            holder.textViewPartNumber.text = list!![position].part_number
            holder.textViewNumber.text = list!![position].quantity.toString()
            holder.textViewSerialNumber.text = list!![position].serial_number
            if (list!![position].isSelected) {
                if (viewModel.pref?.isOrangeThemeColor!!) {
                    holder.itemRoot.background =
                            ContextCompat.getDrawable(context, R.color.colorBlue)
                } else {
                    holder.itemRoot.background =
                            ContextCompat.getDrawable(context, R.color.colorRed)
                }
            } else {
                holder.itemRoot.background =
                        ContextCompat.getDrawable(context, R.color.white)
            }
            holder.itemView.setOnClickListener {
                //bottomSheet itemクリック
                // viewModel.bottomSheetItemClick(list!![holder.adapterPosition])
            }
            val resultCartEnable = holder.isCartButtonEnable(list!![position].part_number)
            if (highLightPartsIds.contains(list!![position].part_id)) {
                //カートの状態ではあります
                holder.buttonCartAdd.visibility = View.GONE
            } else {
                //カートの状態ではありません
                if (resultCartEnable) {
                    holder.buttonCartAdd.visibility = View.VISIBLE
                } else {
                    holder.buttonCartAdd.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setBottomSheetList(list: MutableList<PartModel>?, figId: Int?, ids: List<Int>) {
        currentFigId = figId ?: 0
        this.list = list
        this.highLightPartsIds = ids
        notifyDataSetChanged()
    }

    internal inner class MyHolder(itemView: View) : ViewHolder(itemView) {
        val itemRoot: LinearLayout =
                itemView.findViewById(R.id.item_root)
        val textViewReferenceNumber: TextView =
                itemView.findViewById(R.id.textview_reference_number)
        val textViewPartName: TextView =
                itemView.findViewById(R.id.textview_partname)
        val textViewPartNumber: TextView =
                itemView.findViewById(R.id.textview_partnumber)
        val textViewSerialNumber: TextView =
                itemView.findViewById(R.id.textview_serial_number)
        val textViewNumber: TextView =
                itemView.findViewById(R.id.textview_number)
        val buttonCartAdd: AppCompatButton =
                itemView.findViewById(R.id.button_cart_add)

        /**
         * カートに追加できないように 非活性/活性
         *
         * @param partNumber 品番
         * @return 「カート追加ボタン」の活性状態 true: はい   false:ない
         */
        fun isCartButtonEnable(partNumber: String?): Boolean {
            val result = partNumber?.replace("-", "")?.trim()
            return !(result == "9999999999" || TextUtils.isEmpty(result))
        }
    }
}