/*
 * GroupJoinFragment.kt
 *
 * Created by xingjunchao on 2019/12/24.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.bottomsheet.infragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.change.demox.R
import com.change.demox.views.bottomsheet.KotlinRecycleAdapter
import kotlinx.android.synthetic.main.activity_bottom_sheet.*

/**
 * BottomSheet
 *
 */
class BottomSheetFragment : Fragment() {

    private var list: List<String>? = null

    private fun initData() {
        list = ArrayList()
        for (i in 1..10) {
            (list as ArrayList<String>).add("我是条目$i")
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, null);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        initData()
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_machine_part.layoutManager = layoutManager
        rv_machine_part.itemAnimator = DefaultItemAnimator()
        rv_machine_part.addItemDecoration(
                DividerItemDecoration(
                        activity,
                        DividerItemDecoration.VERTICAL
                )
        )
        val adapter = KotlinRecycleAdapter(activity?.applicationContext!!, list)
        rv_machine_part.adapter = adapter
    }
}
