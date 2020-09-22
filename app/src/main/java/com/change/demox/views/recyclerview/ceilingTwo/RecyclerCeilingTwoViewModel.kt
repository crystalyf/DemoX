/*
 * PartDetailViewModel.kt
 *
 * Created by xingjunchao on 2020/07/08.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.ceilingTwo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.change.demox.utils.FirstKanaLetterFromRomaji
import com.change.demox.views.recyclerview.ceiling.bean.CeilingItemModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecyclerCeilingTwoViewModel(
) : ViewModel() {

    val wirelessHeaderList = mutableListOf<String>()
    //转换前
    val wirelessList = MutableLiveData<MutableList<CeilingItemModel>>()
    //转换后
    val wirelessListAfter = mutableListOf<CeilingItemModel>()

    fun getData() {
        val type = object : TypeToken<List<CeilingItemModel>>() {}.type
        val list = Gson().fromJson<MutableList<CeilingItemModel>>(dataStr, type)
        wirelessList.value = list
    }

    /**
     * List<转换前> -> List<转换后></Indexable><T>*/
    fun transform() {
        var count = 0
        var list: MutableList<CeilingItemModel> ? = null
        val map = mutableMapOf<String, MutableList<CeilingItemModel>>()
        for (i in wirelessList.value?.indices!!) {
            val entity = wirelessList.value!![i]
            val kanaField = getFieldIndexBy(entity.kana)
            if (!map.containsKey(kanaField)) {
                list  = mutableListOf()
                //如果map中没这个五十音key,说明一条都没有，list插入一个catgory数据
                count++
                wirelessHeaderList.add(kanaField+ "行")
                map[kanaField] = list
            } else {
                //否则从map找到这个五十音key 对应的list,再插入
                list = map[kanaField]!!
                count = list[0].tage
            }
            list.add(entity)
            //把源数据里的tage变量全部重新赋值了，每个category多少个item确定了下来
            entity.tage = count
        }
        wirelessListAfter.clear()
        //変換
        for ((key, value) in map) {
            value.forEach{
                it.groupFieldName = key+ "行"
            }
            wirelessListAfter.addAll(value)
        }
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