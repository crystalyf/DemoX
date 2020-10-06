package com.change.demox.views.recyclerview.paging.onlyshow.usecase.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.change.demox.views.recyclerview.paging.onlyshow.bean.SampleModel
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository.IDataRepository

/**
 * 但是我们需要构建一个新的PagedList的时候——比如数据已经失效，DataSource中旧的数据没有意义了，因此DataSource也需要被重置。
在代码中，这意味着新的DataSource对象被创建，因此，我们需要提供的不是DataSource，而是提供DataSource的工厂。

作者：却把清梅嗅
链接：https://juejin.im/post/6844903976777809928
来源：掘金
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。


ps:所以，这个类的返回值是DataSource.Factory

 */

class PageHomeDataSourceFactory(val repo: IDataRepository) :
        DataSource.Factory<Int, SampleModel>() {
    val sourceLiveData = MutableLiveData<PageHomeDataSource>()
    override fun create(): DataSource<Int, SampleModel> {
        val source = PageHomeDataSource(repo)
        sourceLiveData.postValue(source)
        return source
    }

}