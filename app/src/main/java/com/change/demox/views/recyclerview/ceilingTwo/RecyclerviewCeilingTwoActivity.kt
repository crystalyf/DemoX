package com.change.demox.views.recyclerview.ceilingTwo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.change.demox.R
import com.change.demox.databinding.ActivityRecyclerCeilingBinding
import com.change.demox.views.recyclerview.adapter.CeilingListAdapter
import com.change.demox.views.recyclerview.ceiling.RecyclerCeilingViewModel

class RecyclerviewCeilingTwoActivity : AppCompatActivity() {

    private lateinit var viewModel: RecyclerCeilingTwoViewModel
    var listAdapter: CeilingListTwoAdapter? = null
    private var dataBinding: ActivityRecyclerCeilingBinding? = null
    private var headDecpration: StickySectionDecoration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_ceiling)
        viewModel = ViewModelProviders.of(this).get(RecyclerCeilingTwoViewModel::class.java)
        initView()
    }

    private fun initView() {
        viewModel.getData()
        headDecpration = StickySectionDecoration(this)
        if (dataBinding?.recyclerviewList?.itemDecorationCount == 0) {
            dataBinding?.recyclerviewList?.addItemDecoration(headDecpration!!)
        }
        setUpAdapter()
        viewModel.wirelessList.observe(this, Observer {
            if (it != null) {
                viewModel.transform()
                headDecpration?.setCitiList(viewModel.wirelessListAfter)
                listAdapter?.setDataList()
            }
        })
    }

    private fun setUpAdapter() {
        if (listAdapter == null) {
            listAdapter = CeilingListTwoAdapter(viewModel, viewModel.wirelessListAfter)
        }
        dataBinding?.recyclerviewList?.adapter = listAdapter
    }

}