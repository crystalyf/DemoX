package com.change.demox.remote

import com.change.demox.remote.bean.ApiErrorModel
import com.google.gson.Gson
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ApiCallback<T>(
        private val cancellableContinuation: CancellableContinuation<T?>,
        private val onSuccess: ((response: Response<T>) -> Unit)? = null,
        private val onFailed: ((response: Response<T>) -> Unit)? = null,
        private val onConnectionFailed: ((t: Throwable) -> Unit)? = null,
        private val withResume: Boolean = true
) : Callback<T> {
    var onFinished = {}
    override fun onFailure(call: Call<T>, t: Throwable) {
        onFinished.invoke()
        if (withResume) {
            cancellableContinuation.resumeWithException(
                    RetrofitApiException(
                            ApiErrorModel.getNetWorkErrorModel(
                                    t
                            )
                    )
            )
        }
        onConnectionFailed?.invoke(t)
    }


    /**
     * onResponse回调，协程通过挂起来执行耗时任务，而成功与失败会分别通过resume()与resumeWithExecption()来唤起挂起的协程，让它返回之前的挂起点，进行执行。
     * 而resumeWithExecption()内部也是调用了resume()，所以协程的唤起都是通过resume()来操作的。调用resume()之后，我们可以在调用协程的地方返回请求的结果
     *
     */
    override fun onResponse(call: Call<T>, response: Response<T>) {
        onFinished.invoke()
        if (response.isSuccessful) {
            if (withResume) {
                if ((response.body() is ApiErrorModel) && (response.body() as ApiErrorModel).code != null) {
                    cancellableContinuation.resumeWithException(RetrofitApiException((response.body() as ApiErrorModel)))
                    onFailed?.invoke(response)
                    return
                }
                //唤起协程，返回body
                cancellableContinuation.resume(response.body())
            }
            onSuccess?.invoke(response)
        } else {
            if (withResume) {
                val model = if (response.code() >= 500) {
                    //设置ApiErrorModel为Maintenance的错误类型
                    ApiErrorModel.getMaintenanceModel()
                } else {
                    try {
                        Gson().fromJson(response.errorBody()?.string(), ApiErrorModel::class.java)
                    } catch (e: Exception) {
                        //设置ApiErrorModel为NetWorkError的错误类型
                        ApiErrorModel.getNetWorkErrorModel(e)
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        // 唤起协程，抛出异常
                        cancellableContinuation.resumeWithException(RetrofitApiException(model))
                    } catch (e: Exception) {
                        if (e is RetrofitApiException) {
                            cancellableContinuation.resumeWithException(e)
                        } else {
                            cancellableContinuation.resumeWithException(
                                    RetrofitApiException(
                                            ApiErrorModel.getNetWorkErrorModel(e)
                                    )
                            )
                        }
                    }
                }
            }
            onFailed?.invoke(response)
        }
    }
}