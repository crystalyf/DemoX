package com.change.demox.views.recyclerview.paging.onlyshow.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.change.demox.views.recyclerview.paging.onlyshow.bean.Result
import com.change.demox.views.recyclerview.paging.onlyshow.bean.SampleModel
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.paging.PageHomeDataSourceFactory
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository.IDataRepository

class GetPagingHomeDataUseCase(private val dataRepo: IDataRepository) {

    /**
     * 用来为PagedList容器提供分页数据，那就是数据源DataSource
     *
     *
     * 页面初始化时，会通过工厂方法创建一个新的DataSource，这之后对应会创建一个新的PagedList，每当PagedList想要获取下一页的数据，数据源都会根据请求索引进行数据的提供。
    当数据失效时，DataSource.Factory会再次创建一个新的DataSource，其内部包含了最新的数据快照snapshot（本案例中代表着数据库中的最新数据），随后创建一个新的PagedList，并从
    DataSource中取最新的数据进行展示——当然，这之后的分页流程都是相同的，无需再次复述。

    ps:snapshot()函数在PagedList第605行

    作者：却把清梅嗅
    链接：https://juejin.im/post/6844903976777809928
    来源：掘金
    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    val datasource = PageHomeDataSourceFactory(dataRepo)
    val refreshState: LiveData<Result<*>> = Transformations.switchMap(datasource.sourceLiveData) {
        it.loadingStatus
    }


    /**
     * 如何 将数据源的工厂和LiveData<PagedList>进行串联  ？
    因此我们还需要定义一个新的角色PagedListBuilder，开发者将 数据源工厂 和 相关配置 统一交给PagedListBuilder，即可生成对应的LiveData<PagedList<User>>:

    作者：却把清梅嗅
    链接：https://juejin.im/post/6844903976777809928
    来源：掘金
    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     */

    operator fun invoke(): LiveData<PagedList<SampleModel>> {
        //通过建造者模式create
        return LivePagedListBuilder(
                datasource, PagedList.Config.Builder()
                .setPageSize(50)   // 分页加载的数量
                .setEnablePlaceholders(true)  // 是否启用占位符
                .setInitialLoadSizeHint(50)  // 初次加载的数量：定义首次加载时要加载的Item数量。此值通常大于PageSize，因此在初始化列表时，该配置可以使得加载的数据保证屏幕可以小范围的滚动。
                .setPrefetchDistance(1)  // 预取数据的距离：顾名思义，该参数配置定义了列表当距离加载边缘多远时进行分页的请求，默认大小为PageSize——即距离底部还有一页数据时，开启下一页的数据加载
                .build()
        ).build()
        /**
         * 点进build()可以看到，最终Builder中的所有配置都通过annotation依赖注入的方式对PagedList进行了实例化
         */
    }

    fun refresh() {
        datasource.sourceLiveData.value?.invalidate()
    }
}


