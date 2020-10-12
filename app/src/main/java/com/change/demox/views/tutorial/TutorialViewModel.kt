/*
 * TutorialViewModel.kt
 *
 * Created by xingjunchao on 2020/06/04.
 */

package com.change.demox.views.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.change.demox.utils.Event

class TutorialViewModel : ViewModel() {

    //是否从menu画面进入的、 true: 是   false:否（启动splash画面直接进入的）
    var channelFromMenu = false

    //是否是最后一页、 true: 是   false:否
    val isLastPage = MutableLiveData<Boolean>()

    //next Click LiveData
    private val _nextClick = MutableLiveData<Event<Unit>>()
    val nextClick: LiveData<Event<Unit>> = _nextClick

    private val _okClick = MutableLiveData<Event<Boolean>>()
    val okClick: LiveData<Event<Boolean>> = _okClick

    /**
     * OK 按钮点击
     *
     */
    fun tutorialOkClick() {
        if (channelFromMenu) {
            _okClick.value = Event(true)
        } else {
            _okClick.value = Event(false)
        }
    }

    /**
     * next 按钮点击
     *
     */
    fun tutorialNextClick() {
        _nextClick.value = Event(Unit)
    }
}