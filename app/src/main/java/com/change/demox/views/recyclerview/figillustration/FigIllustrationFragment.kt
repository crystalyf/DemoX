/*
 * FigIllustrationFragment.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration

import android.content.res.Configuration
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.viewpager.widget.ViewPager
import com.change.base.BaseFragment
import com.change.demox.R
import com.change.demox.databinding.FragmentFigIllustrationBinding
import com.change.demox.extension.getViewModelFactory
import com.change.demox.utils.EventObserver
import com.change.demox.views.recyclerview.figillustration.adapter.BottomSheetPartAdapter
import com.change.demox.views.recyclerview.figillustration.adapter.FigIllustrationViewPagerAdapter
import com.change.demox.views.recyclerview.figillustration.bean.PartModel
import com.change.demox.views.recyclerview.figillustration.bean.ThumbnailModel
import com.change.demox.views.recyclerview.figillustration.widget.DragLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * FigIllustration是从项目中简单删减抽离出来的功能模块
 *
 * 一个Fig(零件)有多个部件（Illustration）,所以在一个Fig中会有多个部件图，故用FigFragment装载每个零件，
 * Illustration装载每一个部件。若零件是只有一个部件，那么左右滑动屏幕会切换到下一个零件（FigFragment）。若
 * 是有多个部件，那么左右滑动屏幕则切换到下一个IllustrationFragment
 *
 */
class FigIllustrationFragment : BaseFragment<FragmentFigIllustrationBinding>() {

    companion object {
        const val SELECT_PART_ID = "selectedPartId"
        const val SELECT_FIG_ID = "selectedFigId"
    }

    //数据源string
    var dataStr = "[{\"booklet\":\"ARM323\",\"featureName\":\"\",\"figComment\":\"\",\"figId\":815901,\"figLinkUrl\":{\"url_type\":0,\"urls\":[]},\"figName\":\"駆動ギヤケース（左）\",\"figNumber\":\"P00100\",\"illustrations\":[{\"high_quality_image_download_link\":\"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2462146637,4274174245&fm=26&gp=0.jpg\",\"illustration_id\":164993,\"low_quality_image_download_link\":\"https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2462146637,4274174245&fm=26&gp=0.jpg\"}],\"isFavorite\":false,\"updatedAt\":\"2019-06-07\"},{\"booklet\":\"ARM323\",\"featureName\":\"\",\"figComment\":\"\",\"figId\":815902,\"figLinkUrl\":{\"url_type\":0,\"urls\":[]},\"figName\":\"駆動ギヤケース（中）\",\"figNumber\":\"P00200\",\"illustrations\":[{\"high_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603363525086&di=03a34dc1b955c87aafab4a6bdbedde47&imgtype=0&src=http%3A%2F%2Fi1.sinaimg.cn%2FIT%2F2010%2F0419%2F201041993511.jpg\",\"illustration_id\":166735,\"low_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603363525086&di=03a34dc1b955c87aafab4a6bdbedde47&imgtype=0&src=http%3A%2F%2Fi1.sinaimg.cn%2FIT%2F2010%2F0419%2F201041993511.jpg\"}],\"isFavorite\":false,\"updatedAt\":\"2019-06-07\"},{\"booklet\":\"ARM323\",\"featureName\":\"\",\"figComment\":\"\",\"figId\":815903,\"figLinkUrl\":{\"url_type\":0,\"urls\":[]},\"figName\":\"チェーンケース（左）\",\"figNumber\":\"P00300\",\"illustrations\":[{\"high_quality_image_download_link\":\"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1546500353,2204894501&fm=26&gp=0.jpg\",\"illustration_id\":166739,\"low_quality_image_download_link\":\"https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1546500353,2204894501&fm=26&gp=0.jpg\"}],\"isFavorite\":false,\"updatedAt\":\"2019-06-07\"},{\"booklet\":\"ARM323\",\"featureName\":\"\",\"figComment\":\"\",\"figId\":815904,\"figLinkUrl\":{\"url_type\":0,\"urls\":[]},\"figName\":\"チェーンケース（中）\",\"figNumber\":\"P00400\",\"illustrations\":[{\"high_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603363525083&di=efde694d311122ab527e117fec04e032&imgtype=0&src=http%3A%2F%2Fbig5.wallcoo.com%2Fflower%2Fsky_Flowers_01_1600x1200%2Fimages%2FBlue_Sky_Flowers_HM055_350A.jpg\",\"illustration_id\":166736,\"low_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603363525083&di=efde694d311122ab527e117fec04e032&imgtype=0&src=http%3A%2F%2Fbig5.wallcoo.com%2Fflower%2Fsky_Flowers_01_1600x1200%2Fimages%2FBlue_Sky_Flowers_HM055_350A.jpg\"}],\"isFavorite\":false,\"updatedAt\":\"2019-06-07\"},{\"booklet\":\"ARM323\",\"featureName\":\"\",\"figComment\":\"\",\"figId\":815905,\"figLinkUrl\":{\"url_type\":0,\"urls\":[]},\"figName\":\"チェーン・テンション（左）\",\"figNumber\":\"P00600\",\"illustrations\":[{\"high_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603363525082&di=c21e61f0aa9313ea18100cf87ab9a4ca&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F02%2F38%2F01300000237560123245382609951.jpg\",\"illustration_id\":164994,\"low_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603363525082&di=c21e61f0aa9313ea18100cf87ab9a4ca&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F02%2F38%2F01300000237560123245382609951.jpg\"}],\"isFavorite\":false,\"updatedAt\":\"2019-06-07\"},{\"booklet\":\"ARM323\",\"featureName\":\"\",\"figComment\":\"\",\"figId\":815906,\"figLinkUrl\":{\"url_type\":0,\"urls\":[]},\"figName\":\"チェーン・テンション（中）\",\"figNumber\":\"P00700\",\"illustrations\":[{\"high_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603363525081&di=f97de59dbff25527f2805cdf848d4abf&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa044ad345982b2b7dabd6f0633adcbef77099bd5.jpg\",\"illustration_id\":166737,\"low_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603363525081&di=f97de59dbff25527f2805cdf848d4abf&imgtype=0&src=http%3A%2F%2Fc.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2Fa044ad345982b2b7dabd6f0633adcbef77099bd5.jpg\"}],\"isFavorite\":false,\"updatedAt\":\"2019-06-07\"},{\"booklet\":\"ARM323\",\"featureName\":\"\",\"figComment\":\"\",\"figId\":815907,\"figLinkUrl\":{\"url_type\":0,\"urls\":[]},\"figName\":\"デバイダ・標準付属品\",\"figNumber\":\"P00800\",\"illustrations\":[{\"high_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603364597668&di=462e869f33233b1991f8c2239a0586bd&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F31%2F91%2F01300000291092125765914006238.jpg\",\"illustration_id\":166738,\"low_quality_image_download_link\":\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1603364597668&di=462e869f33233b1991f8c2239a0586bd&imgtype=0&src=http%3A%2F%2Fa4.att.hudong.com%2F31%2F91%2F01300000291092125765914006238.jpg\"}],\"isFavorite\":false,\"updatedAt\":\"2019-06-07\"}]"

