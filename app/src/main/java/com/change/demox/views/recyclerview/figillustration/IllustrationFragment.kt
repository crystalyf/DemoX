/*
 * IllustrationFragment.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.change.base.BaseFragment
import com.change.demox.R
import com.change.demox.databinding.FragmentIllustrationBinding
import com.change.demox.extension.getViewModelFactory
import com.change.demox.views.recyclerview.figillustration.bean.Illustration
import com.change.demox.views.recyclerview.figillustration.bean.PartModel
import com.change.demox.views.recyclerview.figillustration.widget.ScalableImageView

/**
 * イラスト画像を表示のfragment
 *
 */
class IllustrationFragment : BaseFragment<FragmentIllustrationBinding>() {

    var isFromLocal = false

    private lateinit var bookViewModel: FigViewModel
    private fun viewModel(): FigViewModel = bookViewModel


    //figIdを表示
    private var figId: Int? = 0

    var illustrationTag = ""

    //画面が初期化されているかどうか  true: はい   false:ない
    private var isHasCreated = false

    var illustration: Illustration? = null

    //Figイラスト url
    private var imageUrl = ""

    private var mGetSelectedData: ICurrentSelectedData? = null

    override fun afterView() {
        arguments?.let {
            figId = requireArguments().getInt(FIG_ID)
            illustration = requireArguments().getParcelable(ILLUSTRATION_KEY)
        }
        bookViewModel = activity?.viewModels<FigViewModel> { getViewModelFactory() }?.value!!
        isHasCreated = true
        viewModel().pref = preferences
        initView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        dataBinding?.imageView?.restoreImageViewParams()
    }

    override fun onStop() {
        super.onStop()
        dataBinding?.imageView?.restoreImageViewParams()
    }

    /**
     * 現在選択されているポイントをリセットします
     *
     * @param ids パーツのIdリスト
     */
    fun resetSelectedPoint(ids: MutableList<Int>) {
        dataBinding?.imageView?.setSelectedDataIds(ids)
    }

    /**
     * 現在Height Pointされているポイントをリセットします
     *
     */
    fun resetHighLightPoint() {
        dataBinding?.imageView?.setHighLightDataIds(
            viewModel().getHighLightPartsIds(figId ?: 0)?.toMutableList() ?: mutableListOf()
        )
    }

    override fun layoutId() = R.layout.fragment_illustration

    private fun hideProgress() {
        dataBinding?.layoutProgress?.visibility = View.GONE
    }

    private fun showProgress() {
        dataBinding?.layoutProgress?.visibility = View.VISIBLE
    }

    private fun initView() {
        showProgress()
        dataBinding?.imageView?.onDeviceClicked = { partIdList, referenceNumber ->
            //bottomSheetを表示
            viewModel().showBottomSheetPart(referenceNumber)
            if (partIdList != null) {
                mGetSelectedData?.getSelectedPartsIds(partIdList)
                viewModel().setSelectedPartsId(figId ?: 0, partIdList)
            }
        }
        imageUrl = viewModel().getIllustrationImageUrl(illustration!!)
        //カートの現在のFigイラストを確認する、次に、Figイラストにマークを付けます
        viewModel().setHighlight.observe(this, Observer {
            dataBinding?.imageView?.setHighLightDataIds(it.toMutableList())
        })
        dataBinding?.imageView?.setProgress(object : ScalableImageView.ProgressListener {
            override fun loadFinish() {
                hideProgress()
                dataBinding?.imageView?.visibility = View.VISIBLE
            }
        })
        dataBinding?.imageView?.setDataSource(
            imageUrl,
            illustration!!.illustration_id,
            true,
            viewModel().isFromLocal,
            viewModel().bookId,
            viewModel()
        )

        val parts = viewModel().getCurrentIllustrationPartList(
            illustration?.illustration_id ?: 0,
            figId ?: 0
        )
        setParts(parts)
    }

    /**
     * パーツのデータがイラストに設置
     *
     * @param partList
     */
    fun setParts(partList: MutableList<PartModel>?) {
        if (partList == null) {
            return
        }
        dataBinding?.imageView?.setParts(
            partList,
            viewModel().getSelectedPartsIds(figId ?: 0) ?: listOf(),
            viewModel().getHighLightPartsIds(figId ?: 0) ?: listOf()
        )
    }

    companion object {
        const val FIG_ID = "figId"
        const val ILLUSTRATION_KEY = "IllustrationsKey"

        /**
         * IllustrationFragmentを作成
         *
         * @param figId イラストのフィグId
         * @param illustration イラストの画像データ
         * @param getSelectedData　ICurrentSelectedData
         * @return
         */
        fun newInstance(
                figId: Int,
                illustration: Illustration?,
                isFromLocal: Boolean,
                getSelectedData: ICurrentSelectedData,
                tag: String
        ): IllustrationFragment {
            val illustrationFragment = IllustrationFragment()
            illustrationFragment.isFromLocal = isFromLocal
            val bundle = Bundle()
            bundle.putInt(FIG_ID, figId)
            bundle.putParcelable(ILLUSTRATION_KEY, illustration)
            illustrationFragment.arguments = bundle
            illustrationFragment.mGetSelectedData = getSelectedData
            illustrationFragment.illustrationTag = tag
            return illustrationFragment
        }
    }
}

