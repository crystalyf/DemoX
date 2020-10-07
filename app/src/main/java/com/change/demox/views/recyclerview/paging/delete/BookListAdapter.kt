/*
 * BookListAdapter.kt
 *
 * Created by xingjunchao on 2019/12/27.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.recyclerview.paging.delete

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.databinding.ItemMemberManageListBinding
import com.change.demox.views.recyclerview.paging.delete.bean.Book

/**
 * PagingDeleteFragment用的Adapter
 *
 * @property viewModel PagingDeleteViewModel
 * @property context 環境
 */
class BookListAdapter(private val viewModel: PagingDeleteViewModel?, private val context: Context) :
        PagedListAdapter<Book, BookListAdapter.ItemViewHolder>(
                MemberListDiffCallbackMember()
        ) {

    companion object {
        //チェックボックスの選択状態
        var checkStates = HashMap<Int, Boolean>()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(viewModel!!, item, position)
    }

    /**
     * 選択Mapをクリア
     *
     */
    fun clearCheckStates() {
        if (!checkStates.isNullOrEmpty()) {
            checkStates.clear()
        }
        notifyDataSetChanged()
    }

    fun getCheckStates(): HashMap<Int, Boolean> {
        return checkStates
    }

    /**
     * アイテムのViewHolder
     *
     * @property binding　ItemTopicListBinding
     */
    class ItemViewHolder private constructor(val binding: ItemMemberManageListBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(
                viewModel: PagingDeleteViewModel,
                item: Book?,
                position: Int
        ) {
            binding.memberName = item?.model_name
            binding.checkboxMemberManage.tag = position
            if (!checkStates.containsKey(position)) {
                checkStates[position] = false
            }
            binding.checkboxMemberManage.isChecked = checkStates[position]!!
            binding.checkboxMemberManage.setOnCheckedChangeListener { boxView: CompoundButton, isCheck: Boolean ->
                val pos = boxView.tag.toString().toInt()
                if (isCheck) {
                    checkStates[pos] = true
                } else {
                    checkStates.remove(pos)
                    checkStates[pos] = false
                }
                adaptItemSelect(viewModel, item)
            }
            binding.root.setOnClickListener {
                binding.checkboxMemberManage.isChecked = !binding.checkboxMemberManage.isChecked
            }
            binding.executePendingBindings()
        }

        /**
         * アイテム選択ステータスの適応
         *
         * @param viewModel メンバー管理画面ViewModel
         * @param item リストアイテム
         */
        private fun adaptItemSelect(
                viewModel: PagingDeleteViewModel,
                item: Book?
        ) {
            item?.isChecked = !item?.isChecked!!
            viewModel.refreshDeleteButtonState(item.book_id.toString(), checkStates)
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMemberManageListBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }
}


class MemberListDiffCallbackMember : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(
            oldItem: Book,
            newItem: Book
    ): Boolean {
        return oldItem.book_id == newItem.book_id
    }

    override fun areContentsTheSame(
            oldItem: Book,
            newItem: Book
    ): Boolean {
        return oldItem.isSameWith(newItem)
    }
}
