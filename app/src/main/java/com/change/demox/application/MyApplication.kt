package com.change.demox.application

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.change.demox.R
import com.change.demox.utils.SharedPreferences

/**
 * Created by xingjunchao on 2020/08/24.
 */
class MyApplication : Application(), Application.ActivityLifecycleCallbacks {
    var context: Context? = null
        private set

    /**
     * データ保存操作の対象
     */
    private val preferences by lazy { SharedPreferences(applicationContext) }

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