    //数据源
    var figIllustrationList: MutableList<ThumbnailModel> = mutableListOf()

    private lateinit var bookFigViewModel: FigViewModel
    private fun figViewModel(): FigViewModel = bookFigViewModel


    //bottomSheet adapter
    private var bottomSheetPartAdapter: BottomSheetPartAdapter? = null

    //イラストの viewpager Adapter
    private var figViewPagerAdapter: FigIllustrationViewPagerAdapter? = null

    //ボトムシートの表示位置
    private var bottomSheetShowPosition: Int? = null

    private var bottomSheetState: Int = DragLayout.State.AT_BOTTOM

    private var reStorePosition: Int = 0

    private var layoutManager: LinearLayoutManager? = null

    private var smoothScroller: SmoothScroller? = null

    private var imageShowPosition = 0

    override fun layoutId() = R.layout.fragment_fig_illustration


    override fun afterView() {
        // メーニュを設定
        setHasOptionsMenu(true)
        initViewModel()
        // Figイラストのデータを取得
        getFigIllustrationList()
        initView()
        if (figViewModel().selectedPartId != null) {
            //ブック内検索_結果 から遷移の場合　せ
            figViewModel().selectedPartsIdMap[figViewModel().selectedFigId ?: 0] =
                    listOf(figViewModel().selectedPartId!!)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        bottomSheetState = dataBinding?.drawLayout?.currentState ?: DragLayout.State.AT_BOTTOM
        reStorePosition = layoutManager?.findFirstVisibleItemPosition() ?: 0
        super.onConfigurationChanged(newConfig)
        initViewModel()
        initView()
        layoutManager?.scrollToPositionWithOffset(reStorePosition, 0)
    }

    override fun onResume() {
        super.onResume()
        //最初に画面に入り、viewpagerはスライドできます
        dataBinding?.viewPager?.isScroll = true
    }

    override fun onDestroyView() {
        dataBinding?.viewPager?.adapter = null
        dataBinding?.recyclerViewPart?.adapter = null
        dataBinding?.drawLayout?.currentState = DragLayout.State.AT_BOTTOM
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * bottomSheetは最上部にスクロールする
     *
     */
    fun scrollToTop() {
        dataBinding?.recyclerViewPart?.stopScroll()
        layoutManager?.scrollToPosition(0)
    }

    /**
     * viewModelを初期化
     *
     */
    private fun initViewModel() {
        bookFigViewModel =
                activity?.viewModels<FigViewModel> { getViewModelFactory() }?.value!!
        dataBinding?.viewModel = figViewModel()
    }

    private fun initView() {
        //bottomSheetを復元
        dataBinding?.drawLayout?.currentState = bottomSheetState
        initFigFragmentViewPager()
        // bottomSheetの初期化
        initBottomSheetRecyclerView()
        initObservers()
    }

    /**
     * ViewPagerを初期化
     *
     */
    private fun initFigFragmentViewPager() {
        val idList = mutableListOf<Int?>()
        figIllustrationList.forEach {
            val partId = if (it.figId == arguments?.getInt(SELECT_FIG_ID)) {
                arguments?.getInt(SELECT_PART_ID)
            } else {
                null
            }
            idList.add(partId)
        }
        figViewPagerAdapter =
                FigIllustrationViewPagerAdapter(
                        childFragmentManager, figIllustrationList,
                        false,
                        idList,
                        resources.configuration.orientation
                )
        dataBinding?.viewPager?.adapter = figViewPagerAdapter
        dataBinding?.viewPager?.addOnPageChangeListener(object :
                ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val index = position - 1
                if (index >= 0 && index < figIllustrationList.size) {
                    val beforeModel = figIllustrationList[index]
                    figViewModel().illustrationIndexMap[beforeModel.figId ?: 0] =
                            beforeModel.illustrations?.size ?: 0 - 1
                }
                scrollToTop()
                updateBottomSheetList(position)
                imageShowPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
        figViewModel().illustrationUiInit.observe(viewLifecycleOwner, Observer {
        })

        //viewPager 最开始显示的page
        dataBinding?.viewPager?.currentItem = imageShowPosition
    }

    private fun getFigIllustrationList() {
        val type = object : TypeToken<List<ThumbnailModel>>() {}.type
        figIllustrationList = Gson().fromJson<MutableList<ThumbnailModel>>(dataStr, type)
    }

    /**
     * bottomSheetを初期化
     *
     */
    private fun initBottomSheetRecyclerView() {
        smoothScroller = object : LinearSmoothScroller(activity) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
        layoutManager = LinearLayoutManager(activity)
        layoutManager?.orientation = LinearLayoutManager.VERTICAL
        dataBinding?.recyclerViewPart?.layoutManager = layoutManager
        dataBinding?.recyclerViewPart?.itemAnimator = DefaultItemAnimator()
        dataBinding?.recyclerViewPart?.addItemDecoration(
                DividerItemDecoration(
                        activity?.applicationContext,
                        DividerItemDecoration.VERTICAL
                )
        )
        if (bottomSheetPartAdapter == null) {
            bottomSheetPartAdapter = BottomSheetPartAdapter(
                    activity,
                    mutableListOf(),
                    figViewModel()
            )
        }
        dataBinding?.recyclerViewPart?.adapter = bottomSheetPartAdapter
    }

    /**
     * Observersを初期化
     *
     */
    private fun initObservers() {
        figViewModel().togglePartList.observe(viewLifecycleOwner, EventObserver {
            if (!it) {
                return@EventObserver
            }
            updateBottomSheetList(dataBinding?.viewPager?.currentItem ?: 0)
        })

        figViewModel().bottomSheetAdapterItemClick.observe(this, EventObserver {
            transferToPartsDetail(it)
        })

        figViewModel().showBottomPart.observe(this, Observer {
            //显示FIG的部件list
            showBottomPart(it)
        })

        figViewModel().updatePartListState.observe(this, EventObserver {
            //刷新FIG的部件list
            refreshBottomSheet()
        })
        figViewModel().cartAdd.observe(this, EventObserver {

        })

        figViewModel().updateBarge.observe(this, EventObserver {

        })
    }

    /**
     * ブック_品番詳細画面に遷移する
     *
     */
    private fun transferToPartsDetail(partModel: PartModel) {

    }

    /**
     * 指定されたpositionに移動する
     *
     * @param position
     */
    fun updateBottomSheetList(position: Int = dataBinding?.viewPager?.currentItem ?: 0) {
        figViewModel().currentIllustrationId =
                (figViewPagerAdapter?.getItem(position) as FigFragment).getCurrentIllustrationId()
        figViewModel().currentFigId = figIllustrationList[position].figId ?: 0
        figViewModel().currentFigName = figIllustrationList[position].figName ?: ""
        figViewModel().currentIllustration = figIllustrationList[position].illustrations
        val mapKey = figViewModel().currentFigId.toString() + figViewModel().currentIllustrationId.toString()
        if (!figViewModel().partsMap[mapKey].isNullOrEmpty()) {
            val figId = figIllustrationList[position].figId
            bottomSheetPartAdapter?.setBottomSheetList(
                    figViewModel().partsMap[mapKey],
                    figId, figViewModel().getHighLightPartsIds(figId ?: 0) ?: listOf()
            )
        } else {
//            changeBottomSheetStatus(
//                figViewModel().partListApiState[figViewModel().currentFigId]
//                    ?: Constants.PartsListApiStatus.NoErrorLoading
//            )
        }
    }

//    /**
//     * bottomSheetの状態を更新する
//     * PartsListApiStatusがNoErrorLoadingの場合　→　インジケーターを表示する
//     * PartsListApiStatusがNoErrorSuccessの場合　→　パーツリストを表示する
//     * PartsListApiStatusがMaintenanceの場合　→　メンテナンスを表示する
//     * 他の場合　　　　　　　　　　　　　　　→　ネットワークエラーを表示する
//     *
//     * @param type
//     */
//    private fun changeBottomSheetStatus(type: Constants.PartsListApiStatus) {
//        when (type) {
//            Constants.PartsListApiStatus.NoErrorLoading -> {
//                dataBinding?.layoutProgress?.visibility = View.VISIBLE
//                dataBinding?.recyclerViewPart?.visibility = View.GONE
//                dataBinding?.layoutMaintenanceError?.visibility = View.GONE
//                dataBinding?.layoutNetworkError?.visibility = View.GONE
//            }
//            Constants.PartsListApiStatus.NoErrorSuccess -> {
//                dataBinding?.layoutProgress?.visibility = View.GONE
//                dataBinding?.recyclerViewPart?.visibility = View.VISIBLE
//                dataBinding?.layoutMaintenanceError?.visibility = View.GONE
//                dataBinding?.layoutNetworkError?.visibility = View.GONE
//            }
//            Constants.PartsListApiStatus.Maintenance -> {
//                dataBinding?.layoutProgress?.visibility = View.GONE
//                dataBinding?.recyclerViewPart?.visibility = View.GONE
//                dataBinding?.layoutMaintenanceError?.visibility = View.VISIBLE
//                dataBinding?.layoutNetworkError?.visibility = View.GONE
//            }
//            else -> {
//                dataBinding?.layoutProgress?.visibility = View.GONE
//                dataBinding?.recyclerViewPart?.visibility = View.GONE
//                dataBinding?.layoutMaintenanceError?.visibility = View.GONE
//                dataBinding?.layoutNetworkError?.visibility = View.VISIBLE
//            }
//        }
//    }

    /**
     * FIG 的部件listを表示
     *
     */
    private fun showBottomPart(referenceNumber: String?) {
        val mapKey = figViewModel().currentFigId.toString() + figViewModel().currentIllustrationId.toString()
        figViewModel().partsMap[mapKey]?.forEach { part ->
            if (referenceNumber.isNullOrBlank()) {
                part.isSelected = false
            } else {
                part.isSelected = referenceNumber == part.reference_number1
            }
        }
        refreshBottomSheet()
        //表示
        figViewModel().partsMap[mapKey]?.forEachIndexed { index, partModel ->
            if (partModel.isSelected) {
                bottomSheetShowPosition = index
                if (dataBinding?.drawLayout?.currentState != DragLayout.State.AT_MIDDLE) {
                    dataBinding?.drawLayout?.reLayout(DragLayout.State.AT_MIDDLE)
                    dataBinding?.drawLayout?.smoothToMid()
                }
                smoothScroller?.targetPosition = bottomSheetShowPosition!!
                layoutManager?.startSmoothScroll(smoothScroller)
                return
            }
        }
    }

    /**
     * 刷新 bottomSheet
     *
     */
    private fun refreshBottomSheet() {
        val mapKey = figViewModel().currentFigId.toString() + figViewModel().currentIllustrationId.toString()
        if (!figViewModel().partsMap[mapKey].isNullOrEmpty()) {
            val figId = figIllustrationList[dataBinding?.viewPager?.currentItem
                    ?: 0].figId
            bottomSheetPartAdapter?.setBottomSheetList(
                    figViewModel().partsMap[mapKey],
                    figId,
                    figViewModel().getHighLightPartsIds(figId ?: 0) ?: listOf()
            )
        }
    }
}

