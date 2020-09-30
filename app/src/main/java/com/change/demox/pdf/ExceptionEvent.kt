/*
 * ExceptionEvent.kt
 *
 * Created by kangzewei on 2020/06/02.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.pdf

import androidx.lifecycle.Observer
import com.change.demox.utils.Constants
import com.change.demox.utils.Event

open class ExceptionEvent<out T>(
    content: T,
    private val handler: ((Constants.ApiErrorType) -> Unit)? = null
) : Event<T>(content) {
    fun peekAction(): ((Constants.ApiErrorType) -> Unit)? {
        return handler
    }
}

class ExceptionEventObserver<T>(private val onEventUnhandledContent: (T, ((Constants.ApiErrorType) -> Unit)?) -> Unit) :
    Observer<ExceptionEvent<T>> {
    override fun onChanged(event: ExceptionEvent<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it, event.peekAction())
        }
    }
}