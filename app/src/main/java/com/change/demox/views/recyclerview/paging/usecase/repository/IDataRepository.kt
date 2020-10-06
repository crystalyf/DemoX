package com.change.demox.views.recyclerview.paging.usecase.repository

import com.change.demox.views.recyclerview.paging.bean.Result
import com.change.demox.views.recyclerview.paging.bean.SampleModel

interface IDataRepository {
    suspend fun getData(offset: Int, count: Int): Result<List<SampleModel>>
}