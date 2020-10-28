/*
 * FigFragment.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.change.base.BaseFragment
import com.change.base.BaseLazyFragment
import com.change.demox.R
import com.change.demox.databinding.FragmentFigBinding
import com.change.demox.extension.getViewModelFactory
import com.change.demox.views.recyclerview.figillustration.adapter.FigViewPagerAdapter
import com.change.demox.views.recyclerview.figillustration.bean.Illustration
import com.change.demox.views.recyclerview.figillustration.bean.ThumbnailModel


/**
 * Fig Fragment
 */
class FigFragment : BaseLazyFragment<FragmentFigBinding>(), ICurrentSelectedData {

    companion object {
        const val ILLUSTRATION_KEY = "illustrationListKey"

        /**
         * FigFragmentを作成
         * @return
         */
        fun newInstance(
                thumbnailModel: ThumbnailModel?,
                isFromLocal: Boolean,
                selectedPartsId: Int? = null,
                tag: String
        ): FigFragment {
            val figFragment = FigFragment()
            figFragment.isFromLocal = isFromLocal
            figFragment.figTag = tag
            if (selectedPartsId != null) {
                figFragment.selectedPartId = mutableListOf(selectedPartsId)
            }
            val bundle = Bundle()
            //Fragment追加パラメーター
            bundle.putParcelableArrayList(
                    ILLUSTRATION_KEY,
                    thumbnailModel?.illustrations as ArrayList
            )
            bundle.putParcelable("model", thumbnailModel)
            figFragment.arguments = bundle
            return figFragment
        }
    }

    /**
     * Figの画像データソース
     */
    private var illustrationsList: MutableList<Illustration> = mutableListOf()

    /**
     * このフィグ中で、選択したポイントのIdのリスト
     */
    var selectedPartId: MutableList<Int>? = mutableListOf()
    private var figViewPagerAdapter: FigViewPagerAdapter? = null
    lateinit var bookFigViewModel: FigViewModel
    var isFromLocal = false
    private fun figViewModel(): FigViewModel = bookFigViewModel


    private var currentFigName: String? = null
    private var currentFigId: Int = 0
    private var currentFigModel: ThumbnailModel? = null
    var figTag = ""
    override fun layoutId(): Int = R.layout.fragment_fig

    override fun afterView() {
        bookFigViewModel = activity?.viewModels<FigViewModel> { getViewModelFactory() }?.value!!
        if (selectedPartId.isNullOrEmpty()) {
            figViewModel().selectedPartsIdMap[currentFigId]?.forEach {
                selectedPartId?.add(it)
            }
        }
        initView()
        figViewModel().currentIllustration = illustrationsList
        figViewModel().getPartListData(
                currentFigModel,
                figViewModel().currentFigModelName,
                illustrationsList
        )
    }

    override fun onDestroyView() {
        dataBinding?.viewPagerChild?.adapter = null
        super.onDestroyView()
    }

    override fun getSelectedPartsIds(ids: MutableList<Int>) {
        selectedPartId?.clear()
        ids.forEach {
            selectedPartId?.add(it)
        }
    }

    /**
     * 今表示のillustrationId
     *
     */
    fun getCurrentIllustrationId(): Int {
        if (illustrationsList.size == 0) {
            return 0
        }
        return illustrationsList.let {
            it[dataBinding?.viewPagerChild?.currentItem ?: 0].illustration_id
        }
    }
    var hasUpdatePartList = false
    private fun initView() {
        arguments?.let {
            illustrationsList =
                    it.getParcelableArrayList<Illustration>(ILLUSTRATION_KEY)?.toMutableList()
                            ?: mutableListOf()
            currentFigModel = it.getParcelable("model")
            currentFigId = currentFigModel?.figId ?: 0
            currentFigName = currentFigModel?.figName
        }
        figViewModel().pref = preferences
        initIllustrationFragmentViewPager()
        figViewModel().illustrationUiInit.observe(viewLifecycleOwner, Observer {
            if (it == currentFigId || (!hasUpdatePartList)) {
                hasUpdatePartList = true
                updatePartList()
            }
        })
    }

    /**
     * イラスト画像が表示のfragmentを初期化
     *
     */
    private fun initIllustrationFragmentViewPager() {
        figViewPagerAdapter = FigViewPagerAdapter(
                childFragmentManager, currentFigId,
                illustrationsList,
                isFromLocal,
                this,
                resources.configuration.orientation
        )
        dataBinding?.viewPagerChild?.adapter = figViewPagerAdapter
        dataBinding?.viewPagerChild?.addOnPageChangeListener(object :
                ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                (figViewPagerAdapter!!.getItem(position) as IllustrationFragment).resetSelectedPoint(
                        selectedPartId ?: return
                )
                (figViewPagerAdapter!!.getItem(position) as IllustrationFragment).resetHighLightPoint()
                if ((parentFragment != null && parentFragment is FigIllustrationFragment)) {
                    (parentFragment as FigIllustrationFragment).scrollToTop()
                    (parentFragment as FigIllustrationFragment).updateBottomSheetList()
                }
                figViewModel().illustrationIndexMap[currentFigId] = position
            }
        })

        dataBinding?.viewPagerChild?.currentItem =
                figViewModel().illustrationIndexMap[currentFigId] ?: 0
    }

    /**
     * イラスト画像のポイントを更新する
     *
     */
    private fun updatePartList() {
        var referenceNumber = ""
        for (index in 0 until (figViewPagerAdapter?.count ?: 1)) {
            val illustrationFragment = figViewPagerAdapter?.getItem(index) as IllustrationFragment
            val partList =
                    figViewModel().getCurrentIllustrationPartList(illustrationsList[index].illustration_id, currentFigId)
            illustrationFragment.setParts(partList)
            if (currentFigId == figViewModel().selectedFigId && partList?.firstOrNull { it.part_id == figViewModel().selectedPartId } != null) {
                referenceNumber =
                        partList.first { it.part_id == figViewModel().selectedPartId }.reference_number1
                                ?: ""
            }
        }
        if (currentFigId == figViewModel().selectedFigId) {
            ////ブック内検索_結果 から遷移の場合、bottomsheetを表示する
            figViewModel().selectedFigId = null
            figViewModel().selectedPartId = null
            Handler().postDelayed({
                figViewModel().showBottomSheetPart(referenceNumber)
            }, 100)
        }
    }

    fun getParent(): FigIllustrationFragment {
        return parentFragment as FigIllustrationFragment
    }

    override fun loadData() {
      //
    }
}

/**
 * 現在選択したのポイントのidのリストを取る
 *
 */
interface ICurrentSelectedData {
    fun getSelectedPartsIds(ids: MutableList<Int>)
}
