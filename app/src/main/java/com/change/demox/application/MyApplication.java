package com.change.demox.application;

import android.app.Application;
import android.content.Context;

import com.change.demox.themecolor.screen.Colorful;


/**
 * created by Fenrir-xingjunchao
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.mContext = getApplicationContext();
        initConfiguration();
    }

    /**
     * 初始化配置
     */
    private void initConfiguration() {
        // colorful
        Colorful.defaults()
                .primaryColor(Colorful.ThemeColor.BLUE)
                .accentColor(Colorful.ThemeColor.BLUE)
                .translucent(false)
                .dark(true);
        Colorful.init(this);
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    public Context getContext() {
        return mContext;
    }

}
