/*
 * DataRepositoryImpl.kt
 *
 * Created by yananyu on 2019/11/29.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository

import android.content.Context
import com.change.demox.views.recyclerview.paging.onlyshow.bean.Result
import com.change.demox.views.recyclerview.paging.onlyshow.bean.SampleModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DataRepositoryImpl(
        private val context: Context,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IDataRepository {


    private var offset = 0

    override suspend fun getData(offset: Int, count: Int): Result<List<SampleModel>> {

        return withContext(ioDispatcher) {
            val apiResult = getData(offset)
            val pref = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
            when (apiResult) {
                is Result.Error -> apiResult
                is Result.Loading -> apiResult
                is Result.Success -> Result.Success(apiResult.data.map {
                    SampleModel(it, pref.getBoolean(it, false))
                })
            }
        }
    }

    // suspendの中にcallback形のリクエストを呼ぶ場合の例
    private suspend fun getData(offset: Int): Result<List<String>> {
        this.offset = offset
        val result = suspendCoroutine<Result<List<String>>> {
            mockCallBackCall(object : MockCallback {
                override fun onSuccess(data: List<String>) {
                    it.resume(Result.Success(data))
                }

                override fun onFailed(e: Exception) {
                    it.resume(Result.Error(e))
                }

            })
        }
        return result
    }

    private fun mockCallBackCall(callback: MockCallback) {
        callback.onSuccess(List(50) {
            "this is data no.${offset + it}"
        })
    }

    interface MockCallback {
        fun onSuccess(data: List<String>)
        fun onFailed(e: Exception)
    }

    companion object {
        private var instance: IDataRepository? = null
        fun instance(context: Context): IDataRepository {
            if (instance == null) {
                instance =
                        DataRepositoryImpl(
                                context
                        )
            }
            return instance!!
        }
    }

}