package com.change.demox.views.recyclerview.muti

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.change.demox.R
import com.change.demox.databinding.ActivityRecyclerCateBinding
import com.change.demox.views.recyclerview.adapter.WirelessListAdapter

class RecyclerviewCategoryActivity : AppCompatActivity() {

    private lateinit var viewModel: RecyclerCategoryViewModel
    var listListAdapter: WirelessListAdapter? = null
    private var dataBinding : ActivityRecyclerCateBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_cate)
        viewModel = ViewModelProviders.of(this).get(RecyclerCategoryViewModel::class.java)
        initView()
    }

   private fun initView(){
       viewModel.getData()
       setUpAdapter()
       viewModel.wirelessList.observe(this, Observer {
           viewModel.transform()
           listListAdapter?.setDataList()
       })
    }

    private fun setUpAdapter() {
        if (listListAdapter == null) {
            listListAdapter = WirelessListAdapter(viewModel, viewModel.wirelessTransformList)
        }
        dataBinding?.recyclerviewList?.adapter = listListAdapter
    }

}