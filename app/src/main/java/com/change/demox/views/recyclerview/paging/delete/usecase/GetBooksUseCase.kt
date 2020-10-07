/*
 * ModelSearchResultUseCase.kt
 *
 * Created by wangxin on 2020/06/23.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */
package com.change.demox.views.recyclerview.paging.delete.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.change.demox.remote.RetrofitApiException
import com.change.demox.views.recyclerview.paging.delete.bean.Book
import com.change.demox.views.recyclerview.paging.delete.usecase.paging.ModelSearchResultDataSourceFactory
import com.change.demox.views.recyclerview.paging.delete.usecase.repository.ISearchRepository

/**
 * PagingDeleteFragment画面 用 UseCase
 *
 * @property iSearchRepository ITopRepository
 */
class GetBooksUseCase(
        private val iSearchRepository: ISearchRepository) {

    private var factory: ModelSearchResultDataSourceFactory? = null

    fun errorState(): LiveData<RetrofitApiException> = if (factory != null) {
        Transformations.switchMap(factory!!.sourceLiveData) {
            it.error
        }
    } else {
        MutableLiveData()
    }

    fun getBooks(): LiveData<PagedList<Book>> {
        //写法较之前提交的PagingFragment更为简洁
        factory = ModelSearchResultDataSourceFactory(iSearchRepository)
        return LivePagedListBuilder(
                factory!!, PagedList.Config.Builder()
                .setPageSize(10) // ページ読み込みの数
                .setEnablePlaceholders(true) // PlaceHoldersを起動する
                .setInitialLoadSizeHint(10) // 初期ロードの数
                .setPrefetchDistance(5) // 下から複数のデータがある場合、次のページのデータをロードします
                .build()
        ).build()
    }

    /**
     * 下拉刷新
     *
     */
    fun refresh() {
        factory?.sourceLiveData?.value?.invalidate()
    }

    fun retryAllFailed() {
        factory?.sourceLiveData?.value?.retryAllFailed()
    }

}