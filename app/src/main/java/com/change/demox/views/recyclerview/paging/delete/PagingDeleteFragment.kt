package com.change.demox.views.recyclerview.paging.delete

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.change.demox.databinding.FragmentPagingDeleteBinding
import com.change.demox.extension.getViewModelFactory
import com.change.demox.views.widget.CommonDialog
import timber.log.Timber

class PagingDeleteFragment : Fragment() {

    private var viewModel: PagingDeleteViewModel? = null

    private var viewDataBinding: FragmentPagingDeleteBinding? = null

    private lateinit var listAdapter: BookListAdapter

    /**
     * 限制删除的数量
     */
    val ALERT_LIMIT_COUNT = 100

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentPagingDeleteBinding.inflate(inflater, container, false).apply {
            viewModel = viewModels<PagingDeleteViewModel> { activity?.getViewModelFactory()!! }.value
        }
        return viewDataBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding!!.lifecycleOwner = this.viewLifecycleOwner
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDataBinding!!.homeList.adapter = null
        viewDataBinding = null
    }

    private fun initView() {
        setupListAdapter()
        viewDataBinding?.buttonDelete?.setOnClickListener {
            val deleteIdList = mutableListOf<String>()
            listAdapter.getCheckStates().forEach {
                if (it.value) {
                    deleteIdList.add(viewModel?.books?.value?.get(it.key)?.book_id.toString())
                }
            }
            if (deleteIdList.size < ALERT_LIMIT_COUNT) {
                showCanDeleteDialog(deleteIdList)
            }

        }
        viewDataBinding?.swipeRefresh?.setOnRefreshListener {
            viewDataBinding?.swipeRefresh?.isRefreshing = false
            listAdapter.clearCheckStates()
            viewModel?.refresh()
        }
    }

    private fun setupListAdapter() {
        viewModel = viewDataBinding?.viewModel
        if (viewModel != null) {
            listAdapter = BookListAdapter(viewModel, activity?.applicationContext!!)
            viewDataBinding!!.homeList.adapter = listAdapter
            viewModel?.getDataList()
            //在Activity中对LiveData进行订阅
            viewModel?.books?.observe(viewLifecycleOwner, Observer {
                if (listAdapter.currentList?.size ?: 0 > 0 && it.size == 0) {
                    return@Observer
                }
                listAdapter.submitList(it)
                //删除按钮状态
                viewModel?.refreshDeleteButtonState()
            })
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun showCanDeleteDialog(deleteIdList: MutableList<String>) {
        fragmentManager?.let { manager ->
            CommonDialog<CommonDialog<*>>()
                    .title("删除")
                    .message("确定删除吗")
                    .positiveTitle("OK")
                    .onPositiveListener(DialogInterface.OnClickListener { _, _ ->
                        //发送删除协议，并且在成功之后执行viewModel.refresh(),刷新获取get数据
                    })
                    .negativeTitle("cancel")
                    .show(manager)
        }
    }
}