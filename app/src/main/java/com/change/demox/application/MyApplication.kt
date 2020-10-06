package com.change.demox.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.change.demox.R
import com.change.demox.repository.ITopRepository
import com.change.demox.repository.TopRepository
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository.DataRepositoryImpl
import com.change.demox.views.recyclerview.paging.onlyshow.usecase.repository.IDataRepository

/**
 * Created by xingjunchao on 2020/08/24.
 */
class MyApplication : Application(), Application.ActivityLifecycleCallbacks {
    var context: Context? = null
        private set

    /**
     * データ保存操作の対象
     */
    val preferences by lazy { SharedPreferences(applicationContext) }

    val topRepository: ITopRepository
        get() = TopRepository.instance(this)

    val dataRepository: IDataRepository
        get() = DataRepositoryImpl.instance(this)

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        registerActivityLifecycleCallbacks(this)
    }

    companion object {
        @get:Synchronized
        var instance: MyApplication? = null
            private set
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
}