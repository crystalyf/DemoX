/*
 * MemberListByReadAtAdapter.kt
 *
 * Created by xingjunchao on 2020/01/10.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.flexboxlayout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.change.demox.databinding.ItemMemberListByReadAtBinding


/**
 * メンバー一覧(連絡詳細) 用 Adapterクラス
 *
 * @property userId ユーザーId
 * @property list データソース
 */
class MemberListByReadAtAdapter(
        private var viewModel: TopicDetailViewModel?,
        private var list: MutableList<MemberReactionModel>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun setDataList() {
        this.list = viewModel?.memberListByReadAt?.value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val member = list?.get(position)
        if (holder is ItemViewHolder) {
            holder.bind(
                    member!!
            )
        }
    }

    /**
     * アイテムのViewHolder
     *
     * @property binding　ItemGroupListBinding
     */
    class ItemViewHolder private constructor(val binding: ItemMemberListByReadAtBinding) :
            RecyclerView.ViewHolder(binding.root) {

        /**
         * 0:管理者
         */
        val ROLE_MANAGER = 0

        /**
         * 1: 連絡係
         */
        val ROLE_SENDER = 1

        /**
         * 2:一般
         */
        val ROLE_OTHER = 2

        /**
         * 読む
         */
        val READ_FINISH = 1

        /**
         * 未読
         */
        val READ_UN_FINISH = 0

        /**
         * 回答済み
         */
        val ANSWER_FINISH = 1

        /**
         * 未回答
         */
        val ANSWER_UN_FINISH = 0

        fun bind(
                item: MemberReactionModel
        ) {
            binding.iconUrl = item.member.picture
            binding.memberName = item.member.name
            binding.textviewMemberManager.visibility =
                    if (item.member.role == ROLE_MANAGER) View.VISIBLE else View.GONE
            binding.textviewMemberContacter.visibility =
                    if (item.member.role == ROLE_SENDER) View.VISIBLE else View.GONE
            binding.textviewMemberSelf.visibility = View.VISIBLE
            binding.textviewMemberUnread.visibility
            binding.textviewMemberUnanswer.visibility = View.VISIBLE
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMemberListByReadAtBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }
}
