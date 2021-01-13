/*
 * Ra9BaseFragment.kt
 *
 * Created by yananyu on 2019/12/12.
 * Copyright © 2019年 Eole. All rights reserved.
 */

package com.change.demox.views.flexboxlayout.component

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.change.demox.utils.SharedPreferences
import com.change.demox.views.widget.CommonDialog

/**
 * アプリ中使用のBaseFragment
 * LifeCycleのDebugログを出力
 * Fragmentの共通処理もここにおく
 *
 */
open class Ra9BaseFragment : Fragment() {

    /**
     * データ保存操作の対象
     */
    val preferences by lazy { SharedPreferences(requireContext()) }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        fragmentManager?.let {
            it.fragments.forEach { fragment ->
                if (fragment is CommonDialog<*>) {
                    fragment.clearListeners()
                    if (activity?.isFinishing == false) {
                        fragment.dismissAllowingStateLoss()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

}