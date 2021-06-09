/*
 * PartDetailViewModel.kt
 *
 * Created by xingjunchao on 2020/07/08.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.dialog.bottomsheetdialog


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.change.demox.utils.Event


class BottomSheetDialogViewModel(
) : ViewModel() {

    //问题种类的文本
    var categoryNameText = MutableLiveData<String>()
    //问题种类的文本显示的Position
    var categoryNamePosition: Int = 0

    //选择范围内容的Item
    var dialogItemList: MutableList<String> = mutableListOf()

    private val _selectTextClick = MutableLiveData<Event<Unit>>()
    val selectTextClick: LiveData<Event<Unit>> = _selectTextClick

    /**
     * 组装选择内容
     */
    fun makeDialogContent(){
        dialogItemList.add("第1项")
        dialogItemList.add("第2项")
        dialogItemList.add("第3项")
        dialogItemList.add("第4项")
        dialogItemList.add("第5项")
    }

    fun selectTextClick() {
        _selectTextClick.value = Event(Unit)
    }
}