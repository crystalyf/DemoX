package com.change.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.change.demox.remote.RetrofitApiException
import com.change.demox.utils.Constants
import com.change.demox.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    private val _showProgress = MutableLiveData<Event<Boolean>>()
    val showProgress: LiveData<Event<Boolean>> = _showProgress

    private val _showBlockingProgress = MutableLiveData<Event<Boolean>>()
    val showBlockingProgress: LiveData<Event<Boolean>> = _showBlockingProgress

    fun runPipeline(
            context: CoroutineContext = Dispatchers.IO,
            showProgress: Boolean = true,
            showBlockingProgress: Boolean = false,
            errorHandler: ((Exception) -> Unit)? = null,
            pipelineBlock: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context) {
            if (showProgress) {
                postShowProgress(showBlockingProgress)
            }
            try {
                pipelineBlock()
            } catch (e: Exception) {
                if (e is RetrofitApiException) {
                    postErrorEvent(e, showBlockingProgress)
                }
                Timber.e(e)
                errorHandler?.invoke(e)
            } finally {
                //API重试的时候，转圈不消失
                if (showProgress) {
                    postHideProgress()
                }
            }
        }
    }

    fun postErrorEvent(e: RetrofitApiException, showBlockingProgress: Boolean = false) {
        when (e.errorModel.getErrorType()) {
            Constants.ApiErrorType.AuthError -> {
                //Unauthorized
                //  _authError.postValue(ExceptionEvent(EventException(showBlockingProgress, e)))
            }
            Constants.ApiErrorType.PasswordExpired -> {
                //PasswordExpired
                //  _passwordExpired.postValue(ExceptionEvent(EventException(showBlockingProgress, e)))
            }
            Constants.ApiErrorType.PermissionError -> {
                //Forbidden
                //  _permissionError.postValue(ExceptionEvent(EventException(showBlockingProgress, e)))
            }
            Constants.ApiErrorType.InvalidParameter, Constants.ApiErrorType.BadRequest -> {
                //  _invalidParameterError.postValue(ExceptionEvent(EventException(showBlockingProgress, e)))
            }
            Constants.ApiErrorType.Maintenance -> {
                //Maintenance
                //  _maintenanceError.postValue(ExceptionEvent(EventException(showBlockingProgress, e)))
            }
            Constants.ApiErrorType.NetworkError -> {
                //   _networkError.postValue(ExceptionEvent(EventException(showBlockingProgress, e)))
            }

            Constants.ApiErrorType.OtherError -> {
                //  _otherError.postValue(ExceptionEvent(EventException(showBlockingProgress, e)))
            }

            else -> {
                //  _commonError.postValue(ExceptionEvent(EventException(showBlockingProgress, e)))
            }
        }
    }

    fun postShowProgress(showBlockingProgress: Boolean) {
        if (showBlockingProgress) {
            _showBlockingProgress.postValue(Event(true))
        } else {
            _showProgress.postValue(Event(true))
        }
    }

    fun postHideProgress() {
        _showBlockingProgress.postValue(Event(false))
        _showProgress.postValue(Event(false))
    }

}