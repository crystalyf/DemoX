package com.change.demox.views.recyclerview.banner

import com.change.demox.remote.RetrofitManager
import com.change.demox.views.recyclerview.banner.common.HomeBean
import com.change.demox.views.recyclerview.banner.component.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by xuhao on 2017/11/21.
 * desc: 首页精选 model
 */

class HomeViewModel{

    //直接在viewModel中就发协议了，没在RetrofitManager中再封一层调用函数，图省事了。

    /**
     * 获取首页 Banner 数据
     */
    fun requestHomeData(num:Int):Observable<HomeBean>{
        return RetrofitManager.retrofitService2.getFirstHomeData(num).compose(SchedulerUtils.ioToMain())
    }

    /**
     * 加载更多
     */
    fun loadMoreData(url:String):Observable<HomeBean>{
        return RetrofitManager.retrofitService2.getMoreHomeData(url)
                .compose(SchedulerUtils.ioToMain())
    }



}
