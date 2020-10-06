package com.change.demox.views.recyclerview.paging.onlyshow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.change.demox.databinding.FragmentPagingBinding
import com.change.demox.extension.getViewModelFactory
import timber.log.Timber

class PagingFragment : Fragment() {

    private lateinit var viewModel: PagingViewModel

    private var viewDataBinding: FragmentPagingBinding? = null

    private lateinit var listAdapter: HomeListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentPagingBinding.inflate(inflater, container, false).apply {
            viewModel = viewModels<PagingViewModel> { activity?.getViewModelFactory()!! }.value
        }
        return viewDataBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding!!.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDataBinding!!.homeList.adapter = null
        viewDataBinding = null
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding!!.viewModel
        if (viewModel != null) {
            listAdapter = HomeListAdapter(viewModel)
            viewDataBinding!!.homeList.adapter = listAdapter
            //在Activity中对LiveData进行订阅
            viewModel.datas.observe(viewLifecycleOwner, Observer {
                // 每当数据更新，计算新旧数据集的差异，对列表进行更新
                //内部代码：mDiffer.submitList(pagedList);
                listAdapter.submitList(it)
            })

            viewModel.reloadEvent.observe(viewLifecycleOwner, Observer {
                listAdapter.notifyItemChanged(it.peekContent())
            })
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }
}