/*
 * MemberListByReadAtFragment.kt
 *
 * Created by xingjunchao on 2020/01/10.
 * Copyright © 2019年 Eole. All rights reserved.
 */
package com.change.demox.views.flexboxlayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.change.demox.databinding.FragmentMemberListByReadAtBinding
import com.change.demox.extension.getViewModelFactory
import com.change.demox.views.flexboxlayout.component.Ra9BaseFragment


class MemberListByReadAtFragment : Ra9BaseFragment() {

    private lateinit var memberListByReadAtViewModel: TopicDetailViewModel
    private var viewDataBinding: FragmentMemberListByReadAtBinding? = null
    var listAdapter: MemberListByReadAtAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberListByReadAtViewModel =
                activity?.viewModels<TopicDetailViewModel> { getViewModelFactory() }!!.value
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
                FragmentMemberListByReadAtBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        initView()
        return viewDataBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding?.lifecycleOwner = this.viewLifecycleOwner
        setUpAdapter()
    }

    override fun onDestroyView() {
        viewDataBinding = null
        viewDataBinding?.recyclerViewMemberListByReadAt?.adapter = null
        super.onDestroyView()
    }

    private fun initView() {
        memberListByReadAtViewModel.getTopicDetail()
        memberListByReadAtViewModel.memberListByReadAt.observe(viewLifecycleOwner, Observer {
            listAdapter?.setDataList()
        })
    }

    private fun setUpAdapter() {
        listAdapter = MemberListByReadAtAdapter(memberListByReadAtViewModel, memberListByReadAtViewModel.memberListByReadAt.value)
        viewDataBinding?.recyclerViewMemberListByReadAt?.adapter = listAdapter
    }
}
