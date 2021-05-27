/*
 * PartDetailViewModel.kt
 *
 * Created by xingjunchao on 2020/07/08.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.edittext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.change.demox.utils.Event

class EditTextViewModel(
) : ViewModel() {

    companion object {
        //验证电子邮件的正则
        private const val EMAIL_CHECK_PATTERN = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
    }

    private val _clickEmailButton = MutableLiveData<Event<Unit>>()
    val clickEmailButton: LiveData<Event<Unit>> = _clickEmailButton


    val email = MutableLiveData<String>()

    val postButtonEnable = email.map {
        it.matches(Regex(EMAIL_CHECK_PATTERN))
    }

    fun clickEmail() {
       _clickEmailButton.value = Event(Unit)
    }


}