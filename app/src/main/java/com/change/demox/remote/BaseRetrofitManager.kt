package com.change.demox.remote

import okhttp3.Request
import retrofit2.Call

abstract class BaseRetrofitManager {
    /**
     *  [apiPool] a pool of APIs that are running and have not yet returned
     *  callers have to implement this property
     *  for example: Sets.newConcurrentHashSet<String>()
     */
    abstract val apiPool: MutableSet<Request>

    /**
     *  exclusive run of apis
     *  @param [apiCall] retrofit api result
     *  @param [apiCallback] retrofit api callback
     *  @param [T] type of response data
     */
    protected fun <T> exclusiveRun(apiCall: Call<T>, apiCallback: ApiCallback<T?>) {
        val exclusiveRequest = apiCall.request()
        apiCallback.onFinished = {
            apiPool.remove(exclusiveRequest)
        }
        if (!apiPool.contains(exclusiveRequest)) {
            apiCall.enqueue(apiCallback)
            apiPool.add(exclusiveRequest)
        }
    }
}