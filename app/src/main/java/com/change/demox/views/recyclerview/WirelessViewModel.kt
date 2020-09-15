/*
 * PartDetailViewModel.kt
 *
 * Created by xingjunchao on 2020/07/08.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.change.demox.utils.FirstKanaLetterFromRomaji
import com.change.demox.views.recyclerview.bean.WirelessContactModel
import com.change.demox.views.recyclerview.bean.WirelessTransformModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WirelessViewModel(
) : ViewModel() {

    //转换前
    val wirelessList = MutableLiveData<MutableList<WirelessContactModel>>()

    //转换后
    var wirelessTransformList = mutableListOf<WirelessTransformModel>()

    fun getData() {
        val type = object : TypeToken<List<WirelessContactModel>>() {}.type
        val list = Gson().fromJson<MutableList<WirelessContactModel>>(dataStr, type)
        wirelessList.value = list
    }

    /**
     * List<转换前> -> List<转换后></Indexable><T>*/
    fun transform() {
        var list: MutableList<WirelessTransformModel> = mutableListOf()
        val map = mutableMapOf<String, MutableList<WirelessTransformModel>>()
        for (i in wirelessList.value?.indices!!) {
            val entity = WirelessTransformModel()
            val item = wirelessList.value!![i]
            entity.data = item
            entity.kanaField = getFieldIndexBy(item.kana)
            if (!map.containsKey(entity.kanaField)) {
                //如果map中没这个五十音key,说明一条都没有，list插入一个catgory数据
                val transformModel = WirelessTransformModel()
                transformModel.indexTitle = entity.kanaField + "行"
                list.add(transformModel)
                map[entity.kanaField] = list
            } else {
                //否则从map找到这个五十音key 对应的list,再插入
                list = map[entity.kanaField]!!
            }
            list.add(entity)
        }
        wirelessTransformList.addAll(list)
    }

    /**
     * 根据日文音获取所在的音段
     *
     * @param kanaName  日文音
     * @return  音段
     */
    private fun getFieldIndexBy(kanaName: String): String {
        if (kanaName.isEmpty()) return ""
        return FirstKanaLetterFromRomaji.getFirstKanaLetter(kanaName.first().toString())
    }


    //数据源
    private val dataStr = "[{\"num\":\"1111001\",\"name\":\"大阪駅駅長室\",\"id\":\"10001\",\"kana\":\"おおさか\",\"group\":1},{\"num\":\"1111002\",\"name\":\"大阪駅3番ホーム\",\"id\":\"10002\",\"kana\":\"おおさか\",\"group\":1},{\"num\":\"2222000\",\"name\":\"東京駅駅長室\",\"id\":\"10003\",\"kana\":\"と\",\"group\":1},{\"num\":\"1111000\",\"name\":\"大阪駅5番ホーム\",\"id\":\"10004\",\"kana\":\"だ\",\"group\":1},{\"num\":\"1234567\",\"name\":\"品川駅駅長室\",\"id\":\"10005\",\"kana\":\"し\",\"group\":1},{\"num\":\"7654321\",\"name\":\"品川駅駅長室品川駅駅長室品川駅駅長室品川駅駅長室品川駅駅長室\",\"id\":\"10006\",\"kana\":\"し\",\"group\":1},{\"num\":\"2223333\",\"name\":\"きき駅長室\",\"id\":\"10007\",\"kana\":\"き\",\"group\":1},{\"num\":\"2004563\",\"name\":\"なな駅長室\",\"id\":\"10007\",\"kana\":\"な\",\"group\":1},{\"num\":\"2029873\",\"name\":\"は1は駅長室\",\"id\":\"10007\",\"kana\":\"は\",\"group\":1},{\"num\":\"2029876\",\"name\":\"は2駅長室\",\"id\":\"10008\",\"kana\":\"は\",\"group\":1},{\"num\":\"2653413\",\"name\":\"ま1駅長室\",\"id\":\"10009\",\"kana\":\"ま\",\"group\":1},{\"num\":\"2653416\",\"name\":\"ま2駅長室\",\"id\":\"10010\",\"kana\":\"ま\",\"group\":1},{\"num\":\"2223333\",\"name\":\"や1駅長室\",\"id\":\"10011\",\"kana\":\"や\",\"group\":1},{\"num\":\"2223333\",\"name\":\"や2駅長室\",\"id\":\"10012\",\"kana\":\"や\",\"group\":1},{\"num\":\"2223333\",\"name\":\"き1駅長室\",\"id\":\"10013\",\"kana\":\"き\",\"group\":1},{\"num\":\"2223339\",\"name\":\"き2駅長室\",\"id\":\"10014\",\"kana\":\"き\",\"group\":1}]"

}