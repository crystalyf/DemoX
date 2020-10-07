/*
 * ModelSearchResultDataSource.kt
 *
 * Created by wangxin on 2020/06/23.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */
package com.change.demox.views.recyclerview.paging.delete.usecase.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.change.demox.remote.RetrofitApiException
import com.change.demox.views.recyclerview.paging.delete.bean.Book
import com.change.demox.views.recyclerview.paging.delete.bean.BookModel
import com.change.demox.views.recyclerview.paging.delete.usecase.repository.ISearchRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

/**
 * PagingDeleteFragment画面 用
 *
 * @property repository ISearchRepository
 */
class ModelSearchResultDataSource(
        private val repository: ISearchRepository
) : PageKeyedDataSource<Int?, Book>() {

    // keep a function reference for the retry event
    var retry: (() -> Any)? = null

    private var _loadingStatus: MutableLiveData<Boolean> = MutableLiveData()
    val loadingStatus: LiveData<Boolean> = _loadingStatus

    private var _error: MutableLiveData<RetrofitApiException> = MutableLiveData()
    val error: LiveData<RetrofitApiException> = _error

    private var itemSize = 0

    fun retryAllFailed() {
        val preRetry = retry
        retry = null
        preRetry?.let {
            GlobalScope.launch {
                it.invoke()
            }
        }
    }

    override fun loadInitial(
            params: LoadInitialParams<Int?>,
            callback: LoadInitialCallback<Int?, Book>
    ) {
        runBlocking {
            _loadingStatus.postValue(true)
            val bookModel: BookModel?
            try {
                //最初加载时，offset是0
                bookModel = repository.getBooks(0)
            } catch (e: Exception) {
                if (e is RetrofitApiException) {
                    _error.postValue(e)
                }
                Timber.e(e)
                _loadingStatus.postValue(false)
                return@runBlocking
            }

            itemSize = bookModel?.books?.size ?: 0
            val books = bookModel?.books?.toMutableList() ?: mutableListOf()
            callback.onResult(
                    books,
                    null,
                    if (itemSize < bookModel?.total ?: 0) itemSize else null
            )
            _loadingStatus.postValue(false)
        }
    }

    override fun loadAfter(
            params: LoadParams<Int?>,
            callback: LoadCallback<Int?, Book>
    ) {
        runBlocking {
            val bookModel: BookModel?
            try {
                bookModel = repository.getBooks(params.key)
            } catch (e: Exception) {
                if (e is RetrofitApiException) {
                    _error.postValue(e)
                }
                retry = {
                    loadAfter(params, callback)
                }
                Timber.e(e)
                return@runBlocking
            }

            itemSize += bookModel?.books?.size ?: 0
            val books = bookModel?.books?.toMutableList() ?: mutableListOf()
            callback.onResult(
                    books,
                    if (itemSize < bookModel?.total ?: 0) itemSize else null
            )
            retry = null
        }
    }

    override fun loadBefore(
            params: LoadParams<Int?>,
            callback: LoadCallback<Int?, Book>
    ) {
        // loadAfterに取得するデータを追加するので、前ページを実装する必要はない
    }
}