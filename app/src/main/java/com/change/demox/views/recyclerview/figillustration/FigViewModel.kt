/*
 * FigIllustrationViewModel.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.change.base.BaseViewModel
import com.change.demox.utils.Event
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.recyclerview.figillustration.bean.Illustration
import com.change.demox.views.recyclerview.figillustration.bean.PartModel
import com.change.demox.views.recyclerview.figillustration.bean.ThumbnailModel

/**
 * Fig ViewModel
 *
 */
open class FigViewModel(
) : BaseViewModel() {

    /**
     *  画面显示的图片细节参数
     */
    var offsetX = 0F
    var offsetY = 0F
    var currentScale = 0F
    var illustrationId = 0

    /**
     * SharedPreferences
     */
    var pref: SharedPreferences? = null

    var currentIllustrationId = 0

    var illustrationIndexMap: MutableMap<Int, Int> = mutableMapOf()

    var isFromLocal = false

    var bookId = 0

    //ブック内検索_結果 から遷移の場合、選択のパーツのpartId
    var selectedPartId: Int? = null

    //ブック内検索_結果 から遷移の場合、選択のパーツのFigId
    var selectedFigId: Int? = null

    //現在のFIG モデル名
    var currentFigModelName: String? = null
    var currentFigName: String? = null
    var currentFigId = 0
    var currentIllustration: List<Illustration>? = mutableListOf()

    //bottomSheet itemのクリック位置
    private val _bottomSheetAdapterItemClick = MutableLiveData<Event<PartModel>>()
    val bottomSheetAdapterItemClick: LiveData<Event<PartModel>> = _bottomSheetAdapterItemClick

    //「カート」high light LiveData
    private val _setHighlight = MutableLiveData<List<Int>>()
    val setHighlight: LiveData<List<Int>> = _setHighlight

    //初期化 IllustrationFragmentUI LiveData
    private val _illustrationUiInit = MutableLiveData<Int>()
    val illustrationUiInit: LiveData<Int> = _illustrationUiInit

    //cart add LiveData
    private val _cartAdd = MutableLiveData<Event<Unit>>()
    val cartAdd: LiveData<Event<Unit>> = _cartAdd

    private val _updateBarge = MutableLiveData<Event<Int>>()
    val updateBarge: LiveData<Event<Int>> = _updateBarge

    //bottomSheet show LiveData
    private val _showBottomPart = MutableLiveData<String>()
    val showBottomPart: LiveData<String> = _showBottomPart

    //updatePartListState LiveData
    private val _updatePartListState = MutableLiveData<Event<Unit>>()
    val updatePartListState: LiveData<Event<Unit>> = _updatePartListState

    //all part list data: list<FigId + IllustrationId, partList>
    var partsMap: MutableMap<String, MutableList<PartModel>> = mutableMapOf()

    //all selected list data : map<figId, parts id>
    var selectedPartsIdMap: MutableMap<Int, List<Int>> = mutableMapOf()

    //all high light parts data: map <figId, parts id list>
    var highLightPartsIdMap: MutableMap<Int, List<Int>> = mutableMapOf()

    /**
     * 一つイラストのパーツをリストを取る、　true:　bottomSheetのパーツリストは更新する false: 何もしない
     */
    private val _togglePartList = MutableLiveData<Event<Boolean>>()
    val togglePartList: LiveData<Event<Boolean>> = _togglePartList

    /**
     * Fig 内パーツ一覧取得
     *
     * @param figId figId
     * @param modelName モデル名
     */
    fun getPartListData(
            thumbnailModel: ThumbnailModel?,
            modelName: String?,
            illustrations: List<Illustration>?
    ) {
//        currentThumbnailModel = thumbnailModel
//        val figId = thumbnailModel?.figId ?: 0
//        var responseList: MutableList<PartModel>?
//        runPipeline(pipelineBlock = {
//            responseList = if (!isFromLocal) {
//                //API data
//                figIllustrationUseCase.getPartListByFigId(figId)
//            } else {
//                //local data
//                withContext(Dispatchers.Main) {
//                    figIllustrationUseCase.getPartFromBD(figId, pref?.userId ?: "")?.toMutableList()
//                }
//            }
//            if (responseList == null) {
//                return@runPipeline
//            }
//            responseList?.forEach { model ->
//                //セットモデル名
//                model.modelName = modelName
//                model.figName = thumbnailModel?.figName
//                if (model.illustrations?.isNotEmpty()!!) {
//                    createPartsList(model, model.illustrations, figId)
//                } else {
//                    createPartsList(
//                            model,
//                            illustrations?.map { Illustrations(it.illustration_id, listOf()) },
//                            figId
//                    )
//                }
//            }
//            _illustrationUiInit.postValue(figId)
//        }, showProgress = false, errorHandler = {
//            _showPartListError.postValue(Event(figId))
//        })
    }

//    private fun createPartsList(model: PartModel, illustrations: List<Illustrations>?, figId: Int) {
//        illustrations?.forEach {
//            if (partsMap.containsKey(figId.toString() + it.illustration_id.toString())) {
//                val mapKey = figId.toString() + it.illustration_id.toString()
//                //isExit:partsMapすでに存在するかどうか？true: はい   false:ない
//                var isExit = false
//                partsMap[mapKey]?.forEach { part ->
//                    if (part.part_id == model.part_id) {
//                        isExit = true
//                    }
//                }
//                if (!isExit) {
//                    partsMap[mapKey]?.add(model)
//                }
//            } else {
//                val parts = mutableListOf<PartModel>()
//                parts.add(model)
//                partsMap[figId.toString() + it.illustration_id.toString()] = parts
//            }
//        }
//    }

    /**
     * 表示するのイラストの選択中のパーツのIdリストを設定する
     *
     * @param figId　表示のイラストのフィグID
     * @param ids　パーツのIdのリスト
     */
    fun setSelectedPartsId(figId: Int, ids: List<Int>) {
        selectedPartsIdMap[figId] = ids
    }

    /**
     * 表示するのイラストの選択中のパーツのIdリストを取る
     *
     * @param figId　表示のイラストのフィグID
     * @return 選択のパーツのIdのリスト
     */
    fun getSelectedPartsIds(figId: Int): List<Int>? {
        return selectedPartsIdMap[figId]
    }

    /**
     * 表示するのイラストのハイライトのパーツ(カートに追加した)のIdリストを取る
     *
     * @param figId　表示のイラストのフィグId
     * @return　ハイライトのパーツのIdリスト
     */
    fun getHighLightPartsIds(figId: Int): List<Int>? {
        return highLightPartsIdMap[figId]
    }

    /**
     * Figで　illustration_id パーツ一覧取得
     *
     * @param illustrationId illustration Id
     *
     */
    fun getCurrentIllustrationPartList(illustrationId: Int, figId: Int): MutableList<PartModel>? {
        if (partsMap[figId.toString() + illustrationId.toString()].isNullOrEmpty()) {
            return mutableListOf()
        }
        _togglePartList.postValue(Event(true))
        return partsMap[figId.toString() + illustrationId.toString()]
    }

    /**
     *  ブック_FIGリスト画面を表示する
     *
     * @param referenceNumber referenceNumber
     */
    fun showBottomSheetPart(referenceNumber: String?) {
        _showBottomPart.value = referenceNumber
    }

    fun getIllustrationImageUrl(illustration: Illustration): String {
        return illustration.low_quality_image_download_link
    }

}