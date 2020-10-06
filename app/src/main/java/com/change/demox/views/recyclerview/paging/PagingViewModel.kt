package com.change.demox.views.recyclerview.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.change.demox.utils.Event
import com.change.demox.views.recyclerview.paging.bean.Result
import com.change.demox.views.recyclerview.paging.bean.SampleModel
import com.change.demox.views.recyclerview.paging.usecase.GetPagingHomeDataUseCase

class PagingViewModel(
        private val getHomeDataUseCase: GetPagingHomeDataUseCase
) : ViewModel() {

    /**
     * 总共有4层角色：
     *
     * 1. PagedList
     * 2. DataSource、 DataSource.Factory
     * 3. PagedListBuilder (将DataSource.Factory和LiveData<PagedList>进行衔接)
     * 4. PagedList.Config  PagedList.Config，以描述更细节化的配置参数
     *
     *
     */


    /**
     * Google设计出了一个新的角色PagedList，顾名思义，该角色的意义就是： 分页列表数据的list 。
     *
     * 既然有了List，为什么需要额外设计这样一个PagedList的数据结构？本质原因在于加载分页数据的操作是异步的 ，因此定义PagedList的第二个作用是 对分页数据的异步加载
     */

    //在ViewModel中，开发者可以轻易通过对items进行订阅以响应分页数据的更新。由usecase层中的LivePagedListBuilder转换DataSource数据源而得到
    val datas: LiveData<PagedList<SampleModel>> = getHomeDataUseCase()

    // データ取得中のフラグ
    val dataLoading: LiveData<Boolean> = Transformations.map(getHomeDataUseCase.refreshState) {
        it is Result.Loading
    }

    // データ取得中にエラー
    val requestStatus = Transformations.map(getHomeDataUseCase.refreshState) {
        if (it is Result.Error) {
            it.exception
        }
    }

    val empty: LiveData<Boolean> = Transformations.map(datas) {
        it.isEmpty()
    }

    private val _reloadEvent = MutableLiveData<Event<Int>>()
    val reloadEvent: LiveData<Event<Int>> = _reloadEvent

    /**
     * 最初からデータを取得処理
     */
    fun refresh() {
        getHomeDataUseCase.refresh()
    }


    /**
     *
     * update机制：
     *
     * 正如前文所说，数据是动态的， 假设用户通过操作添加了一个联系人，这时数据库中的数据集发生了更新。
    因此，这时屏幕中RecyclerView对应的PagedList和DataSource已经没有失效了，因为DataSource中的数据是之前数据库中数据的快照，数据库内部进行了更新，PagedList从旧的DataSource中再取数据毫无意义。
    因此，Paging组件接收到了数据失效的信号，这意味着生产者需要重新构建一个PagedList，因此DataSource.Factory再次提供新版本的数据源DataSource V2——其内部持有了最新数据的快照。
    在创建新的PagedList的时候，针对PagedList内部的初始化需要慎重考虑，因为初始化的数据需要根据用户当前屏幕中所在的位置（position）进行加载。
    通过LiveData，UI层级再次观察到了新的PagedList，并再次通过submitList()函数注入到PagedListAdapter中。
    和初次的数据渲染不同，这一次我们使用到了PagedListAdapter内部的AsyncPagedListDiffer对两个数据集进行差异性计算——这避免了notifyDataSetChanged()的滥用，同时，差异性计算的任务被切换到了后台线程中执行，一旦计算出差异性结果，新的PagedList会替换旧的PagedList，并对列表进行 增量更新。

    作者：却把清梅嗅
    链接：https://juejin.im/post/6844903976777809928
    来源：掘金
    著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     */
}

