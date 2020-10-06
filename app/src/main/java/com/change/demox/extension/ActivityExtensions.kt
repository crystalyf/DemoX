/*
 * ActivityExtensions.kt
 *
 * Created by kangzewei on 2020/05/29.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.extension

import android.app.Activity
import com.change.demox.ViewModelFactory
import com.change.demox.application.MyApplication
import com.change.demox.utils.SharedPreferences

/**
 *
 * 获取用于在Activity中创建ViewMode的Factory
 *
 * @return ViewModelFactory
 */
fun Activity.getViewModelFactory(): ViewModelFactory {
    val sharePref = SharedPreferences(applicationContext)
    val topRepository = (applicationContext as MyApplication).topRepository
    val dataRepository = (applicationContext as MyApplication).dataRepository
    return ViewModelFactory.getInstance(
            sharePref,
            topRepository,
            dataRepository
    )
}