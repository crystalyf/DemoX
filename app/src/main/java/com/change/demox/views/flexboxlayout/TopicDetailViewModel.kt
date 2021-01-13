/*
 * TopicDetailViewModel.kt
 *
 * Created by jilingwen on 2019/12/31.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.flexboxlayout

import androidx.lifecycle.MutableLiveData
import com.change.base.BaseViewModel

/**
 * MemberListByReadAtFragmentのViewModel
 *
 */
class TopicDetailViewModel() : BaseViewModel() {


    /**
     * 数据源
     */
    var memberListByReadAt = MutableLiveData<MutableList<MemberReactionModel>>()

    /**
     * 連絡詳細を取得
     */
    fun getTopicDetail() {
        //TODO: false data
        val memberListByReadAtTemp = ArrayList<MemberReactionModel>()
        for (index in 1..17) {
            val beanLight = MemberLightModel(index.toString(), "编号：$index", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3155998395,3600507640&fm=26&gp=0.jpg", 1)
            val bean = MemberReactionModel(1, beanLight, 1, false)
            memberListByReadAtTemp.add(bean)
        }
        memberListByReadAt.value = memberListByReadAtTemp
    }
}