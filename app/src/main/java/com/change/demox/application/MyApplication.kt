package com.change.demox.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.change.demox.R
import com.change.demox.repository.ITopRepository
import com.change.demox.repository.TopRepository
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.recyclerview.paging.delete.usecase.repository.ISearchRepository
import com.change.demox.views.recyclerview.paging.delete.usecase.repository.SearchRepository
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository.DataRepositoryImpl
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository.IDataRepository
import com.squareup.leakcanary.RefWatcher
import io.microshow.rxffmpeg.RxFFmpegInvoke

/**
 * Created by xingjunchao on 2020/08/24.
 */
class MyApplication : Application(), Application.ActivityLifecycleCallbacks {
    var context: Context? = null
        private set
    private var refWatcher: RefWatcher? = null

    /**
     * データ保存操作の対象
     */
    val preferences by lazy { SharedPreferences(applicationContext) }

    val topRepository: ITopRepository
        get() = TopRepository.instance(this)

    val searchRepository: ISearchRepository
        get() = SearchRepository.instance(this)


    val dataRepository: IDataRepository
        get() = DataRepositoryImpl.instance(this)

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        registerActivityLifecycleCallbacks(this)
        //开启/关闭 debug 模式，建议在 Application 初始化调用
        RxFFmpegInvoke.getInstance().setDebug(true)
    }

    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
            private set
        fun getRefWatcher(context: Context): RefWatcher? {
            val myApplication = context.applicationContext as MyApplication
            return myApplication.refWatcher
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (!preferences.isOrangeThemeColor) {
            activity.setTheme(R.style.AppTheme)
        } else {
            activity.setTheme(R.style.AppThemeOrange)
        }
    }

    override fun onActivityResumed(activity: Activity) {

    }

    fun showToast(content:String){
        Toast.makeText(this,content,Toast.LENGTH_LONG).show()
    }
}