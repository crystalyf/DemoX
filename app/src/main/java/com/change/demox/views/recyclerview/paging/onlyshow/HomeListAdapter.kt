package com.change.demox.views.recyclerview.paging.onlyshow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.databinding.ItemHomeListBinding
import com.change.demox.views.recyclerview.paging.onlyshow.bean.SampleModel

/**
 * 这里父类我们使用到了PagedListAdapter，它是官方基于RecyclerView.Adapter的AsyncPagedListDiffer封装类，
 * 其内创建了AsyncPagedListDiffer的示例，以便在后台线程中使用DiffUtil计算新旧数据集的差异，从而节省Item更新的性能。
 *
 */
class HomeListAdapter(private val viewModel: PagingViewModel) :
        PagedListAdapter<SampleModel, HomeListAdapter.ViewHolder>(
                DataDiffCallback()
        ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel, item)
    }

    class ViewHolder private constructor(val binding: ItemHomeListBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: PagingViewModel, item: SampleModel?) {
            binding.viewmodel = viewModel
            binding.itemData = item ?: SampleModel("N/A", false)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemHomeListBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

/**
 * 此外，我们还需要在ListAdapter中声明DiffUtil.ItemCallback，对数据集的差异计算的逻辑进行补充
 *
 */
class DataDiffCallback : DiffUtil.ItemCallback<SampleModel>() {
    override fun areItemsTheSame(
            oldItem: SampleModel,
            newItem: SampleModel
    ): Boolean {
        return oldItem.first == newItem.first
    }

    override fun areContentsTheSame(
            oldItem: SampleModel,
            newItem: SampleModel
    ): Boolean {
        return oldItem == newItem
    }
}