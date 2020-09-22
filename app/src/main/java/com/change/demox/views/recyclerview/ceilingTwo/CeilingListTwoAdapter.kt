package com.change.demox.views.recyclerview.ceilingTwo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.R
import com.change.demox.databinding.ItemWirelessListBinding
import com.change.demox.views.recyclerview.ceiling.RecyclerCeilingViewModel
import com.change.demox.views.recyclerview.ceiling.bean.CeilingItemModel


/**
 * 悬浮吸顶效果的adapter
 *
 * @property viewModel ブック_サムネイル  ViewModel
 * @property list phoneのbook一覧list (from HTTP)
 */
class CeilingListTwoAdapter(
        private var viewModel: RecyclerCeilingTwoViewModel?,
        private var list: MutableList<CeilingItemModel>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageLayoutHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_wireless_list,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageLayoutHolder) {
            holder.bind(viewModel, position, list!!)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setDataList() {
        this.list = viewModel?.wirelessListAfter
        notifyDataSetChanged()
    }

    class ImageLayoutHolder(private val binding: ItemWirelessListBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(
                viewModel: RecyclerCeilingTwoViewModel?,
                position: Int,
                list: MutableList<CeilingItemModel>  //转换后的model
        ) {
            binding.contactName.text = list[position].name
            binding.contactNumber.text = list[position].num
//            binding.linearlayoutFigThumbRoot.setOnClickListener {
//                viewModel?.toFigIllustration(position)
//            }
            binding.executePendingBindings()
        }
    }
}
