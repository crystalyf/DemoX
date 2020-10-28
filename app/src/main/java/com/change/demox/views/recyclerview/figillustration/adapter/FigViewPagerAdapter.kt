/*
 * FigViewPagerAdapter.kt
 *
 * Created by xingjunchao on 2020/06/17.
 * Copyright © 2020年 Kubota-PAD. All rights reserved.
 */

package com.change.demox.views.recyclerview.figillustration.adapter

import android.content.res.Configuration
import android.os.Parcelable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.change.demox.views.recyclerview.figillustration.ICurrentSelectedData
import com.change.demox.views.recyclerview.figillustration.IllustrationFragment
import com.change.demox.views.recyclerview.figillustration.bean.Illustration

/**
 * fig ViewPagerAdapter
 * @param fm
 */
class FigViewPagerAdapter(
        private val fm: FragmentManager?,
        private val figId: Int,
        private val illustration: List<Illustration?>,
        private val isFromLocal: Boolean,
        private val getSelectedData: ICurrentSelectedData,
        private val orientation: Int
) : FragmentStatePagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        /**
         * 内存优化：如果内存中根据tag查找有此部件的fragment, 那么先remove掉旧的，再重新加载新的。
         */
        val fragment =
                fm?.fragments?.find {
                    TextUtils.equals(
                            (it as IllustrationFragment).illustrationTag,
                            position.toString() + orientation.toString()
                    )
                }
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val oldFragment = fm?.fragments?.find {
                TextUtils.equals(
                        (it as IllustrationFragment).illustrationTag,
                        position.toString() + Configuration.ORIENTATION_LANDSCAPE.toString()
                )
            }
            if (oldFragment != null) {
                fm?.beginTransaction()?.remove(oldFragment)?.commit()
            }
        } else {
            val oldFragment =
                    fm?.fragments?.find {
                        TextUtils.equals(
                                (it as IllustrationFragment).illustrationTag,
                                position.toString() + Configuration.ORIENTATION_PORTRAIT.toString()
                        )
                    }
            if (oldFragment != null) {
                fm?.beginTransaction()?.remove(oldFragment)?.commit()
            }
        }
        return fragment ?: IllustrationFragment.newInstance(
                figId,
                illustration[position],
                isFromLocal,
                getSelectedData,
                position.toString() + orientation.toString()
        )
    }

    override fun getCount(): Int {
        return illustration.size
    }

    override fun saveState(): Parcelable? {
        return null
    }

}