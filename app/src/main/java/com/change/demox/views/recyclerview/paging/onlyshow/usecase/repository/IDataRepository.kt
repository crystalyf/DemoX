package com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository

import com.change.demox.views.recyclerview.paging.onlyshow.bean.Result
import com.change.demox.views.recyclerview.paging.onlyshow.bean.SampleModel

interface IDataRepository {
    suspend fun getData(offset: Int, count: Int): Result<List<SampleModel>>
}