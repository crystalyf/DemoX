/*
 * FragmentExtensions.kt
 *
 * Created by kangzewei on 2020/05/29.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.extension

import androidx.fragment.app.Fragment
import com.change.demox.ViewModelFactory
import com.change.demox.application.MyApplication
import com.change.demox.utils.SharedPreferences

/**
 * Fragment中にViewModeを作成する使用のFactoryを取得
 *
 * @return ViewModelFactory
 */
fun Fragment.getViewModelFactory(): ViewModelFactory {
    val sharePref = SharedPreferences(requireContext().applicationContext)
    val topRepository = (requireContext().applicationContext as MyApplication).topRepository
    val dataRepository = (requireContext().applicationContext as MyApplication).dataRepository
    val searchRepository = (requireContext().applicationContext as MyApplication).searchRepository

    return ViewModelFactory.getInstance(
            sharePref,
            topRepository,
            dataRepository,
            searchRepository
    )
}
