package com.change.demox.views.recyclerview.paging.usecase.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.change.demox.views.recyclerview.paging.bean.Result
import com.change.demox.views.recyclerview.paging.bean.SampleModel
import com.change.demox.views.recyclerview.paging.usecase.repository.IDataRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


/**
 * 我们还需要定义一个角色，用来为PagedList容器提供分页数据，那就是数据源DataSource
 *
 * 什么是DataSource呢？它不应该是 数据库数据 或者 服务端数据， 而应该是 数据库数据 或者 服务端数据 的一个快照（Snapshot）
 * 每当Paging被告知需要更多数据：“Hi，我需要第45-60个的数据！”——数据源DataSource就会将当前Snapshot对应索引的数据交给PagedList。
 *

作者：却把清梅嗅
链接：https://juejin.im/post/6844903976777809928
来源：掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 */

class PageHomeDataSource(
        private val repo: IDataRepository
) : PageKeyedDataSource<Int, SampleModel>() {
    private var totalSize = Integer.MAX_VALUE
    var loadingStatus: MutableLiveData<Result<*>> = MutableLiveData()

    override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, SampleModel>
    ) {
        GlobalScope.launch {
            loadingStatus.postValue(Result.Loading)
            val result = repo.getData(0, params.requestedLoadSize)
            if (result is Result.Success) {
                if (result.data.size < params.requestedLoadSize) {
                    totalSize = result.data.size
                    callback.onResult(result.data, null, null)
                } else {
                    callback.onResult(result.data, null, result.data.size)
                }
                loadingStatus.postValue(Result.Success(""))
            } else {
                loadingStatus.postValue(result)
            }
        }
    }

    override fun loadAfter(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, SampleModel>
    ) {
        GlobalScope.launch {
            loadingStatus.postValue(Result.Loading)
            val result = repo.getData(params.key, params.requestedLoadSize)
            if (result is Result.Success) {
                if (result.data.size < params.requestedLoadSize) {
                    totalSize = params.key + result.data.size
                    callback.onResult(result.data, null)
                } else {
                    callback.onResult(
                            result.data,
                            params.key + result.data.size
                    )
                }
                loadingStatus.postValue(Result.Success(""))
            } else {
                loadingStatus.postValue(result)
            }
        }
    }

    override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, SampleModel>
    ) {
        // loadAfterに取得するデータを追加するので、前ページを実装する必要はない
    }
}