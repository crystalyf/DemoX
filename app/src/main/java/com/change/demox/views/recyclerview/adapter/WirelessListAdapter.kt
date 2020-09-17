package com.change.demox.views.recyclerview.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.R
import com.change.demox.databinding.ItemWirelessListBinding
import com.change.demox.databinding.ItemWirelessTitleBinding
import com.change.demox.views.recyclerview.muti.RecyclerCategoryViewModel
import com.change.demox.views.recyclerview.muti.bean.WirelessTransformModel


/**
 * phoneのbook一覧を表示する
 *
 * @property viewModel ブック_サムネイル  ViewModel
 * @property list phoneのbook一覧list (from HTTP)
 */
class WirelessListAdapter(
        private var viewModel: RecyclerCategoryViewModel?,
        private var list: MutableList<WirelessTransformModel>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val RV_TYPE_LIST = 1
        const val RV_TYPE_CATEGORY = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == RV_TYPE_CATEGORY)
            CategoryHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            R.layout.item_wireless_title,
                            parent,
                            false
                    )
            )
        else {
            ImageLayoutHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.context),
                            R.layout.item_wireless_list,
                            parent,
                            false
                    )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (TextUtils.isEmpty(list?.get(position)?.indexTitle)) {
            RV_TYPE_LIST
        } else {
            RV_TYPE_CATEGORY
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageLayoutHolder) {
            holder.bind(viewModel, position, list!!)
        } else if (holder is CategoryHolder) {
            holder.bind(position, list)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setDataList() {
        this.list = viewModel?.wirelessTransformList
        notifyDataSetChanged()
    }

    class ImageLayoutHolder(private val binding: ItemWirelessListBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(
                viewModel: RecyclerCategoryViewModel?,
                position: Int,
                list: MutableList<WirelessTransformModel>  //转换后的model
        ) {
            binding.contactName.text = list[position].data?.name
            binding.contactNumber.text = list[position].data?.num
//            binding.linearlayoutFigThumbRoot.setOnClickListener {
//                viewModel?.toFigIllustration(position)
//            }
            binding.executePendingBindings()
        }
    }

    class CategoryHolder(private val binding: ItemWirelessTitleBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(
                position: Int,
                list: MutableList<WirelessTransformModel>?    //转换后的model
        ) {
            binding.titleName.text = list?.get(position)?.indexTitle
            binding.executePendingBindings()
        }
    }
}
