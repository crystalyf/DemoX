/*
 * ModelSearchResultDataSourceFactory.kt
 *
 * Created by xinghunchao on 2020/06/23.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */
package com.change.demox.views.recyclerview.paging.delete.usecase.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.recyclerview.paging.delete.bean.Book
import com.change.demox.views.recyclerview.paging.delete.usecase.repository.ISearchRepository

/**
 * モデル検索結果画面 用 データソースの工場 クラス
 *
 * @property repository ITopRepository
 */
class ModelSearchResultDataSourceFactory(
        private val repository: ISearchRepository
) : DataSource.Factory<Int?, Book>() {

    val sourceLiveData = MutableLiveData<ModelSearchResultDataSource>()

    override fun create(): DataSource<Int?, Book> {
        val source = ModelSearchResultDataSource(repository)
        sourceLiveData.postValue(source)
        return source
    }

}